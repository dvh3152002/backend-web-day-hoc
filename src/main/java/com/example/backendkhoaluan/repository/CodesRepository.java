package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Code;
import com.example.backendkhoaluan.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CodesRepository extends JpaRepository<Code, Integer>, JpaSpecificationExecutor<Code> {
    List<Code> findByPost(Post post);
}