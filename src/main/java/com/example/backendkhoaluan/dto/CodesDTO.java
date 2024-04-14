package com.example.backendkhoaluan.dto;

import lombok.Data;

@Data
public class CodesDTO {
    private Integer id;
    private String title;
    private String description;
    private String code;
    private String language;
    private Integer idPost;
}
