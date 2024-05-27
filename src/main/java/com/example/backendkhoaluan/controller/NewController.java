package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.CategoriesDTO;
import com.example.backendkhoaluan.dto.NewsDTO;
import com.example.backendkhoaluan.dto.PostsDTO;
import com.example.backendkhoaluan.entities.News;
import com.example.backendkhoaluan.entities.Post;
import com.example.backendkhoaluan.payload.request.GetNewRequest;
import com.example.backendkhoaluan.payload.request.GetPostRequest;
import com.example.backendkhoaluan.payload.request.NewRequest;
import com.example.backendkhoaluan.payload.request.PostRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.NewService;
import com.example.backendkhoaluan.service.imp.PostService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/new")
public class NewController {
    @Autowired
    private NewService newService;

    @Autowired
    private CloudinaryService cloudinaryService;

    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return BaseResponse.success(newService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> createNew(@Valid @ModelAttribute NewRequest request, @RequestParam MultipartFile file) {
        newService.createNew(request,file);
        return BaseResponse.success("Thêm tin tức thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        newService.deleteById(id);
        return BaseResponse.success("Xóa tin tức thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNew(@PathVariable int id, @Valid @ModelAttribute NewRequest request,
                                  @RequestParam(required = false) MultipartFile file) {
        newService.updateNew(id, request,file);
        return BaseResponse.success("Cập nhật bài viết thành công");
    }

    @GetMapping("")
    public ResponseEntity<?> getListPost(GetNewRequest request) {
        Page<News> page = newService.getListNew(request, PageRequest.of(request.getStart(), request.getLimit()));
        return BaseResponse.successListData(
                page.getContent().stream().map(data -> {
                    NewsDTO newsDTO = modelMapper.map(data,NewsDTO.class);
                    newsDTO.setImage(cloudinaryService.getImageUrl(data.getImage()));
                    return newsDTO;
                }).collect(Collectors.toList()), (int) page.getTotalElements());
    }
}
