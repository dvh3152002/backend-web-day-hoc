package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.ProductsDTO;
import com.example.backendkhoaluan.entities.Products;
import com.example.backendkhoaluan.payload.request.ProductRequest;
import com.example.backendkhoaluan.repository.CustomProductQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductsDTO getById(int id);
    Page<Products> getListProduct(CustomProductQuery.ProductFilterParam param, PageRequest pageRequest);
    void updateProduct(int id, ProductRequest request, MultipartFile file);

    void deleteById(int id);

    void createProduct(ProductRequest request,MultipartFile file);
}
