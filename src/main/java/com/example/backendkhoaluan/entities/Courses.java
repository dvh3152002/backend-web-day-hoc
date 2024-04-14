package com.example.backendkhoaluan.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

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

    @Column(name = "free")
    private boolean free;

    @Column(name = "limit_time")
    private Float limitTime;

    @ManyToOne
    @JoinColumn(name = "id_teacher")
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Categories category;

    @OneToMany(mappedBy = "course",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<RatingCourse> listRatingCourses;

    @OneToMany(mappedBy = "course")
    private List<OrderDetail> listOrderDetails;

    @OneToMany(mappedBy = "course",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Lessons> listLessons;

    @OneToMany(mappedBy = "course",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<CourseDetail> listCourseDetail;
}
