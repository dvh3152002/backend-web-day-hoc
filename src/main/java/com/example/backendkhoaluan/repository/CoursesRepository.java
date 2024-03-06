package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CoursesRepository extends JpaRepository<Courses, Integer>, JpaSpecificationExecutor<Courses> {

}