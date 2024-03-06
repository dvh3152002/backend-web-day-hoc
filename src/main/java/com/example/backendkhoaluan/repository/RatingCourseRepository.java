package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingCourseRepository extends JpaRepository<RatingCourse, Integer> {
    List<RatingCourse> findByCourse(Courses course);
    List<RatingCourse> findByUser(User user);
}
