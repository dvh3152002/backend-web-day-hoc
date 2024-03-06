package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer>, JpaSpecificationExecutor<Orders> {
    List<Orders> findByUser(User user);
}