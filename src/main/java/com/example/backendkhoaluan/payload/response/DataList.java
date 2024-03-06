package com.example.backendkhoaluan.payload.response;

import lombok.Data;

@Data
public class DataList<T> {
    private Integer total = 0;
    private T items;
}
