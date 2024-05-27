package com.example.backendkhoaluan.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "products")
public class Products {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "link")
    private String link;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "create_date")
    private Date createDate;
}
