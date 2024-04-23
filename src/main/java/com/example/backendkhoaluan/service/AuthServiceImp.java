package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.dto.RolesDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.CourseDetail;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.EmailException;
import com.example.backendkhoaluan.exception.ErrorDetailException;
import com.example.backendkhoaluan.payload.request.ChangePasswordRequest;
import com.example.backendkhoaluan.payload.request.CreateUserRequest;
import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.request.VerifyAccountRequest;
import com.example.backendkhoaluan.payload.response.AuthResponse;
import com.example.backendkhoaluan.payload.response.ErrorDetail;
import com.example.backendkhoaluan.repository.CourseDetailRepository;
import com.example.backendkhoaluan.repository.CustomCourseDetailQuery;
import com.example.backendkhoaluan.repository.RolesRepository;
import com.example.backendkhoaluan.repository.UsersRepository;
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

import java.time.Duration;
import java.time.LocalDateTime;
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
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Optional<User> userOptional = usersRepository.findByEmail(request.getEmail());
            if (!userOptional.isPresent()) {
                throw new DataNotFoundException("Không tồn tại người dùng có email là: " + request.getEmail());
            }
            User user = userOptional.get();

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
}
