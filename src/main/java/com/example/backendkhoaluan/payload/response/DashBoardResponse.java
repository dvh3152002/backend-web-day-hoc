package com.example.backendkhoaluan.payload.response;

import lombok.Data;

@Data
public class DashBoardResponse {
    private int countCourse;
    private int countUser;
    private int countPost;
    private int countOrder;
    private int totalCost;
}
