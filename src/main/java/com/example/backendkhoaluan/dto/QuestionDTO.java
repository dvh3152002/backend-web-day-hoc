package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String body;
    private List<TagDTO> tags;
    private Date createDate;
    private Integer countAnswer;
    private String userName;
}
