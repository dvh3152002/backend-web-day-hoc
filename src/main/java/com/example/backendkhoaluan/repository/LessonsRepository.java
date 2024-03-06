package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LessonsRepository extends JpaRepository<Lesson, Integer>, JpaSpecificationExecutor<Lesson> {

}