package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductsRepository extends JpaRepository<Products, Integer>, JpaSpecificationExecutor<Products> {

}