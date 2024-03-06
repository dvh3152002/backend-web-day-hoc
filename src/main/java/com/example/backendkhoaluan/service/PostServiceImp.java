package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.dto.PostsDTO;
import com.example.backendkhoaluan.entities.Post;
import com.example.backendkhoaluan.repository.CodesRepository;
import com.example.backendkhoaluan.repository.PostsRepository;
import com.example.backendkhoaluan.service.imp.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostServiceImp implements PostService {
    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CodesRepository codesRepository;

    @Override
    @Transactional
    public PostsDTO getById(int id) {
        Optional<Post> post=postsRepository.findById(id);
        PostsDTO postDTO=new PostsDTO();

        post.ifPresent(data->{
            postDTO.setDescription(data.getDescription());
            postDTO.setCreateDate(data.getCreateDate());
            postDTO.setListCodes(codesRepository.findByPost(data));
            postDTO.setId(data.getId());
            postDTO.setTitle(data.getTitle());
        });
        return postDTO;
    }
}
