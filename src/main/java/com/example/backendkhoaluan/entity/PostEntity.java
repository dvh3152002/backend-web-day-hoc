package com.example.backendkhoaluan.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="create_date")
    private Date createDate;

    @OneToMany(mappedBy = "post")
    private Set<CodeEntity> listCode;
}
