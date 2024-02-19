package com.example.backendkhoaluan.entity;

import com.example.backendkhoaluan.entity.keys.KeyOrderDetail;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "order_detail")
public class OrderDetailEntity {
    @EmbeddedId
    private KeyOrderDetail keyOrderDetail;

    @Column(name = "price")
    private double price;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "id_order",insertable = false,updatable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "id_course",insertable = false,updatable = false)
    private CourseEntity course;
}
