package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.constant.ErrorCodeDefs;
import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.dto.OrdersDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.payload.request.*;
import com.example.backendkhoaluan.payload.response.AuthResponse;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.payload.response.DashBoardResponse;
import com.example.backendkhoaluan.payload.response.MonthlySaleResponse;
import com.example.backendkhoaluan.repository.CustomCourseDetailQuery;
import com.example.backendkhoaluan.repository.CustomOrderQuery;
import com.example.backendkhoaluan.service.imp.AuthService;
import com.example.backendkhoaluan.service.imp.CourseService;
import com.example.backendkhoaluan.service.imp.OrderService;
import com.example.backendkhoaluan.service.imp.UserService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private final Gson gson = new Gson();

    private ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SignInRequest data) {
        AuthResponse authResponse = authService.signIn(data);

        return BaseResponse.success(authResponse);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody AuthResponse data) {
        AuthResponse authResponse = authService.refreshToken(data);

        return BaseResponse.success(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return BaseResponse.success(authService.register(createUserRequest));
    }

    @PutMapping("verify-account")
    public ResponseEntity<?> verifyAccount(@RequestBody VerifyAccountRequest request) {
        return BaseResponse.success(authService.verifyAccount(request));
    }

    @PutMapping("regenerate-otp")
    public ResponseEntity<?> regenerateOTP(@RequestParam String email) {
        return BaseResponse.success(authService.regenerateOTP(email));
    }

    @PutMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return BaseResponse.success(authService.forgotPassword(request));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @ModelAttribute UpdateUserRequest request,
                                           @RequestPart(name = "file", required = false) MultipartFile file,
                                           @RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        userService.updateUser(user.getId(), request, file);
        return BaseResponse.success("Cập nhật thành công");
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        UsersDTO usersDTO = userService.findByEmail(user.getEmail());
        return BaseResponse.success(usersDTO);

    }


    @GetMapping("/my-course")
    public ResponseEntity<?> getListCourse(@ModelAttribute CustomCourseDetailQuery.CourseDetailFilterParam param,
                                      @RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        param.setIdUser(user.getId());
        List<CoursesDTO> list = courseService.getListCourse(param);
        return BaseResponse.successListData(list, list.size());
    }

    @GetMapping("/my-order")
    public ResponseEntity<?> getListOrder(@ModelAttribute OrderRequest param,
                                     @RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        param.setIdUser(user.getId());
        Page<Orders> page = orderService.getListOrder(param, PageRequest.of(param.getStart(), param.getLimit()));
        return BaseResponse.successListData(page.getContent().stream()
                .map(data -> {
                    OrdersDTO ordersDTO = modelMapper.map(data, OrdersDTO.class);
                    return ordersDTO;
                }).collect(Collectors.toList()), (int) page.getTotalElements());
    }

    @GetMapping("/course-purchased")
    public ResponseEntity<?> getCoursePurchased(@ModelAttribute CustomCourseDetailQuery.CourseDetailFilterParam param,
                                           @RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        param.setIdUser(user.getId());
        Set<Integer> set = authService.getCoursePurchased(param);
        return BaseResponse.success(set);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                       @RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        authService.changePassword(user.getId(), request);
        return BaseResponse.success("Đổi mật khẩu thành công");
    }

    @GetMapping("/dashboard/{year}")
    public ResponseEntity<?> getDashboard(@PathVariable int year) {
        DashBoardResponse data = authService.getDashBoard(year);
        return BaseResponse.success(data);
    }
}
