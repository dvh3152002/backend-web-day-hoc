package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Codes;
import com.example.backendkhoaluan.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CodesRepository extends JpaRepository<Codes, Integer>, JpaSpecificationExecutor<Codes> {
    List<Codes> findByPost(Post post);
}