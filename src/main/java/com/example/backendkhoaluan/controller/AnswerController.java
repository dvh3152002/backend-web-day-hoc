package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.QuestionDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Answers;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.payload.request.AnswerRequest;
import com.example.backendkhoaluan.payload.request.GetAnswerRequest;
import com.example.backendkhoaluan.payload.request.GetQuestionRequest;
import com.example.backendkhoaluan.payload.request.QuestionRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.AnswerService;
import com.example.backendkhoaluan.service.imp.QuestionService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/answer")
@Slf4j
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private Gson gson=new Gson();

    private ModelMapper modelMapper=new ModelMapper();

    @PostMapping("")
    public BaseResponse addAnswer(@RequestBody AnswerRequest request,
                                    @RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        request.setIdUser(user.getId());
        answerService.addAnswer(request);
        return BaseResponse.success("Thêm câu trả lời thành công");
    }

    @GetMapping("")
    public BaseResponse getAllAnswer(GetAnswerRequest request){
        Page<Answers> page=answerService.getListAnswer(request, PageRequest.of(request.getStart(), request.getLimit()));
        return BaseResponse.successListData(page.getContent()
                .stream().map(data->{
                    QuestionDTO questionDTO= modelMapper.map(data, QuestionDTO.class);
                    questionDTO.setUserName(data.getUser().getFullname());
                    return questionDTO;
                }).collect(Collectors.toList()),(int) page.getTotalElements());
    }
}
