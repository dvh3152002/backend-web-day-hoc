package com.example.backendkhoaluan.payload.request;

import lombok.Data;

@Data
public class CreateRatingRequest {
    private String content;
    private Float ratePoint;
    private Integer idCourse;
    private Integer idUser;
}
