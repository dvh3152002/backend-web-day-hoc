package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.RolesDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.*;
import com.example.backendkhoaluan.payload.request.GetUserRequest;
import com.example.backendkhoaluan.payload.request.UserRequest;
import com.example.backendkhoaluan.payload.response.ErrorDetail;
import com.example.backendkhoaluan.repository.*;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import com.example.backendkhoaluan.service.imp.OrderService;
import com.example.backendkhoaluan.service.imp.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper = new ModelMapper();

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
//        UsersDTO userDTO = new UsersDTO();
//        userDTO.setEmail(user.getEmail());
//        userDTO.setAddress(user.getAddress());
//        userDTO.setFullname(user.getFullname());
//        userDTO.setId(user.getId());
//
//        RolesDTO roleDTO = new RolesDTO();
//        roleDTO.setId(user.getRole().getId());
//        roleDTO.setName(user.getRole().getName());

        UsersDTO userDTO = modelMapper.map(user, UsersDTO.class);
        userDTO.setRolesDTOS(modelMapper.map(user.getRoles(),Set.class));

        if (user.getAvatar() != null) {
            if (!user.getAvatar().trim().equals("")) {
                userDTO.setAvatar("http://localhost:8081/api/file/image/" + user.getAvatar());
            }
        }
        return userDTO;
    }

    @Override
    public UsersDTO findByEmail(String email) {
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_EMAIL + email);
        }
        User user = userOptional.get();
//        UsersDTO userDTO = new UsersDTO();
//        userDTO.setEmail(user.getEmail());
//        userDTO.setAddress(user.getAddress());
//        userDTO.setFullname(user.getFullname());
//        userDTO.setId(user.getId());
//
//        RolesDTO roleDTO = new RolesDTO();
//        roleDTO.setId(user.getRole().getId());
//        roleDTO.setName(user.getRole().getName());

        UsersDTO userDTO = modelMapper.map(user, UsersDTO.class);
        userDTO.setRolesDTOS(modelMapper.map(user.getRoles(),Set.class));

        if (user.getAvatar() != null) {
            if (!user.getAvatar().trim().equals("")) {
                userDTO.setAvatar("http://localhost:8081/api/file/image/" + user.getAvatar());
            }
        }

        if (user.getAvatar() != null) {
            if (!user.getAvatar().trim().equals("")) {
                userDTO.setAvatar("http://localhost:8081/api/file/image/" + user.getAvatar());
            }
        }

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
//        UsersDTO userDTO = new UsersDTO();
//
//        userDTO.setEmail(user.getEmail());
//        userDTO.setAddress(user.getAddress());
//        userDTO.setFullname(user.getFullname());
//        userDTO.setId(user.getId());

        UsersDTO userDTO = modelMapper.map(user, UsersDTO.class);

        if (user.getAvatar() != null) {
            if (!user.getAvatar().trim().equals("")) {
                userDTO.setAvatar("http://localhost:8081/api/file/image/" + user.getAvatar());
            }
        }

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

//            User userEntity = new User();
//            userEntity.setFullname(userRequest.getFullname());
//            userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
//            userEntity.setAddress(userRequest.getAddress());
//            userEntity.setEmail(userRequest.getEmail());

            User userEntity = modelMapper.map(userRequest, User.class);
            if (avatar != null) {
                String fileName = filesStorageService.save(avatar);
                userEntity.setAvatar(fileName);
            }
            checkRoleUserExists(userEntity, userRequest.getRoles());
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
//            userEntity.setFullname(userRequest.getFullname());
//            userEntity.setAddress(userRequest.getAddress());
            modelMapper.map(userRequest, userEntity);
            if (avatar != null) {
                if (userEntity.getAvatar() != null) {
                    if (!userEntity.getAvatar().trim().equals("")) {
                        filesStorageService.deleteAll(userEntity.getAvatar());
                        String fileName = filesStorageService.save(avatar);
                        userEntity.setAvatar(fileName);
                    }
                }
            }
            usersRepository.save(userEntity);
        } catch (Exception e) {
            throw new UpdateException("Cập nhật người dùng thất bại", e.getLocalizedMessage());
        }
    }

    private void checkRoleUserExists(User user, Set<Integer> idRole) {
        if (!CollectionUtils.isEmpty(idRole)) {
            List<ErrorDetail> errorDetails = new ArrayList<>();
            List<Role> roles = rolesRepository.findAllById(idRole);
            for (Integer requestId : idRole) {
                boolean isExist = roles.stream().anyMatch(category -> category.getId().equals(requestId));
                if (!isExist) {
                    ErrorDetail errorDetail = new ErrorDetail();
                    errorDetail.setId(requestId.toString());
                    errorDetail.setMessage(Constants.ErrorMessageCategoryValidation.NOT_FIND_CATEGORY_BY_ID + requestId);
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
