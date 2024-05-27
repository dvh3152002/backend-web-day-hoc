package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.NewsDTO;
import com.example.backendkhoaluan.entities.News;
import com.example.backendkhoaluan.payload.request.NewRequest;
import com.example.backendkhoaluan.repository.CustomNewQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface NewService {
    NewsDTO getById(int id);
    Page<News> getListNew(CustomNewQuery.NewFilterParam param, PageRequest pageRequest);
    void updateNew(int id, NewRequest request, MultipartFile file);

    void deleteById(int id);

    void createNew(NewRequest request,MultipartFile file);
}
