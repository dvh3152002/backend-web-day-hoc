package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.RatingCourseDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.payload.request.GetRatingCourseRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    private ModelMapper modelMapper=new ModelMapper();

    @GetMapping("")
    public BaseResponse getListRating(GetRatingCourseRequest request){
        log.info("request: {}",request);
        Page<RatingCourse> page=ratingService.getAllRating(request, PageRequest.of(request.getStart(),request.getLimit()));
        return BaseResponse.successListData(page.stream().map(e->{
            RatingCourseDTO rating=modelMapper.map(e,RatingCourseDTO.class);
            rating.setUser(modelMapper.map(e.getUser(), UsersDTO.class));
            return rating;
        }).toList(),(int) page.getTotalElements());
    }
}
