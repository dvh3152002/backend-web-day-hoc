package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.AnswerDTO;
import com.example.backendkhoaluan.entities.Answers;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.entities.VoteAnswer;
import com.example.backendkhoaluan.enums.VoteType;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.payload.request.AnswerRequest;
import com.example.backendkhoaluan.payload.request.VoteAnswerRequest;
import com.example.backendkhoaluan.repository.*;
import com.example.backendkhoaluan.service.imp.AnswerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class AnswerServiceImp implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private VoteAnswerRepository voteAnswerRepository;

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
            User user = new User();
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
    public Page<Answers> getListAnswer(CustomAnswerQuery.AnswerFilterParam param, PageRequest pageRequest) {
        Specification<Answers> specification = CustomAnswerQuery.getFilterAnswer(param);
        return answerRepository.findAll(specification, pageRequest);
    }

    @Override
    @Transactional
    public void voteAnswer(VoteAnswerRequest request) {
        Optional<Answers> answersOptional = answerRepository.findById(request.getIdAnswer());
        if (!answersOptional.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageAnswerValidation.NOT_FIND_ANSWER_BY_ID + request.getIdAnswer());
        }
        User user = new User();
        user.setId(request.getIdUser());

        Answers answers = answersOptional.get();

        VoteAnswer voteAnswer = new VoteAnswer();
        voteAnswer.setAnswer(answers);
        voteAnswer.setVoteType(request.getVoteType());
        voteAnswer.setUser(user);

        if (request.getVoteType() == VoteType.UPVOTE) {
            answers.setCountVote(answers.getCountVote() + 1);
        } else if (request.getVoteType() == VoteType.DOWNVOTE && answers.getCountVote() > 0) {
            answers.setCountVote(answers.getCountVote() - 1);
        }

        voteAnswerRepository.save(voteAnswer);
        answerRepository.save(answers);
    }
}
