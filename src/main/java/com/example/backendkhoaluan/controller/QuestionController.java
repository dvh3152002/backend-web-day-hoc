package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.QuestionDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.payload.request.GetQuestionRequest;
import com.example.backendkhoaluan.payload.request.QuestionRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.QuestionService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/question")
@Slf4j
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private Gson gson=new Gson();

    private ModelMapper modelMapper=new ModelMapper();

    @PostMapping("")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionRequest request,
                                         @RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        request.setUserId(user.getId());
        questionService.addQuestion(request);
        return BaseResponse.success("Thêm câu hỏi thành công");
    }

    @GetMapping("")
    public ResponseEntity<?> getAllQuestion(GetQuestionRequest request){
        log.info("request: {}",request);
        Page<Questions> page=questionService.getListQuestion(request, PageRequest.of(request.getStart(), request.getLimit()));
        return BaseResponse.successListData(page.getContent()
                .stream().map(data->{
                    QuestionDTO questionDTO= modelMapper.map(data, QuestionDTO.class);
                    questionDTO.setUserName(data.getUser().getFullname());
                    return questionDTO;
                }).collect(Collectors.toList()),(int) page.getTotalElements());
    }

    @GetMapping("/auth")
    public ResponseEntity<?> getMyQuestion(GetQuestionRequest request,
                                      @RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        request.setIdUser(user.getId());
        log.info("request: {}",request);
        Page<Questions> page=questionService.getListQuestion(request, PageRequest.of(request.getStart(), request.getLimit()));
        return BaseResponse.successListData(page.getContent()
                .stream().map(data->{
                    QuestionDTO questionDTO= modelMapper.map(data, QuestionDTO.class);
                    questionDTO.setUserName(data.getUser().getFullname());
                    return questionDTO;
                }).collect(Collectors.toList()),(int) page.getTotalElements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable int id){
        QuestionDTO questionDTO=questionService.getById(id);
        return BaseResponse.success(questionDTO);
    }
}
