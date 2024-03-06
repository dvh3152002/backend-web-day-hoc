package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.OrderDetail;
import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.entities.keys.KeyOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, KeyOrderDetail>, JpaSpecificationExecutor<OrderDetail> {
    List<OrderDetail> findByOrder(Orders orders);
}