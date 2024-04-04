package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.*;
import com.example.backendkhoaluan.payload.request.CreateUserRequest;
import com.example.backendkhoaluan.payload.request.UpdateUserRequest;
import com.example.backendkhoaluan.payload.response.ErrorDetail;
import com.example.backendkhoaluan.repository.*;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import com.example.backendkhoaluan.service.imp.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private CloudinaryService cloudinaryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${root.path.image}")
    private String path;

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
        if(user.getAvatar()!=null){
            userDTO.setAvatar(cloudinaryService.getImageUrl(user.getAvatar()));
        }
        userDTO.setRoles(modelMapper.map(user.getRoles(),Set.class));

        return userDTO;
    }

    @Override
    public UsersDTO findByEmail(String email) {
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_EMAIL + email);
        }
        User user = userOptional.get();

        UsersDTO userDTO = modelMapper.map(user, UsersDTO.class);
        if(user.getAvatar()!=null){
            userDTO.setAvatar(cloudinaryService.getImageUrl(user.getAvatar()));
        }
        userDTO.setRoles(modelMapper.map(user.getRoles(),Set.class));

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
            User user=users.get();
            if(user.getAvatar()!=null){
                cloudinaryService.deleteFile(user.getAvatar());
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

        UsersDTO userDTO = modelMapper.map(user, UsersDTO.class);

        return userDTO;
    }

    @Transactional
    @Override
    public String createUser(CreateUserRequest createUserRequest, MultipartFile avatar) {
        try {
            log.info("createUser - request: {}", createUserRequest);
            Optional<User> user = usersRepository.findByEmail(createUserRequest.getEmail());
            if (user.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageUserValidation.EMAIL_IS_EXIST);
            }

            User userEntity = modelMapper.map(createUserRequest, User.class);
            userEntity.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
            if (avatar != null) {
                String fileName = cloudinaryService.uploadFile(avatar);
                userEntity.setAvatar(fileName);
            }
            checkRoleUserExists(userEntity, createUserRequest.getRoles());
            usersRepository.save(userEntity);
            return "Thêm người dùng thành công";
        } catch (Exception e) {
            throw new InsertException("Thêm người dùng thất bại", e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public void updateUser(int id, UpdateUserRequest request, MultipartFile avatar) {
        try {
            Optional<User> users = usersRepository.findById(id);
            if (!users.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_ID + id);
            }
            User userEntity = users.get();
            String password=userEntity.getPassword();
            modelMapper.map(request, userEntity);

            userEntity.setPassword(password);
            if (avatar != null) {
                if (userEntity.getAvatar() != null) {
                    if (!userEntity.getAvatar().trim().equals("")) {
                        cloudinaryService.deleteFile(userEntity.getAvatar());
                        String fileName = cloudinaryService.uploadFile(avatar);
                        userEntity.setAvatar(fileName);
                    }
                }
            }
            checkRoleUserExists(userEntity, request.getRoles());
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
