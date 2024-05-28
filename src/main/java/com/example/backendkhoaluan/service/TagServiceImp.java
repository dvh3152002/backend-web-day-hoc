package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.dto.TagDTO;
import com.example.backendkhoaluan.entities.Tag;
import com.example.backendkhoaluan.repository.TagRepository;
import com.example.backendkhoaluan.service.imp.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImp implements TagService {
    @Autowired
    private TagRepository tagRepository;

    private ModelMapper modelMapper=new ModelMapper();

    @Override
    public List<TagDTO> getAllTag() {
        List<Tag> tags=tagRepository.findAll();

        List<TagDTO> tagDTOS=modelMapper.map(tags,List.class);

        return tagDTOS;
    }
}
