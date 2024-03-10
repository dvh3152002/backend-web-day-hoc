package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.PostsDTO;
import com.example.backendkhoaluan.entities.Post;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.UpdateException;
import com.example.backendkhoaluan.payload.request.PostRequest;
import com.example.backendkhoaluan.repository.CodesRepository;
import com.example.backendkhoaluan.repository.CustomePostQuery;
import com.example.backendkhoaluan.repository.PostsRepository;
import com.example.backendkhoaluan.service.imp.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        Optional<Post> post = postsRepository.findById(id);
        if (!post.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessagePostValidation.NOT_FIND_POST_BY_ID + id);
        }
        PostsDTO postDTO = new PostsDTO();

        Post data = post.get();
        postDTO.setDescription(data.getDescription());
        postDTO.setCreateDate(data.getCreateDate());
        postDTO.setListCodes(data.getListCodes());
        postDTO.setId(data.getId());
        postDTO.setTitle(data.getTitle());
        return postDTO;
    }

    @Override
    public Page<Post> getListPost(CustomePostQuery.PostFilterParam param, PageRequest pageRequest) {
        Specification<Post> specification = CustomePostQuery.getFilterPost(param);
        return postsRepository.findAll(specification, pageRequest);
    }

    @Override
    @Transactional
    public void updatePost(int id, PostRequest request) {
        try {
            Optional<Post> postOptional = postsRepository.findById(id);
            if (!postOptional.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessagePostValidation.NOT_FIND_POST_BY_ID + id);
            }
            Post post = postOptional.get();
            post.setDescription(request.getDescription());
            post.setCreateDate(new Date());
            post.setTitle(request.getTitle());

            postsRepository.save(post);
        } catch (Exception e) {
            throw new UpdateException("Cập nhật bài viết thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        try {
            Optional<Post> postOptional = postsRepository.findById(id);
            if (!postOptional.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessagePostValidation.NOT_FIND_POST_BY_ID + id);
            }
            Post post = postOptional.get();
            codesRepository.deleteAll(post.getListCodes());
            postsRepository.delete(post);
        } catch (Exception e) {
            throw new DeleteException("Xóa bài viết thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void createPost(PostRequest request) {
        try {
            Post post = new Post();

            post.setDescription(request.getDescription());
            post.setCreateDate(new Date());
            post.setTitle(request.getTitle());

            postsRepository.save(post);
        } catch (Exception e) {
            throw new UpdateException("Thêm bài viết thất bại", e.getLocalizedMessage());
        }
    }
}
