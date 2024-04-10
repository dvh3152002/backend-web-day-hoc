package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.payload.request.PayRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {
    void deleteOrder(int id);
    void deleteOrder(Orders orders);

    String createOrder(PayRequest request);

    void updateOrder(Map<String, String> queryParams);

    List<Orders> findByUser(User user);
}
