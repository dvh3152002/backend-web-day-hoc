package com.example.backendkhoaluan.dto;

import lombok.Data;

@Data
public class RatingCourseDTO {
    private Integer id;
    private Integer idUser;
    private String content;
    private Float ratePoint;
    private Integer idCourse;
}
