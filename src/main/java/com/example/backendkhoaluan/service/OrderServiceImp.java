package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.config.VNPayConfig;
import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.OrderDetailDTO;
import com.example.backendkhoaluan.dto.OrdersDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.*;
import com.example.backendkhoaluan.entities.keys.KeyOrderDetail;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.PaymentException;
import com.example.backendkhoaluan.payload.request.PayRequest;
import com.example.backendkhoaluan.payload.response.MonthlySaleResponse;
import com.example.backendkhoaluan.repository.*;
import com.example.backendkhoaluan.service.imp.OrderService;
import com.example.backendkhoaluan.utils.HelperUtils;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private Gson gson = new Gson();

    private ModelMapper modelMapper=new ModelMapper();

    @Override
    public void deleteOrder(int id) {
        Optional<Orders> orders=ordersRepository.findById(id);
        if(!orders.isPresent()){
            throw new DataNotFoundException(Constants.ErrorMessageOrderValidation.NOT_FIND_ORDER_BY_ID+id);
        }
        deleteOrder(orders.get());
    }

    @Transactional
    @Override
    public void deleteOrder(Orders orders) {
        try {
            List<OrderDetail> listOrderDetails=orderDetailRepository.findByOrder(orders);
            listOrderDetails.forEach(orderDetail -> {
                orderDetailRepository.delete(orderDetail);
            });
            ordersRepository.delete(orders);
        }catch (Exception e){
            throw new DeleteException("Lỗi xóa giỏ hàng",e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public String createOrder(PayRequest request) {
        try {
            Orders orders = new Orders();
            User user = new User();
            user.setId(request.getIdUser());
            orders.setUser(user);
            orders.setCreateDate(new Date());
            orders.setTotalCost(request.getTotalCost());
            orders.setStatus(false);

            Orders order = ordersRepository.save(orders);

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (OrderDetailDTO dto : request.getOrderItem()) {
                OrderDetail orderDetail = new OrderDetail();

                KeyOrderDetail key = new KeyOrderDetail();
                key.setIdOrder(order.getId());
                key.setIdCourse(dto.getIdCourse());
                orderDetail.setId(key);

                orderDetail.setPrice(dto.getPrice());
                orderDetail.setCreateDate(new Date());
                orderDetail.setOrder(order);

                Courses courses=new Courses();
                courses.setId(dto.getIdCourse());
                orderDetail.setDescription(dto.getDescription());
                orderDetail.setCourse(courses);
                orderDetails.add(orderDetail);
            }

            orderDetailRepository.saveAll(orderDetails);

            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            int amount = request.getTotalCost() * 100;
            String bankCode = request.getVnpBankCode();

            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = "127.0.0.1";

            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");

            if (bankCode != null && !bankCode.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bankCode);
            }
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);

            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl+"?orderId="+order.getId());
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
            return paymentUrl;
        }catch (Exception e){
            throw new PaymentException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void updateOrder(Map<String, String> queryParams) {
        try{
            int orderId=Integer.parseInt(queryParams.get("orderId"));
            Orders orders = ordersRepository.findById(orderId)
                    .orElseThrow(() -> new DataNotFoundException("Không tồn tại đơn hàng có ID là: "+orderId));
            orders.setStatus(true);
            orders.setCreateDate(new Date());
            orders.setVnpBankCode(queryParams.get("vnp_BankCode"));

            List<OrderDetail> set=orders.getListOrderDetails();

            for(OrderDetail orderDetail:set){
                CourseDetail courseDetail=new CourseDetail();
                courseDetail.setIdUser(orders.getUser().getId());
                courseDetail.setCourse(orderDetail.getCourse());
                courseDetail.setEndDate(HelperUtils.getLimitDateTime(orderDetail.getCourse().getLimitTime()));

                courseDetailRepository.save(courseDetail);
            }

            ordersRepository.save(orders);
        }catch (Exception e){
            throw new PaymentException(e.getLocalizedMessage());
        }
    }

    @Override
    public List<Orders> findByUser(User user) {
        return ordersRepository.findByUser(user);
    }

    @Override
    public Page<Orders> getListOrder(CustomOrderQuery.OrderFilterParam param,
                                     PageRequest pageRequest,
                                     String header) {
        Set<String> roles=HelperUtils.getAuthorities();
        if(!roles.contains("ROLE_ADMIN")){
            String token = header.substring(7);
            String jwt = jwtUtilsHelper.verifyToken(token);

            UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
            param.setIdUser(user.getId());
        }
        Specification<Orders> specification = CustomOrderQuery.getFilterOrder(param);
        return ordersRepository.findAll(specification,pageRequest);
    }

    @Override
    public OrdersDTO findById(int id) {
        Optional<Orders> ordersOptional=ordersRepository.findById(id);
        if(!ordersOptional.isPresent()){
            throw new DataNotFoundException(Constants.ErrorMessageOrderValidation.NOT_FIND_ORDER_BY_ID+id);
        }
        Orders orders=ordersOptional.get();
        OrdersDTO ordersDTO=modelMapper.map(orders,OrdersDTO.class);
        return ordersDTO;
    }

    @Override
    public List<MonthlySaleResponse> getMonthlySale(int year) {
        // Khởi tạo danh sách chứa giá trị mặc định ban đầu cho các tháng từ tháng 1 đến tháng 12
        List<Double> monthlyTotalList = new ArrayList<>(Arrays.asList(new Double[12]));
        Collections.fill(monthlyTotalList, 0.0);

// Lấy kết quả từ cơ sở dữ liệu
        List<Object[]> results = ordersRepository.getTotalCostByMonthInYear(year);

// Duyệt qua kết quả và cập nhật giá trị tương ứng cho các tháng trong danh sách
        for (Object[] result : results) {
            int month = ((Number) result[0]).intValue(); // Lấy tháng từ kết quả
            double totalCost = ((Number) result[2]).doubleValue(); // Lấy tổng số tiền từ kết quả
            monthlyTotalList.set(month - 1, totalCost); // Cập nhật giá trị cho tháng tương ứng
        }

// Tạo danh sách chứa các đối tượng MonthlySaleResponse từ danh sách tổng số tiền của các tháng
        List<MonthlySaleResponse> monthlyTotals = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            double totalCost = monthlyTotalList.get(i); // Lấy giá trị từ danh sách tổng số tiền của các tháng
            monthlyTotals.add(new MonthlySaleResponse(totalCost, "Tháng " + (i + 1))); // Thêm vào danh sách MonthlySaleResponse
        }

        return monthlyTotals;
    }
}
