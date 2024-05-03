package com.example.backendkhoaluan.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySaleResponse {
    private double totalCost;
    private String month;
}
