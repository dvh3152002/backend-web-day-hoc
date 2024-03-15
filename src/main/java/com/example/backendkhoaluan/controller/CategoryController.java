package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.CategoriesDTO;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("")
    public BaseResponse getList(){
        List<CategoriesDTO> list=categoriesService.getAll();
        return BaseResponse.successListData(list,list.size());
    }

    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable int id){
        return BaseResponse.success(categoriesService.getById(id));
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable int id){
        categoriesService.deleteCategory(id);
        return BaseResponse.success("Xóa danh mục thành công");
    }

    @PostMapping("")
    public BaseResponse insert(String name){
        categoriesService.save(name);
        return BaseResponse.success("Thêm danh mục thành công");
    }

    @PutMapping("/{id}")
    public BaseResponse updateById(@PathVariable int id,String name){
        categoriesService.updateCategory(id,name);
        return BaseResponse.success("Cập nhật danh mục thành công");
    }
}
