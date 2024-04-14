package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CoursesDTO {
    private Integer id;
    private String name;
    private String slug;
    private Integer price;
    private Integer discount;
    private String description;
    private String image;
    private Double rating;
    private UsersDTO teacher;
    private float limitTime;
    private Date createDate;
    private int count;
    private boolean free;

    private CategoriesDTO category;

    private Integer idStart;

    private List<LessonsDTO> lessons;
}
