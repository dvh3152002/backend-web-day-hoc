package com.example.backendkhoaluan.dto;

import com.example.backendkhoaluan.entities.Codes;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostsDTO {
    private Integer id;
    private String title;
    private String description;
    private Date createDate;
    private int countCode;
    private List<Codes> listCodes;
}
