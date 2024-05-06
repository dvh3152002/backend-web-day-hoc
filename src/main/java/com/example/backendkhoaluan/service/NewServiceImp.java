package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.NewsDTO;
import com.example.backendkhoaluan.entities.News;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.UpdateException;
import com.example.backendkhoaluan.payload.request.NewRequest;
import com.example.backendkhoaluan.repository.*;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.NewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Service
public class NewServiceImp implements NewService {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    private ModelMapper modelMapper=new ModelMapper();

    @Override
    @Transactional
    public NewsDTO getById(int id) {
        Optional<News> news = newsRepository.findById(id);
        if (!news.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageNewValidation.NOT_FIND_NEW_BY_ID + id);
        }
        News data = news.get();

        NewsDTO dto=modelMapper.map(data,NewsDTO.class);
        dto.setImage(cloudinaryService.getImageUrl(data.getImage()));

        return dto;
    }

    @Override
    public Page<News> getListNew(CustomeNewQuery.NewFilterParam param, PageRequest pageRequest) {
        Specification<News> specification = CustomeNewQuery.getFilterNew(param);
        return newsRepository.findAll(specification, pageRequest);
    }

    @Override
    @Transactional
    public void updateNew(int id, NewRequest request, MultipartFile file) {
        try {
            Optional<News> newOptional = newsRepository.findById(id);
            if (!newOptional.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageNewValidation.NOT_FIND_NEW_BY_ID + id);
            }
            News news = newOptional.get();
            news.setDescription(request.getDescription());
            news.setCreateDate(new Date());
            news.setTitle(request.getTitle());

            if (file != null) {
                String fileName = cloudinaryService.uploadFile(file);
                news.setImage(fileName);
            }

            newsRepository.save(news);
        } catch (Exception e) {
            throw new UpdateException("Cập nhật bài viết thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        try {
            Optional<News> newsOptional = newsRepository.findById(id);
            if (!newsOptional.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageNewValidation.NOT_FIND_NEW_BY_ID + id);
            }
            News news = newsOptional.get();
            newsRepository.delete(news);
        } catch (Exception e) {
            throw new DeleteException("Xóa tin tức thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void createNew(NewRequest request,MultipartFile file) {
        try {
            News news = modelMapper.map(request,News.class);
            String fileName=cloudinaryService.uploadFile(file);
            news.setImage(fileName);
            news.setCreateDate(new Date());

            newsRepository.save(news);
        } catch (Exception e) {
            throw new UpdateException("Thêm bài viết thất bại", e.getLocalizedMessage());
        }
    }
}
