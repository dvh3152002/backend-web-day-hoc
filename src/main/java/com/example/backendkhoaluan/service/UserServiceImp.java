package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.dto.RolesDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.exception.UpdateException;
import com.example.backendkhoaluan.payload.request.UserRequest;
import com.example.backendkhoaluan.repository.RatingCourseRepository;
import com.example.backendkhoaluan.repository.UsersRepository;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import com.example.backendkhoaluan.service.imp.OrderService;
import com.example.backendkhoaluan.service.imp.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public List<UsersDTO> getAllUser(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> listData = usersRepository.findAll(pageRequest);

        List<UsersDTO> userDTOList = new ArrayList<>();

        for (User user : listData) {
            UsersDTO userDTO = new UsersDTO();
            userDTO.setId(user.getId());
            userDTO.setAddress(user.getAddress());
            userDTO.setFullname(user.getFullname());
            userDTO.setEmail(user.getEmail());

            userDTO.setIdRole(user.getRole().getId());

            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    @Override
    public UsersDTO findById(int id) {
        Optional<User> userOptional = usersRepository.findById(id);
        UsersDTO userDTO = new UsersDTO();
        if (userOptional.isPresent()) {
            userOptional.ifPresent(user -> {
                userDTO.setEmail(user.getEmail());
                userDTO.setAddress(user.getAddress());
                userDTO.setFullname(user.getFullname());
                userDTO.setId(user.getId());

                RolesDTO roleDTO = new RolesDTO();
                roleDTO.setId(user.getRole().getId());
                roleDTO.setName(user.getRole().getName());

                if(user.getAvatar()!=null) {
                    if(!user.getAvatar().trim().equals("")){
                        userDTO.setAvatar("http://localhost:8081/api/file/" + user.getAvatar());
                    }
                }

                userDTO.setIdRole(user.getRole().getId());
            });
            return userDTO;
        }
        return null;
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        try {
            Optional<User> users = usersRepository.findById(id);
            users.ifPresent(data -> {
                List<RatingCourse> listRatingCourses = ratingCourseRepository.findByUser(data);
                listRatingCourses.forEach(ratingCourse -> {
                    ratingCourseRepository.delete(ratingCourse);
                });
                List<Orders> listOrders = orderService.findByUser(data);
                listOrders.forEach(orders -> {
                    orderService.deleteOrder(orders);
                });
            });
            usersRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeleteException("Xóa người dùng thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    public UsersDTO checkLogin(String email, String password) {
        User user = usersRepository.findByEmail(email).orElseThrow();
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                UsersDTO userDTO = new UsersDTO();

                userDTO.setEmail(user.getEmail());
                userDTO.setAddress(user.getAddress());
                userDTO.setFullname(user.getFullname());
                userDTO.setId(user.getId());

                userDTO.setIdRole(user.getRole().getId());

                return userDTO;
            }
        }
        return null;
    }

    @Transactional
    @Override
    public String createUser(UserRequest userRequest, MultipartFile avatar) {
        try {
            log.info("createUser - request: {}", userRequest);
            Role roleEntity = new Role();
            roleEntity.setId(userRequest.getRoleId());

            User userEntity = new User();
            userEntity.setFullname(userRequest.getFullname());
            userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userEntity.setAddress(userRequest.getAddress());
            userEntity.setEmail(userRequest.getEmail());
            if (avatar != null) {
                String fileName=filesStorageService.save(avatar);
                userEntity.setAvatar(fileName);
            }
            userEntity.setRole(roleEntity);
            usersRepository.save(userEntity);
            return "Thêm người dùng thành công";
        }catch (Exception e){
            throw new InsertException("Thêm người dùng thất bại",e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public void updateUser(int id, UserRequest userRequest, MultipartFile avatar) {
        try {
            Optional<User> user = usersRepository.findById(id);
            user.ifPresent(userEntity->{
                Role roleEntity = new Role();
                roleEntity.setId(userRequest.getRoleId());
                userEntity.setFullname(userRequest.getFullname());
                userEntity.setAddress(userRequest.getAddress());
                if (avatar != null) {
                    if(userEntity.getAvatar()!=null) {
                        if (!userEntity.getAvatar().trim().equals("")) {
                            filesStorageService.deleteAll(userEntity.getAvatar());
                            String fileName = filesStorageService.save(avatar);
                            userEntity.setAvatar(fileName);
                        }
                    }
                }
                userEntity.setRole(roleEntity);
                usersRepository.save(userEntity);
            });
        } catch (Exception e) {
            throw new UpdateException("Cập nhật người dùng thất bại", e.getLocalizedMessage());
        }
    }
}
