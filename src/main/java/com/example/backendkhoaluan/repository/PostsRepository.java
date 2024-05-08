package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostsRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {
}