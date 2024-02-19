package com.example.backendkhoaluan.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity(name = "order")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @OneToMany(mappedBy = "order")
    private Set<OrderDetailEntity> orderDetailList;
}
