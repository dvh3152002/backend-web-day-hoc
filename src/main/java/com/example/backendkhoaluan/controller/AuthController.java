package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.constant.ErrorCodeDefs;
import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.payload.request.*;
import com.example.backendkhoaluan.payload.response.AuthResponse;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.repository.CustomCourseDetailQuery;
import com.example.backendkhoaluan.service.imp.AuthService;
import com.example.backendkhoaluan.service.imp.CourseService;
import com.example.backendkhoaluan.service.imp.UserService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

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
    private JwtUtilsHelper jwtUtilsHelper;

    private final Gson gson = new Gson();

    @PostMapping("/signin")
    public BaseResponse signin(@Valid @RequestBody SignInRequest data) {
        AuthResponse authResponse = authService.signIn(data);

        return BaseResponse.success(authResponse);
    }

    @PostMapping("/refreshToken")
    public BaseResponse refreshToken(@RequestBody AuthResponse data) {
        AuthResponse authResponse = authService.refreshToken(data);

        return BaseResponse.success(authResponse);
    }

    @PostMapping("/signup")
    public BaseResponse register(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return BaseResponse.success(authService.register(createUserRequest));
    }

    @PutMapping("verify-account")
    public BaseResponse verifyAccount(@RequestBody VerifyAccountRequest request){
        return BaseResponse.success(authService.verifyAccount(request));
    }

    @PutMapping("regenerate-otp")
    public BaseResponse regenerateOTP(@RequestParam String email){
        return BaseResponse.success(authService.regenerateOTP(email));
    }

    @PutMapping("/profile")
    public BaseResponse updateProfile(@Valid @ModelAttribute UpdateUserRequest request,
                                      @RequestPart(name = "file", required = false) MultipartFile file,
                                      @RequestHeader("Authorization") String header) {
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (token != null) {
                String jwt = jwtUtilsHelper.verifyToken(token);

                if (jwt != null) {
                    UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
                    userService.updateUser(user.getId(), request, file);
                    return BaseResponse.success("Cập nhật thành công");
                }
            }
        }
        return BaseResponse.error(ErrorCodeDefs.ERR_HEADER_TOKEN_REQUIRED, ErrorCodeDefs.getMessage(ErrorCodeDefs.ERR_HEADER_TOKEN_REQUIRED));
    }

    @GetMapping("/profile")
    public BaseResponse getProfile(@RequestHeader("Authorization") String header) {
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (token != null) {
                String jwt = jwtUtilsHelper.verifyToken(token);

                if (jwt != null) {
                    UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
                    UsersDTO usersDTO = userService.findByEmail(user.getEmail());
                    return BaseResponse.success(usersDTO);
                }
            }
        }
        return BaseResponse.error(ErrorCodeDefs.ERR_HEADER_TOKEN_REQUIRED, ErrorCodeDefs.getMessage(ErrorCodeDefs.ERR_HEADER_TOKEN_REQUIRED));
    }

    @GetMapping("/my-course")
    public BaseResponse getListCourse(@ModelAttribute CustomCourseDetailQuery.CourseDetailFilterParam param,
                                      @RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        param.setIdUser(user.getId());
        List<CoursesDTO> list = courseService.getListCourse(param);
        return BaseResponse.successListData(list,list.size());
    }

    @GetMapping("/course-purchased")
    public BaseResponse getCoursePurchased(@ModelAttribute CustomCourseDetailQuery.CourseDetailFilterParam param,
                                           @RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        param.setIdUser(user.getId());
        Set<Integer> set=authService.getCoursePurchased(param);
        return BaseResponse.success(set);
    }

    @PutMapping("/change-password")
    public BaseResponse changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                       @RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        authService.changePassword(user.getId(),request);
        return BaseResponse.success("Đổi mật khẩu thành công");
    }
}
