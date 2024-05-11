package com.example.backendkhoaluan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
public class Questions {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "tags")
    private String tags;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "id_user",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "question",orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Answers> listAnswers;
}
