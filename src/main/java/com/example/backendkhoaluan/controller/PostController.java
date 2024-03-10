package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.PostsDTO;
import com.example.backendkhoaluan.entities.Post;
import com.example.backendkhoaluan.payload.request.GetPostRequest;
import com.example.backendkhoaluan.payload.request.PostRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.PostService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    private ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable int id) {
        return BaseResponse.success(postService.getById(id));
    }

    @PostMapping("/insert")
    public BaseResponse createPost( @Valid @RequestBody PostRequest request) {
        postService.createPost(request);
        return BaseResponse.success("Cập nhật bài viết thành công");
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable int id) {
        postService.deleteById(id);
        return BaseResponse.success("Xóa bài viết thành công");
    }

    @PutMapping("/{id}")
    public BaseResponse updatePost(@PathVariable int id, @Valid @RequestBody PostRequest request) {
        postService.updatePost(id, request);
        return BaseResponse.success("Cập nhật bài viết thành công");
    }

    @GetMapping("/getList")
    public BaseResponse getListPost(GetPostRequest request) {
        Page<Post> page = postService.getListPost(request, PageRequest.of(request.getStart(), request.getLimit()));
        return BaseResponse.successListData(
                page.getContent().stream().map(data -> {
                    PostsDTO postsDTO = modelMapper.map(data, PostsDTO.class);
                    return postsDTO;
                }).collect(Collectors.toList()), (int) page.getTotalElements());
    }
}
