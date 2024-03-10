package com.example.backendkhoaluan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "address")
    private String address;

    @Column(name = "avatar")
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<RatingCourse> listRatingCourses;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Orders> listOrders;
}
