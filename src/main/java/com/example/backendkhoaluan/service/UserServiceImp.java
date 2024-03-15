package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.RolesDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.exception.UpdateException;
import com.example.backendkhoaluan.payload.request.GetUserRequest;
import com.example.backendkhoaluan.payload.request.UserRequest;
import com.example.backendkhoaluan.repository.CustomCourseQuery;
import com.example.backendkhoaluan.repository.CustomeUserQuery;
import com.example.backendkhoaluan.repository.RatingCourseRepository;
import com.example.backendkhoaluan.repository.UsersRepository;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import com.example.backendkhoaluan.service.imp.OrderService;
import com.example.backendkhoaluan.service.imp.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private RatingCourseRepository ratingCourseRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<User> getAllUser(CustomeUserQuery.UserFilterParam param, PageRequest pageRequest) {
        Specification<User> specification = CustomeUserQuery.getFilterUser(param);
        return usersRepository.findAll(specification, pageRequest);
    }

    @Override
    public UsersDTO findById(int id) {
        Optional<User> userOptional = usersRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_ID + id);
        }
        User user = userOptional.get();
        UsersDTO userDTO = new UsersDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setFullname(user.getFullname());
        userDTO.setId(user.getId());

        RolesDTO roleDTO = new RolesDTO();
        roleDTO.setId(user.getRole().getId());
        roleDTO.setName(user.getRole().getName());

        if (user.getAvatar() != null) {
            if (!user.getAvatar().trim().equals("")) {
                userDTO.setAvatar("http://localhost:8081/api/file/image/" + user.getAvatar());
            }
        }

        userDTO.setIdRole(user.getRole().getId());
        return userDTO;
    }

    @Override
    public UsersDTO findByEmail(String email) {
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_EMAIL + email);
        }
        User user = userOptional.get();
        UsersDTO userDTO = new UsersDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setFullname(user.getFullname());
        userDTO.setId(user.getId());

        RolesDTO roleDTO = new RolesDTO();
        roleDTO.setId(user.getRole().getId());
        roleDTO.setName(user.getRole().getName());

        if (user.getAvatar() != null) {
            if (!user.getAvatar().trim().equals("")) {
                userDTO.setAvatar("http://localhost:8081/api/file/image/" + user.getAvatar());
            }
        }

        userDTO.setIdRole(user.getRole().getId());
        return userDTO;
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        try {
            Optional<User> users = usersRepository.findById(id);
            if (!users.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_ID + id);
            }
            User data = users.get();
            List<RatingCourse> listRatingCourses = ratingCourseRepository.findByUser(data);
            listRatingCourses.forEach(ratingCourse -> {
                ratingCourseRepository.delete(ratingCourse);
            });
            List<Orders> listOrders = orderService.findByUser(data);
            listOrders.forEach(orders -> {
                orderService.deleteOrder(orders);
            });
            usersRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeleteException("Xóa người dùng thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    public UsersDTO checkLogin(String email, String password) {
        Optional<User> users = usersRepository.findByEmail(email);
        if (!users.isPresent()) {
            throw new DataNotFoundException("Email hoặc mật khẩu không đúng");
        }
        User user = users.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new DataNotFoundException("Email hoặc mật khẩu không đúng");
        }
        UsersDTO userDTO = new UsersDTO();

        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setFullname(user.getFullname());
        userDTO.setId(user.getId());

        userDTO.setIdRole(user.getRole().getId());

        return userDTO;
    }

    @Transactional
    @Override
    public String createUser(UserRequest userRequest, MultipartFile avatar) {
        try {
            log.info("createUser - request: {}", userRequest);
            Optional<User> user = usersRepository.findByEmail(userRequest.getEmail());
            if (user.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageUserValidation.EMAIL_IS_EXIST);
            }

            Role roleEntity = new Role();
            roleEntity.setId(userRequest.getRoleId());

            User userEntity = new User();
            userEntity.setFullname(userRequest.getFullname());
            userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userEntity.setAddress(userRequest.getAddress());
            userEntity.setEmail(userRequest.getEmail());
            if (avatar != null) {
                String fileName = filesStorageService.save(avatar);
                userEntity.setAvatar(fileName);
            }
            userEntity.setRole(roleEntity);
            usersRepository.save(userEntity);
            return "Thêm người dùng thành công";
        } catch (Exception e) {
            throw new InsertException("Thêm người dùng thất bại", e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public void updateUser(int id, UserRequest userRequest, MultipartFile avatar) {
        try {
            Optional<User> users = usersRepository.findById(id);
            if (!users.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_ID + id);
            }
            User userEntity = users.get();
            Role roleEntity = new Role();
            roleEntity.setId(userRequest.getRoleId());
            userEntity.setFullname(userRequest.getFullname());
            userEntity.setAddress(userRequest.getAddress());
            if (avatar != null) {
                if (userEntity.getAvatar() != null) {
                    if (!userEntity.getAvatar().trim().equals("")) {
                        filesStorageService.deleteAll(userEntity.getAvatar());
                        String fileName = filesStorageService.save(avatar);
                        userEntity.setAvatar(fileName);
                    }
                }
            }
            userEntity.setRole(roleEntity);
            usersRepository.save(userEntity);
        } catch (Exception e) {
            throw new UpdateException("Cập nhật người dùng thất bại", e.getLocalizedMessage());
        }
    }
}
