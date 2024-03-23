package com.example.backendkhoaluan.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@Table(name = "rating_course")
public class RatingCourse {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "rate_point")
    private Float ratePoint;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private Courses course;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
