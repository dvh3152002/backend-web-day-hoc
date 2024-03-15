package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RatingCourseDTO {
    private Integer id;
    private String content;
    private Float ratePoint;
    private UsersDTO user;
    private Date createDate;
}
