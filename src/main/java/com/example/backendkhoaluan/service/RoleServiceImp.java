package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.dto.CategoriesDTO;
import com.example.backendkhoaluan.dto.RolesDTO;
import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.repository.RolesRepository;
import com.example.backendkhoaluan.service.imp.RoleService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImp implements RoleService {
    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private ModelMapper modelMapper=new ModelMapper();

    private Gson gson=new Gson();
    @Override
    public List<RolesDTO> getListRole() {
        List<RolesDTO> dtoList;
        if(redisTemplate.hasKey("roles")){
            String data = redisTemplate.opsForValue().get("roles").toString();
            Type listType = new TypeToken<List<RolesDTO>>() {
            }.getType();
            dtoList = gson.fromJson(data, listType);
        }else {
            List<Role> list = rolesRepository.findAll();
            dtoList = list.stream().map(data -> modelMapper.map(data, RolesDTO.class))
                    .collect(Collectors.toList());
        }
        return dtoList;
    }
}
