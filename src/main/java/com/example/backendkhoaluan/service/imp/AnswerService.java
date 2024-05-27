package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.AnswerDTO;
import com.example.backendkhoaluan.entities.Answers;
import com.example.backendkhoaluan.payload.request.AnswerRequest;
import com.example.backendkhoaluan.payload.request.VoteAnswerRequest;
import com.example.backendkhoaluan.repository.CustomAnswerQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AnswerService {
    void addAnswer(AnswerRequest request);

    AnswerDTO getById(int id);
    Page<Answers> getListAnswer(CustomAnswerQuery.AnswerFilterParam param, PageRequest pageRequest);

    void voteAnswer(VoteAnswerRequest request);
}
