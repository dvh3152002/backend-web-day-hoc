package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.entities.User;

import java.util.List;

public interface OrderService {
    void deleteOrder(int id);
    void deleteOrder(Orders orders);
    List<Orders> findByUser(User user);
}
