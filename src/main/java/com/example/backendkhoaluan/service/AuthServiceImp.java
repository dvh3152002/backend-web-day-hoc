package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.*;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.EmailException;
import com.example.backendkhoaluan.exception.ErrorDetailException;
import com.example.backendkhoaluan.payload.request.*;
import com.example.backendkhoaluan.payload.response.AuthResponse;
import com.example.backendkhoaluan.payload.response.DashBoardResponse;
import com.example.backendkhoaluan.payload.response.ErrorDetail;
import com.example.backendkhoaluan.payload.response.MonthlySaleResponse;
import com.example.backendkhoaluan.repository.*;
import com.example.backendkhoaluan.service.imp.AuthService;
import com.example.backendkhoaluan.utils.EmailUtils;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.example.backendkhoaluan.utils.OtpUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.*;
import java.util.*;

@Service
@Slf4j
public class AuthServiceImp implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @Autowired
    private OtpUtils otpUtils;

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper = new ModelMapper();

    private final Gson gson = new Gson();

    @Override
    public AuthResponse signIn(SignInRequest request) {
        try {
            Optional<User> userOptional = usersRepository.findByEmail(request.getEmail());
            if (!userOptional.isPresent()) {
                throw new DataNotFoundException("Tài khoản hoặc mật khẩu không chính xác");
            }
            User user = userOptional.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new DataNotFoundException("Tài khoản hoặc mật khẩu không chính xác");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            if (user.isActive()) {
                UsersDTO usersDTO = new UsersDTO();
                usersDTO.setEmail(user.getEmail());
                usersDTO.setId(user.getId());

                String jwt = jwtUtilsHelper.generateToken(gson.toJson(usersDTO));
                String refreshToken = jwtUtilsHelper.generateRefreshToken(new HashMap<>(), user.getEmail());

                AuthResponse authResponse = new AuthResponse();
                authResponse.setRoles(modelMapper.map(user.getRoles(), List.class));
                authResponse.setAccessToken(jwt);
                authResponse.setRefreshToken(refreshToken);

                return authResponse;
            } else {
                throw new EmailException("Chưa xác thực email.");
            }
        } catch (Exception e) {
            throw new DataNotFoundException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public String register(CreateUserRequest request) {
        String email = request.getEmail();
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new RuntimeException("Tồn tại người dùng có email là: " + email);
        }

        String otp = otpUtils.generateOtp();
        User user = new User();
        user.setFullname(request.getFullname());
        user.setEmail(email);
        user.setAddress(request.getAddress());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setOtp(otp);
        user.setActive(false);
        user.setOtpGeneratedTime(LocalDateTime.now());

        checkRoleUserExists(user, request.getRoles());
        log.info("user: {}",user);
        log.info("roles: {}",request.getRoles());


        usersRepository.save(user);
        emailUtils.sendOtpEmail(email, otp);
        return "Đăng ký thành công. Hãy xác thực email để đăng nhập";
    }

    @Override
    public String verifyAccount(VerifyAccountRequest request) {
        String email=request.getEmail();
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException("Không tồn tại người dùng có email là: " + email);
        }
        User user = userOptional.get();
        if (user.getOtp().equals(request.getOtp()) &&
                Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setActive(true);
            usersRepository.save(user);
            return "OTP đã xác thực thành công.";
        } else {
            throw new RuntimeException("Hãy tạo lại OTP và thử lại.");
        }
    }

    @Override
    public String regenerateOTP(String email) {
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException("Không tồn tại người dùng có email là: " + email);
        }
        String otp = otpUtils.generateOtp();
        User user = userOptional.get();
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        usersRepository.save(user);
        emailUtils.sendOtpEmail(email,otp);
        return "Email đã gửi... Hãy xác thực trong 1 phút";
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        String email=request.getEmail();
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException("Không tồn tại người dùng có email là: " + email);
        }
        User user = userOptional.get();
        if (user.getOtp().equals(request.getOtp()) &&
                Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            usersRepository.save(user);
            return "OTP đã xác thực thành công.";
        } else {
            throw new RuntimeException("Hãy tạo lại OTP và thử lại.");
        }
    }

    @Override
    public AuthResponse refreshToken(AuthResponse authResponse) {
        AuthResponse response = new AuthResponse();
        String email = jwtUtilsHelper.extractEmail(authResponse.getAccessToken());
        User user = usersRepository.findByEmail(email).orElseThrow();
        if (jwtUtilsHelper.isTokenValid(authResponse.getRefreshToken(), user.getEmail())) {
            String jwt = jwtUtilsHelper.generateToken(user.getEmail());
            authResponse.setRoles(modelMapper.map(user.getRoles(), List.class));
            response.setAccessToken(jwt);
            response.setRefreshToken(authResponse.getAccessToken());
        }
        return response;
    }

    @Override
    public Set<Integer> getCoursePurchased(CustomCourseDetailQuery.CourseDetailFilterParam param) {
        Specification<CourseDetail> specification = CustomCourseDetailQuery.getFilterCourse(param);
        List<CourseDetail> list = courseDetailRepository.findAll(specification);
        Set<Integer> set = new HashSet<>();

        for (CourseDetail courseDetail : list) {
            set.add(courseDetail.getCourse().getId());
        }
        return set;
    }

    @Override
    public void changePassword(Integer id, ChangePasswordRequest request) {
        Optional<User> userOptional = usersRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_ID + id);
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new DataNotFoundException("Mật khẩu không đúng");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepository.save(user);
    }

    @Override
    public DashBoardResponse getDashBoard(int year) {
        LocalDate startDate = Year.of(year).atDay(1);
        LocalDate endDate = Year.of(year).atDay(1).plusYears(1).minusDays(1);

        // Convert LocalDate to Date for JPA query
        long start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long end = endDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant().toEpochMilli();
        DashBoardResponse response=new DashBoardResponse();

        List<MonthlySaleResponse> list=getMonthlySale(year);
        response.setListSale(list);
        response.setCountUser((int) usersRepository.count());

        CustomCourseQuery.CourseFilterParam courseParam=new GetCourseRequest();
        courseParam.setStartDate(start);
        courseParam.setEndDate(end);
        Specification<Courses> courseSpecification=CustomCourseQuery.getFilterCourse(courseParam);
        response.setCountCourse((int) coursesRepository.count(courseSpecification));

        CustomNewQuery.NewFilterParam newParam=new GetNewRequest();
        newParam.setStartDate(start);
        newParam.setEndDate(end);
        Specification<News> newSpecification= CustomNewQuery.getFilterNew(newParam);
        response.setCountNew((int) newsRepository.count(newSpecification));

        CustomOrderQuery.OrderFilterParam orderParam=new OrderRequest();
        orderParam.setStartDate(start);
        orderParam.setEndDate(end);
        Specification<Orders> ordersSpecification=CustomOrderQuery.getFilterOrder(orderParam);
        response.setCountOrder((int) ordersRepository.count(ordersSpecification));

        int totalCost=0;
        for (MonthlySaleResponse data:list){
            totalCost+=data.getTotalCost();
        }
        response.setTotalCost(totalCost);
        return response;
    }

    private void checkRoleUserExists(User user, Set<Integer> idRole) {
        if (!CollectionUtils.isEmpty(idRole)) {
            List<ErrorDetail> errorDetails = new ArrayList<>();
            List<Role> roles = rolesRepository.findAllById(idRole);
            for (Integer requestId : idRole) {
                boolean isExist = roles.stream().anyMatch(role -> role.getId().equals(requestId));
                if (!isExist) {
                    ErrorDetail errorDetail = new ErrorDetail();
                    errorDetail.setId(requestId.toString());
                    errorDetail.setMessage(Constants.ErrorMessageRoleValidation.NOT_FIND_ROLE_BY_ID + requestId);
                    errorDetails.add(errorDetail);
                }
            }
            if (!CollectionUtils.isEmpty(errorDetails)) {
                throw new ErrorDetailException(errorDetails);
            }
            user.setRoles(roles);
        }
    }

    private List<MonthlySaleResponse> getMonthlySale(int year) {
        // Khởi tạo danh sách chứa giá trị mặc định ban đầu cho các tháng từ tháng 1 đến tháng 12
        List<Integer> monthlyTotalList = new ArrayList<>(Arrays.asList(new Integer[12]));
        Collections.fill(monthlyTotalList, 0);

// Lấy kết quả từ cơ sở dữ liệu
        List<Object[]> results = ordersRepository.getTotalCostByMonthInYear(year);

// Duyệt qua kết quả và cập nhật giá trị tương ứng cho các tháng trong danh sách
        for (Object[] result : results) {
            int month = ((Number) result[0]).intValue(); // Lấy tháng từ kết quả
            int totalCost = ((Number) result[2]).intValue(); // Lấy tổng số tiền từ kết quả
            monthlyTotalList.set(month - 1, totalCost); // Cập nhật giá trị cho tháng tương ứng
        }

// Tạo danh sách chứa các đối tượng MonthlySaleResponse từ danh sách tổng số tiền của các tháng
        List<MonthlySaleResponse> monthlyTotals = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int totalCost = monthlyTotalList.get(i); // Lấy giá trị từ danh sách tổng số tiền của các tháng
            monthlyTotals.add(new MonthlySaleResponse(totalCost, "Tháng " + (i + 1))); // Thêm vào danh sách MonthlySaleResponse
        }

        return monthlyTotals;
    }
}
