package com.example.backendkhoaluan.entity;

import jakarta.persistence.*;

@Entity(name = "codes")
public class CodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "id_post")
    private PostEntity post;
}
