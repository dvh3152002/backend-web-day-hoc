package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.config.VNPayConfig;
import com.example.backendkhoaluan.dto.LessonsDTO;
import com.example.backendkhoaluan.dto.OrdersDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Lessons;
import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.payload.request.GetLessonRequest;
import com.example.backendkhoaluan.payload.request.OrderRequest;
import com.example.backendkhoaluan.payload.request.PayRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.payload.response.MonthlySaleResponse;
import com.example.backendkhoaluan.service.imp.OrderService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    @Value("${root.frontend.url}")
    private String frontendUrl;

    private final Gson gson = new Gson();

    private ModelMapper modelMapper=new ModelMapper();

    @GetMapping("/payment-callback")
    public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");

        String orderId = queryParams.get("orderId");
        if(orderId!= null && !orderId.equals("")) {
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công
                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
                orderService.updateOrder(queryParams);
                response.sendRedirect(frontendUrl+"/payment?isSuccess="+true);
            } else {
                // Giao dịch thất bại
                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
                response.sendRedirect(frontendUrl+"/payment?isSuccess="+false);
            }
        }else {
            // Giao dịch thất bại
            // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
            response.sendRedirect(frontendUrl+"/payment?isSuccess="+false);
        }
    }

    @PostMapping("/pay")
    public BaseResponse getPay(@RequestBody PayRequest request,
                               @RequestHeader("Authorization") String header) throws UnsupportedEncodingException {
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);
        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        request.setIdUser(user.getId());
        String paymentUrl=orderService.createOrder(request);

        return BaseResponse.success(paymentUrl);
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteOrder(@PathVariable int id){
        orderService.deleteOrder(id);
        return BaseResponse.success("Xóa đơn hàng thành công");
    }

    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable int id){
        OrdersDTO dto= orderService.findById(id);
        return BaseResponse.success(dto);
    }

    @GetMapping("")
    public BaseResponse getListOrders(@Valid OrderRequest request){
        Page<Orders> page=orderService.getListOrder(request, PageRequest.of(request.getStart(),request.getLimit()));
        return BaseResponse.successListData(page.getContent().stream()
                .map(data->{
                    OrdersDTO ordersDTO=modelMapper.map(data,OrdersDTO.class);
                    return ordersDTO;
                }).collect(Collectors.toList()), (int) page.getTotalElements());
    }
}
