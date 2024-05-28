package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TagRepository extends JpaRepository<Tag, String>, JpaSpecificationExecutor<Tag> {

}