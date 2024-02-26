package com.example.backendkhoaluan.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "address")
    private String address;

    @Column(name="avatar")
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private RoleEntity role;

    @OneToMany(mappedBy = "user")
    private Set<CourseEntity> courseList;

    @OneToMany(mappedBy = "user")
    private Set<RatingCourseEntity> ratingCourseList;

    @OneToMany(mappedBy = "user")
    private Set<OrderEntity> orderList;
}
