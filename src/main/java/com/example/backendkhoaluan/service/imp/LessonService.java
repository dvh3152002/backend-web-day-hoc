package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.LessonsDTO;
import com.example.backendkhoaluan.entities.Lessons;
import com.example.backendkhoaluan.payload.request.LessonRequest;
import com.example.backendkhoaluan.repository.CustomLessonQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LessonService {
    Page<Lessons> getListLesson(CustomLessonQuery.LessonFilterParam param, PageRequest pageRequest);
    LessonsDTO getLessonsById(int id);
    void save(LessonRequest request, MultipartFile video);
    void updateLesson(int id, LessonRequest request, MultipartFile video);
    void deleteLesson(int id);
    void deleteAll(List<Lessons> list);
}
