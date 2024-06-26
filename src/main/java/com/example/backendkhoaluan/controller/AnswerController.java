package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.AnswerDTO;
import com.example.backendkhoaluan.dto.QuestionDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Answers;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.payload.request.*;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.AnswerService;
import com.example.backendkhoaluan.service.imp.QuestionService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addAnswer(@Valid @RequestBody AnswerRequest request,
                                       @RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        request.setIdUser(user.getId());
        answerService.addAnswer(request);
        return BaseResponse.success("Thêm câu trả lời thành công");
    }

    @GetMapping("")
    public ResponseEntity<?> getAllAnswer(GetAnswerRequest request){
        Page<Answers> page=answerService.getListAnswer(request, PageRequest.of(request.getStart(), request.getLimit()));
        return BaseResponse.successListData(page.getContent()
                .stream().map(data->{
                    AnswerDTO answerDTO= modelMapper.map(data, AnswerDTO.class);
                    answerDTO.setUserName(data.getUser().getFullname());
                    return answerDTO;
                }).collect(Collectors.toList()),(int) page.getTotalElements());
    }

    @PostMapping("/vote")
    public ResponseEntity<?> voteAnswer(@RequestBody VoteAnswerRequest request,
                                   @RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        request.setIdUser(user.getId());
        answerService.voteAnswer(request);
        return BaseResponse.success("Vote trả lời thành công");
    }
}
