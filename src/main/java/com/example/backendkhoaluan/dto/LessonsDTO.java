package com.example.backendkhoaluan.dto;

import lombok.Data;

@Data
public class LessonsDTO {
    private Integer id;
    private String title;
    private String video;
    private Integer idCourse;
}
