package com.example.backendkhoaluan.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lessons")
public class Lessons {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "video")
    private String video;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_course")
    private Courses course;
}
