package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.dto.CourseDTO;
import com.example.backendkhoaluan.entity.CourseEntity;
import com.example.backendkhoaluan.entity.RatingCourseEntity;
import com.example.backendkhoaluan.repository.CourseRepository;
import com.example.backendkhoaluan.service.imp.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CourseServiceImp implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<CourseDTO> getAllCourse(int page, int size) {
        List<CourseDTO> list=new ArrayList<>();
        PageRequest pageRequest=PageRequest.of(page, size);
        Page<CourseEntity> listData= courseRepository.findAll(pageRequest);
        for (CourseEntity courseEntity:listData) {
            CourseDTO courseDTO=new CourseDTO();
            courseDTO.setId(courseEntity.getId());
            courseDTO.setDescription(courseEntity.getDescription());
            courseDTO.setName(courseEntity.getName());
            courseDTO.setPrice(courseEntity.getPrice());
            courseDTO.setDiscount(courseEntity.getDiscount());
            courseDTO.setImage(courseDTO.getImage());
            courseDTO.setTeacher(courseEntity.getUser().getFullname());
            courseDTO.setRating(calculatorRating(courseEntity.getRatingCourseList()));

            list.add(courseDTO);
        }
        return list;
    }

    private double calculatorRating(Set<RatingCourseEntity> listRating){
        double totalPoint=0;
        for (RatingCourseEntity data:listRating) {
            totalPoint=data.getRatePoint();
        }
        return totalPoint/listRating.size();
    }
}
