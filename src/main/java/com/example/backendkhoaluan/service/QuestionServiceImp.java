package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.QuestionDTO;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.entities.Tag;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.ErrorDetailException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.payload.request.QuestionRequest;
import com.example.backendkhoaluan.payload.response.ErrorDetail;
import com.example.backendkhoaluan.repository.CustomQuestionQuery;
import com.example.backendkhoaluan.repository.QuestionRepository;
import com.example.backendkhoaluan.repository.TagRepository;
import com.example.backendkhoaluan.service.imp.QuestionService;
import com.example.backendkhoaluan.utils.SlugUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
public class QuestionServiceImp implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagRepository tagRepository;

    private ModelMapper modelMapper=new ModelMapper();

    @Override
    @Transactional
    public void addQuestion(QuestionRequest request) {
        log.info("request question: {}",request);
        try {
            Questions questions=modelMapper.map(request,Questions.class);
            setTagQuestion(questions,request.getTags());
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
        questionDTO.setUserName(questions.getUser().getFullname());

        return questionDTO;
    }

    @Override
    public Page<Questions> getListQuestion(CustomQuestionQuery.QuestionFilterParam param, PageRequest pageRequest) {
        Specification<Questions> specification= CustomQuestionQuery.getFilterQuestion(param);
        return questionRepository.findAll(specification,pageRequest);
    }

    private void setTagQuestion(Questions questions, Set<String> idTag) {
        if (!CollectionUtils.isEmpty(idTag)) {
            List<Tag> tags=new ArrayList<>();
            for(String name:idTag){
                Tag tag=new Tag();
                tag.setId(SlugUtils.toSlug(name));
                tag.setName(name);

                tag=tagRepository.save(tag);

                tags.add(tag);
            }
            questions.setTags(tags);
        }
    }
}
