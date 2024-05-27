package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.CategoriesDTO;
import com.example.backendkhoaluan.dto.RolesDTO;
import com.example.backendkhoaluan.entities.Categories;
import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.repository.RolesRepository;
import com.example.backendkhoaluan.service.imp.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> getListRole(){
        List<RolesDTO> list=roleService.getListRole();
        return BaseResponse.successListData(list,list.size());
    }
}
