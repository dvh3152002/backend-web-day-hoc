package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.AnswerDTO;
import com.example.backendkhoaluan.dto.QuestionDTO;
import com.example.backendkhoaluan.entities.Answers;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.payload.request.AnswerRequest;
import com.example.backendkhoaluan.payload.request.QuestionRequest;
import com.example.backendkhoaluan.repository.AnswerRepository;
import com.example.backendkhoaluan.repository.CustomeAnswerQuery;
import com.example.backendkhoaluan.repository.CustomeQuestionQuery;
import com.example.backendkhoaluan.repository.QuestionRepository;
import com.example.backendkhoaluan.service.imp.AnswerService;
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
import java.util.Optional;

@Service
@Slf4j
public class AnswerServiceImp implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    @Transactional
    public void addAnswer(AnswerRequest request) {
        log.info("request: {}", request);
        try {
            Optional<Questions> questions = questionRepository.findById(request.getIdQuestion());
            if (!questions.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageQuestionValidation.NOT_FIND_QUESTION_BY_ID + request.getIdQuestion());
            }
            Answers answers = new Answers();
            answers.setBody(request.getBody());
            User user=new User();
            user.setId(request.getIdUser());
            answers.setUser(user);
            answers.setQuestion(questions.get());
            answers.setCreateDate(new Date());
            answerRepository.save(answers);
        } catch (Exception e) {
            throw new InsertException("Lỗi thêm câu trả lời", e.getLocalizedMessage());
        }
    }

    @Override
    public AnswerDTO getById(int id) {
        return null;
    }

    @Override
    public Page<Answers> getListAnswer(CustomeAnswerQuery.AnswerFilterParam param, PageRequest pageRequest) {
        Specification<Answers> specification=CustomeAnswerQuery.getFilterAnswer(param);
        return answerRepository.findAll(specification,pageRequest);
    }
}
