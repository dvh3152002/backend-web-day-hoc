package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.repository.CustomeRatingCourseQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RatingService {
    Page<RatingCourse> getAllRating(CustomeRatingCourseQuery.RatingCourseFilterParam param, PageRequest pageRequest);

    String deleteRating(int id);
}
