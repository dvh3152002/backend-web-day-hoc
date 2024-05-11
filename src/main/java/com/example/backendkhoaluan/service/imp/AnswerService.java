package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.AnswerDTO;
import com.example.backendkhoaluan.dto.QuestionDTO;
import com.example.backendkhoaluan.entities.Answers;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.payload.request.AnswerRequest;
import com.example.backendkhoaluan.payload.request.QuestionRequest;
import com.example.backendkhoaluan.repository.CustomeAnswerQuery;
import com.example.backendkhoaluan.repository.CustomeQuestionQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AnswerService {
    void addAnswer(AnswerRequest request);

    AnswerDTO getById(int id);
    Page<Answers> getListAnswer(CustomeAnswerQuery.AnswerFilterParam param, PageRequest pageRequest);
}
