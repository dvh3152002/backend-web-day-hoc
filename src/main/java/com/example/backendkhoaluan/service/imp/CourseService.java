package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.payload.request.CreateCourseRequest;
import com.example.backendkhoaluan.repository.CustomCourseQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {
    Page<Courses> getAllCourse(CustomCourseQuery.CourseFilterParam param, PageRequest pageRequest);
    void deleteCourse(int id);
    CoursesDTO getCourseById(int id);
    void save(CreateCourseRequest courseResponse, MultipartFile file);
    double calculatorRating(List<RatingCourse> listRating);
}
