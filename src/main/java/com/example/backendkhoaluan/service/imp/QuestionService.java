package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.QuestionDTO;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.payload.request.QuestionRequest;
import com.example.backendkhoaluan.repository.CustomeQuestionQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface QuestionService {
    void addQuestion(QuestionRequest request);

    QuestionDTO getById(int id);
    Page<Questions> getListQuestion(CustomeQuestionQuery.QuestionFilterParam param, PageRequest pageRequest);
}
