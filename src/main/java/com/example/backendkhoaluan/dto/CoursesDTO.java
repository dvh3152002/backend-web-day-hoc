package com.example.backendkhoaluan.dto;

import lombok.Data;

@Data
public class CoursesDTO {
    private Integer id;
    private String name;
    private Double price;
    private Integer discount;
    private String description;
    private String image;
    private Double rating;
    private Integer idUser;
    private String categoryName;
}
