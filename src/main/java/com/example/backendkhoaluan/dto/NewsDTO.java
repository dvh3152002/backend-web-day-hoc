package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class NewsDTO {
    private Integer id;
    private String title;
    private String description;
    private String image;
    private Date createDate;
}
