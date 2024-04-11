package com.example.backendkhoaluan.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrdersDTO {
    private Integer id;
    private Date createDate;
    private Integer totalCost;
    private String vnpBankCode;
    private Integer status = 0;

    private List<OrderDetailDTO> orderDetails;
}
