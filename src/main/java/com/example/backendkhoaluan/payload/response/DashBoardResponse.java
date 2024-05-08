package com.example.backendkhoaluan.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class DashBoardResponse {
    private int countCourse;
    private int countUser;
    private int countNew;
    private int countOrder;
    private int totalCost;
    private List<MonthlySaleResponse> listSale;
}
