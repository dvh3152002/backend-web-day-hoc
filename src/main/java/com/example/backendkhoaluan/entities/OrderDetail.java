package com.example.backendkhoaluan.entities;

import com.example.backendkhoaluan.entities.keys.KeyOrderDetail;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "order_detail")
public class OrderDetail{
    @EmbeddedId
    private KeyOrderDetail keyOrderDetail;

    @Column(name = "price")
    private Double price;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "id_order")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private Courses course;
}
