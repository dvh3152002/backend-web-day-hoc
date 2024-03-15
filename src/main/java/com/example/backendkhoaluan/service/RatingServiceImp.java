package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.repository.CustomeRatingCourseQuery;
import com.example.backendkhoaluan.repository.RatingCourseRepository;
import com.example.backendkhoaluan.service.imp.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

@Service
public class RatingServiceImp implements RatingService {
    @Autowired
    private RatingCourseRepository ratingCourseRepository;
    @Override
    public Page<RatingCourse> getAllRating(CustomeRatingCourseQuery.RatingCourseFilterParam param,
                                           PageRequest pageRequest) {
        Specification<RatingCourse> specification = CustomeRatingCourseQuery.getFilterRating(param);
        return ratingCourseRepository.findAll(specification,pageRequest);
    }
}
