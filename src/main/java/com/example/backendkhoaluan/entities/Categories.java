package com.example.backendkhoaluan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class Categories {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category",orphanRemoval = true)
    private List<Courses> listCourses;

    @JsonIgnore
    @OneToMany(mappedBy = "category",orphanRemoval = true)
    private List<Post> listPosts;
}
