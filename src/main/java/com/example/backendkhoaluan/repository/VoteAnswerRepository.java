package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.VoteAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VoteAnswerRepository extends JpaRepository<VoteAnswer,Integer>, JpaSpecificationExecutor<VoteAnswer> {
}
