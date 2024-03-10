package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.PostsDTO;
import com.example.backendkhoaluan.entities.Post;
import com.example.backendkhoaluan.payload.request.PostRequest;
import com.example.backendkhoaluan.repository.CustomePostQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PostService {
    PostsDTO getById(int id);
    Page<Post> getListPost(CustomePostQuery.PostFilterParam param, PageRequest pageRequest);
    void updatePost(int id, PostRequest request);

    void deleteById(int id);

    void createPost(PostRequest request);
}
