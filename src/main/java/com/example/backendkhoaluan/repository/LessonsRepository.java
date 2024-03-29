package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Lessons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LessonsRepository extends JpaRepository<Lessons, Integer>, JpaSpecificationExecutor<Lessons> {

}