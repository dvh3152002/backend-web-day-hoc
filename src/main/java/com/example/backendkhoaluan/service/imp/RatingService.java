package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.RatingCourseDTO;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.payload.request.CreateRatingRequest;
import com.example.backendkhoaluan.repository.CustomRatingCourseQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RatingService {
    Page<RatingCourse> getAllRating(CustomRatingCourseQuery.RatingCourseFilterParam param, PageRequest pageRequest);

    String deleteRating(int id);

    void createRating(CreateRatingRequest request);

    void updateRating(int id, CreateRatingRequest request);

    RatingCourseDTO getRating(int idCourse, int idUser);
}
