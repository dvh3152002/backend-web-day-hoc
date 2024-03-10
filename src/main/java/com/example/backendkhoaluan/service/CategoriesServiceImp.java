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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImp implements CategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<CategoriesDTO> getAll() {
        List<Categories> list = categoriesRepository.findAll();
        List<CategoriesDTO> dtoList = list.stream().map(data -> modelMapper.map(data, CategoriesDTO.class))
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        try {
            Optional<Categories> categoriesOptional=categoriesRepository.findById(id);
            if(!categoriesOptional.isPresent()){
                throw new DataNotFoundException(Constants.ErrorMessageCategoryValidation.NOT_FIND_CATEGORY_BY_ID +id);
            }
            categoriesRepository.deleteById(id);
        }catch (Exception e){
            throw new DeleteException("Xóa danh mục thất bại",e.getLocalizedMessage());
        }
    }

    @Override
    public CategoriesDTO getById(int id) {
        Optional<Categories> categoriesOptional=categoriesRepository.findById(id);
        if(!categoriesOptional.isPresent()){
            throw new DataNotFoundException(Constants.ErrorMessageCategoryValidation.NOT_FIND_CATEGORY_BY_ID +id);
        }
        Categories categories=categoriesOptional.get();
        CategoriesDTO categoriesDTO=new CategoriesDTO();
        categoriesDTO.setId(categories.getId());
        categoriesDTO.setName(categories.getName());
        return categoriesDTO;
    }

    @Override
    @Transactional
    public void save(String name) {
        try {
            Categories categories=new Categories();
            categories.setName(name);
            categoriesRepository.save(categories);
        }catch (Exception e){
            throw new InsertException("Thêm danh mục thất bại",e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void updateCategory(int id,String name) {
        try {
            Optional<Categories> categories=categoriesRepository.findById(id);
            if(!categories.isPresent()){
                throw new DataNotFoundException(Constants.ErrorMessageCategoryValidation.NOT_FIND_CATEGORY_BY_ID+id);
            }
            Categories data=categories.get();
                data.setName(name);
                categoriesRepository.save(data);
        }catch (Exception e){
            throw new UpdateException("Cập nhật danh mục thất bại",e.getLocalizedMessage());
        }
    }
}
