package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.payload.request.GetUserRequest;
import com.example.backendkhoaluan.payload.request.CreateUserRequest;
import com.example.backendkhoaluan.payload.request.UpdateUserRequest;
import com.example.backendkhoaluan.payload.response.BaseListResponse;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    private ModelMapper modelMapper = new ModelMapper();

    @GetMapping("")
    public BaseListResponse<UsersDTO> getAllUser(@Valid @ModelAttribute GetUserRequest request) {
        Page<User> page = userService.getAllUser(request, PageRequest.of(request.getStart(), request.getLimit()));

        return BaseResponse.successListData(page.getContent().stream()
                .map(e -> {
                    UsersDTO usersDTO=modelMapper.map(e, UsersDTO.class);
                    if(e.getAvatar()!=null){
//                        usersDTO.setAvatar(cloudinaryService.getImageUrl(e.getAvatar()));
                    }
                    usersDTO.setRoles(modelMapper.map(e.getRoles(), Set.class));
                    return usersDTO;
                })
                .collect(Collectors.toList()), (int) page.getTotalElements());
    }

    @GetMapping("/{id}")
    public BaseResponse getDetailUser(@PathVariable("id") int id) {
        return BaseResponse.success(userService.findById(id));
    }

    @PutMapping(value = "/{id}")
    public BaseResponse updateUser(@PathVariable("id") int id, @Valid @ModelAttribute UpdateUserRequest request,
                                        @RequestParam(name = "file", required = false) MultipartFile file) {
        log.info("user :{}", request);
        log.info("file :{}", file);
        userService.updateUser(id, request, file);
        return BaseResponse.success("Cập nhật người dùng thành công");
    }

    @PostMapping(value = "")
    public BaseResponse createUser(@Valid @ModelAttribute CreateUserRequest request,
                                   @RequestParam(name = "file", required = false) MultipartFile file) {
        log.info("user :{}", request);
        log.info("file :{}", file);
        userService.createUser(request, file);
        return BaseResponse.success("Thêm người dùng thành công");
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteUser(@PathVariable("id") int id) {
        log.info("id: ", id);
        userService.deleteUser(id);
        return BaseResponse.success("Xóa thành công");
    }
}
