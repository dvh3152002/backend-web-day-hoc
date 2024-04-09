package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.CategoriesDTO;
import com.example.backendkhoaluan.entities.Categories;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.exception.UpdateException;
import com.example.backendkhoaluan.repository.CategoriesRepository;
import com.example.backendkhoaluan.service.imp.CategoriesService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImp implements CategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private final ModelMapper modelMapper = new ModelMapper();

    private final Gson gson = new Gson();

    @Override
    public List<CategoriesDTO> getAll() {
        List<CategoriesDTO> dtoList = new ArrayList<>();
//        if (redisTemplate.hasKey("categories")) {
//            String data = redisTemplate.opsForValue().get("categories").toString();
//            Type listType = new TypeToken<List<CategoriesDTO>>() {
//            }.getType();
//            dtoList = gson.fromJson(data, listType);
//        } else {
        List<Categories> list = categoriesRepository.findAll();
        dtoList = list.stream().map(data -> modelMapper.map(data, CategoriesDTO.class))
                .collect(Collectors.toList());
//        String gsonList = gson.toJson(dtoList);
//            redisTemplate.opsForValue().set("categories", gsonList);
//        }
        return dtoList;
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        try {
            Optional<Categories> categoriesOptional = categoriesRepository.findById(id);
            if (!categoriesOptional.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageCategoryValidation.NOT_FIND_CATEGORY_BY_ID + id);
            }
            categoriesRepository.deleteById(id);

//            if (redisTemplate.hasKey("categories")) {
//                String data = redisTemplate.opsForValue().get("categories").toString();
//                Type listType = new TypeToken<List<CategoriesDTO>>() {
//                }.getType();
//                List<CategoriesDTO> dtoList = gson.fromJson(data, listType);
//
//                // Loại bỏ danh mục với id đã xóa
//                dtoList.removeIf(dto -> dto.getId() == id);
//
//                // Cập nhật lại Redis
//                redisTemplate.opsForValue().set("categories", gson.toJson(dtoList));
//            }
        } catch (Exception e) {
            throw new DeleteException("Xóa danh mục thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    public CategoriesDTO getById(int id) {
        Optional<Categories> categoriesOptional = categoriesRepository.findById(id);
        if (!categoriesOptional.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageCategoryValidation.NOT_FIND_CATEGORY_BY_ID + id);
        }
        Categories categories = categoriesOptional.get();
        CategoriesDTO categoriesDTO = new CategoriesDTO();
        categoriesDTO.setId(categories.getId());
        categoriesDTO.setName(categories.getName());
        return categoriesDTO;
    }

    @Override
    @Transactional
    public void save(String name) {
        try {
            Categories categories = new Categories();
            categories.setName(name);
            Categories c = categoriesRepository.save(categories);
            CategoriesDTO categoriesDTO = modelMapper.map(c, CategoriesDTO.class);

//            if (redisTemplate.hasKey("categories")) {
//                String data = redisTemplate.opsForValue().get("categories").toString();
//                Type listType = new TypeToken<List<CategoriesDTO>>() {
//                }.getType();
//                List<CategoriesDTO> dtoList = gson.fromJson(data, listType);
//                dtoList.add(categoriesDTO);
//                redisTemplate.opsForValue().set("categories", gson.toJson(dtoList));
//            }
        } catch (Exception e) {
            throw new InsertException("Thêm danh mục thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void updateCategory(int id, String name) {
        try {
            Optional<Categories> categories = categoriesRepository.findById(id);
            if (!categories.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageCategoryValidation.NOT_FIND_CATEGORY_BY_ID + id);
            }
            Categories data = categories.get();
            data.setName(name);
            data = categoriesRepository.save(data);
            CategoriesDTO categoriesDTO = modelMapper.map(data, CategoriesDTO.class);
//            if (redisTemplate.hasKey("categories")) {
//                String dataJson = redisTemplate.opsForValue().get("categories").toString();
//                Type listType = new TypeToken<List<CategoriesDTO>>() {
//                }.getType();
//                List<CategoriesDTO> dtoList = gson.fromJson(dataJson, listType);
//
//                // Tìm và cập nhật thông tin của danh mục trong danh sách
//                for (CategoriesDTO dto : dtoList) {
//                    if (dto.getId() == id) {
//                        dto.setName(categoriesDTO.getName());
//                        break;
//                    }
//                }
//
//                // Cập nhật lại Redis
//                redisTemplate.opsForValue().set("categories", gson.toJson(dtoList));
//            }
        } catch (Exception e) {
            throw new UpdateException("Cập nhật danh mục thất bại", e.getLocalizedMessage());
        }
    }
}
