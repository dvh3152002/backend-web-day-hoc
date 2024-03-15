package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;

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
    private UsersDTO user;
    private Date createDate;

    private String categoryName;
}
