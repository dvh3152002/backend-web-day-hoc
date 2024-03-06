package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.PostsDTO;
import com.example.backendkhoaluan.payload.response.DataResponse;
import com.example.backendkhoaluan.service.imp.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        PostsDTO postDTO=postService.getById(id);

        DataResponse dataResponse =new DataResponse();
        dataResponse.setData(postDTO);

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
