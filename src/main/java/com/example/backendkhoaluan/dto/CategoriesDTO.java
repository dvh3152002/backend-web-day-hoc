package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoriesDTO {
    private int id;
    private String name;

    private List<PostsDTO> posts;
}
