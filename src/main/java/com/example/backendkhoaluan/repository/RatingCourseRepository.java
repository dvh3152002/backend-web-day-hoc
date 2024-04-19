package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RatingCourseRepository extends JpaRepository<RatingCourse, Integer>, JpaSpecificationExecutor<RatingCourse> {
    List<RatingCourse> findByCourse(Courses course);
    List<RatingCourse> findByUser(User user);

    Optional<RatingCourse> findByUserAndAndCourse(User user, Courses courses);
}
