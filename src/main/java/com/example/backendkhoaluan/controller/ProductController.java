package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.NewsDTO;
import com.example.backendkhoaluan.dto.ProductsDTO;
import com.example.backendkhoaluan.entities.News;
import com.example.backendkhoaluan.entities.Products;
import com.example.backendkhoaluan.payload.request.GetNewRequest;
import com.example.backendkhoaluan.payload.request.GetProductRequest;
import com.example.backendkhoaluan.payload.request.NewRequest;
import com.example.backendkhoaluan.payload.request.ProductRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.NewService;
import com.example.backendkhoaluan.service.imp.ProductService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CloudinaryService cloudinaryService;

    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return BaseResponse.success(productService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> createNew(@Valid @ModelAttribute ProductRequest request, @RequestParam MultipartFile file) {
        productService.createProduct(request,file);
        return BaseResponse.success("Thêm sản phẩm thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        productService.deleteById(id);
        return BaseResponse.success("Xóa sản phẩm thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @Valid @ModelAttribute ProductRequest request,
                                  @RequestParam(required = false) MultipartFile file) {
        productService.updateProduct(id, request,file);
        return BaseResponse.success("Cập nhật sản phẩm thành công");
    }

    @GetMapping("")
    public ResponseEntity<?> getListProduct(GetProductRequest request) {
        Page<Products> page = productService.getListProduct(request, PageRequest.of(request.getStart(), request.getLimit()));
        return BaseResponse.successListData(
                page.getContent().stream().map(data -> {
                    ProductsDTO productsDTO = modelMapper.map(data,ProductsDTO.class);
                    productsDTO.setImage(cloudinaryService.getImageUrl(data.getImage()));
                    return productsDTO;
                }).collect(Collectors.toList()), (int) page.getTotalElements());
    }
}
