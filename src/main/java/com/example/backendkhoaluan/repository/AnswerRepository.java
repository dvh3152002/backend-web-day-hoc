package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Answers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnswerRepository extends JpaRepository<Answers,Integer>, JpaSpecificationExecutor<Answers> {
}
