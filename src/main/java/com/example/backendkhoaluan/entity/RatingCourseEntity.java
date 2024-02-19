package com.example.backendkhoaluan.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "rating_course")
public class RatingCourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "rate_point")
    private float ratePoint;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private CourseEntity course;
}
