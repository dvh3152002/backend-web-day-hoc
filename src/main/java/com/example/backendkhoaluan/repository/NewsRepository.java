package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NewsRepository extends JpaRepository<News, Integer>, JpaSpecificationExecutor<News> {

}