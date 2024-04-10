package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDetailDTO {
    private Integer idOrder;
    private Integer idCourse;
    private Integer price;
    private Date createDate;
}
