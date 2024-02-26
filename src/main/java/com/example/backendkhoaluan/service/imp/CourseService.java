package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.CourseDTO;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourse(int page, int size);
}
