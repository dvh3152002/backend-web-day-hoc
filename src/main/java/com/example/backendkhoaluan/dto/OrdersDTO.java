package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrdersDTO {
    private Integer id;
    private Integer idUser;
    private Date createDate;
}
