package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProductsDTO {
    private Integer id;
    private String title;
    private String introduction;
    private String link;
    private String description;
    private String image;
    private Date createDate;
}
