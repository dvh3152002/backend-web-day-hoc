package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LessonsDTO {
    private Integer id;
    private String title;
    private String video;
    private Date createDate;
}
