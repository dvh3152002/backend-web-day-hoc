package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer>, JpaSpecificationExecutor<Orders> {
    List<Orders> findByUser(User user);

    @Query("SELECT MONTH(o.createDate) AS month, YEAR(o.createDate) AS year, SUM(o.totalCost) AS totalCost " +
            "FROM Orders o WHERE YEAR(o.createDate) = :year GROUP BY YEAR(o.createDate), MONTH(o.createDate)")
    List<Object[]> getTotalCostByMonthInYear(int year);
}