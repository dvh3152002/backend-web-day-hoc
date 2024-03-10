package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.CategoriesDTO;

import java.util.List;

public interface CategoriesService {
    List<CategoriesDTO> getAll();
    void deleteCategory(int id);
    CategoriesDTO getById(int id);
    void save(String name);
    void updateCategory(int id,String name);
}
