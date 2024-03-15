package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.payload.request.GetUserRequest;
import com.example.backendkhoaluan.payload.request.UserRequest;
import com.example.backendkhoaluan.payload.response.BaseListResponse;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    private ModelMapper modelMapper = new ModelMapper();

    @GetMapping("")
    public BaseListResponse<UsersDTO> getAllUser(@Valid GetUserRequest request) {
        Page<User> page = userService.getAllUser(request, PageRequest.of(request.getStart(), request.getLimit()));

        return BaseResponse.successListData(page.getContent().stream()
                .map(e -> {
                    UsersDTO usersDTO=modelMapper.map(e, UsersDTO.class);
                    if(usersDTO.getAvatar()!=null){
                        usersDTO.setAvatar("http://localhost:8081/api/file/"+usersDTO.getAvatar());
                    }
                    return usersDTO;
                })
                .collect(Collectors.toList()), (int) page.getTotalElements());
    }

    @GetMapping("/{id}")
    public BaseResponse getDetailUser(@PathVariable("id") int id) {
        return BaseResponse.success(userService.findById(id));
    }

    @PutMapping(value = "/{id}")
    public BaseResponse updateUser(@PathVariable("id") int id, @Valid @RequestBody UserRequest userRequest,
                                        @RequestParam(name = "file", required = false) MultipartFile file) {
        log.info("user :{}", userRequest);
        log.info("file :{}", file);
        userService.updateUser(id, userRequest, file);
        return BaseResponse.success("Cập nhật người dùng thành công");
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteUser(@PathVariable("id") int id) {
        log.info("id: ", id);
        userService.deleteUser(id);
        return BaseResponse.success("Xóa thành công");
    }
}
