package com.example.backendkhoaluan.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private int id;

    private String name;

    private double price;

    private int discount;

    private String description;

    private String image;

    private String teacher;

    private double rating;
}
