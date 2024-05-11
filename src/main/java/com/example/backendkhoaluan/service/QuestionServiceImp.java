package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.QuestionDTO;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.payload.request.QuestionRequest;
import com.example.backendkhoaluan.repository.CustomeQuestionQuery;
import com.example.backendkhoaluan.repository.QuestionRepository;
import com.example.backendkhoaluan.service.imp.QuestionService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QuestionServiceImp implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    private ModelMapper modelMapper=new ModelMapper();

    @Override
    @Transactional
    public void addQuestion(QuestionRequest request) {
        log.info("request question: {}",request);
        try {
            Questions questions=modelMapper.map(request,Questions.class);
            questions.setTags(String.join("-", request.getTags()));
            questions.setCreateDate(new Date());
            questionRepository.save(questions);
        }catch (Exception e){
            throw new InsertException("Lỗi thêm câu hỏi",e.getLocalizedMessage());
        }
    }

    @Override
    public QuestionDTO getById(int id) {
        Optional<Questions> questionsOptional=questionRepository.findById(id);
        if(!questionsOptional.isPresent()){
            throw new DataNotFoundException(Constants.ErrorMessageQuestionValidation.NOT_FIND_QUESTION_BY_ID+id);
        }
        Questions questions=questionsOptional.get();
        QuestionDTO questionDTO=modelMapper.map(questions,QuestionDTO.class);
        questionDTO.setCountAnswer(questions.getListAnswers().size());
        questionDTO.setTags(List.of(questions.getTags().split("-")));
        questionDTO.setUserName(questions.getUser().getFullname());

        return questionDTO;
    }

    @Override
    public Page<Questions> getListQuestion(CustomeQuestionQuery.QuestionFilterParam param, PageRequest pageRequest) {
        Specification<Questions> specification=CustomeQuestionQuery.getFilterQuestion(param);
        return questionRepository.findAll(specification,pageRequest);
    }
}
