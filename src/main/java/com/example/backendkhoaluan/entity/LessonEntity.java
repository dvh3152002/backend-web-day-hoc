package com.example.backendkhoaluan.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "lessons")
public class LessonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "video")
    private String video;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private CourseEntity course;
}
