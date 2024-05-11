package com.example.backendkhoaluan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "active")
    private boolean active;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_generated_time")
    private LocalDateTime otpGeneratedTime;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"))
    private List<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<RatingCourse> listRatingCourses;

    @JsonIgnore
    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private List<Orders> listOrders;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher",orphanRemoval = true)
    private List<Courses> listCourses;

    @JsonIgnore
    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private List<Questions> listQuestions;
}
