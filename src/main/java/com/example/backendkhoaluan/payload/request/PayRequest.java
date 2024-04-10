package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.dto.OrderDetailDTO;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
public class PayRequest {
    private Integer idUser;
    private Integer idCourse;
    private Integer totalCost;
    private String vnpBankCode;
    private Set<OrderDetailDTO> orderItem;
}
