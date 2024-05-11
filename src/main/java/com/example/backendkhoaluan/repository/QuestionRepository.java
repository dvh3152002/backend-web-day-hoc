package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionRepository extends JpaRepository<Questions,Integer>, JpaSpecificationExecutor<Questions> {
}
