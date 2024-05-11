package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AnswerDTO {
    private Integer id;
    private String body;
    private Date createDate;
    private String userName;
}
