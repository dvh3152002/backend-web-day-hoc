package com.example.backendkhoaluan.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity(name = "courses")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "discount")
    private int discount;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @OneToMany(mappedBy = "course")
    private Set<LessonEntity> lessionList;

    @OneToMany(mappedBy = "course")
    private Set<RatingCourseEntity> ratingCourseList;

    @OneToMany(mappedBy = "course")
    private Set<OrderDetailEntity> orderDetailList;
}
