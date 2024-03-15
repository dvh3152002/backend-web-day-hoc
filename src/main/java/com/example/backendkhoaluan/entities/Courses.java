package com.example.backendkhoaluan.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "courses")
public class Courses {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "price")
    private Integer price;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Categories category;

    @OneToMany(mappedBy = "course",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<RatingCourse> listRatingCourses;

    @OneToMany(mappedBy = "course")
    private List<OrderDetail> listOrderDetails;

    @OneToMany(mappedBy = "course")
    private List<Lessons> listLessons;
}
