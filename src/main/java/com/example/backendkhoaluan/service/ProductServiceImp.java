package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.ProductsDTO;
import com.example.backendkhoaluan.entities.Products;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.exception.UpdateException;
import com.example.backendkhoaluan.payload.request.ProductRequest;
import com.example.backendkhoaluan.repository.CustomProductQuery;
import com.example.backendkhoaluan.repository.ProductsRepository;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    private ModelMapper modelMapper=new ModelMapper();

    @Override
    @Transactional
    public ProductsDTO getById(int id) {
        Optional<Products> products = productsRepository.findById(id);
        if (!products.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageProductValidation.NOT_FIND_PRODUCT_BY_ID + id);
        }
        Products data = products.get();

        ProductsDTO dto=modelMapper.map(data,ProductsDTO.class);
        dto.setImage(cloudinaryService.getImageUrl(data.getImage()));

        return dto;
    }

    @Override
    public Page<Products> getListProduct(CustomProductQuery.ProductFilterParam param, PageRequest pageRequest) {
        Specification<Products> specification = CustomProductQuery.getFilterProduct(param);
        return productsRepository.findAll(specification, pageRequest);
    }

    @Override
    @Transactional
    public void updateProduct(int id, ProductRequest request, MultipartFile file) {
        try {
            Optional<Products> productOptional = productsRepository.findById(id);
            if (!productOptional.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageProductValidation.NOT_FIND_PRODUCT_BY_ID + id);
            }
            Products products = productOptional.get();
            products.setDescription(request.getDescription());
            products.setLink(request.getLink());
            products.setIntroduction(request.getIntroduction());
            products.setCreateDate(new Date());
            products.setTitle(request.getTitle());

            if (file != null) {
                String fileName = cloudinaryService.uploadFile(file);
                products.setImage(fileName);
            }

            productsRepository.save(products);
        } catch (Exception e) {
            throw new UpdateException("Cập nhật sản phẩm thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        try {
            Optional<Products> productsOptional = productsRepository.findById(id);
            if (!productsOptional.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageProductValidation.NOT_FIND_PRODUCT_BY_ID + id);
            }
            productsRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeleteException("Xóa sản phẩm thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void createProduct(ProductRequest request,MultipartFile file) {
        try {
            Products products = modelMapper.map(request,Products.class);
            String fileName=cloudinaryService.uploadFile(file);
            products.setImage(fileName);
            products.setCreateDate(new Date());

            productsRepository.save(products);
        } catch (Exception e) {
            throw new InsertException("Thêm sản phẩm thất bại", e.getLocalizedMessage());
        }
    }
}
