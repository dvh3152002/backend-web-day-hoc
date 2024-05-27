package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.LessonsDTO;
import com.example.backendkhoaluan.entities.Lessons;
import com.example.backendkhoaluan.payload.request.GetCourseRequest;
import com.example.backendkhoaluan.payload.request.GetLessonRequest;
import com.example.backendkhoaluan.payload.request.LessonRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.LessonService;
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
@RequestMapping("/api/lesson")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    private ModelMapper modelMapper=new ModelMapper();

    @GetMapping("")
    public ResponseEntity<?> getListByPost(@Valid GetLessonRequest request){
        Page<Lessons> page=lessonService.getListLesson(request, PageRequest.of(request.getStart(),request.getLimit()));
        return BaseResponse.successListData(page.getContent().stream()
        .map(data->{
            LessonsDTO lessonsDTO=modelMapper.map(data,LessonsDTO.class);
//            lessonsDTO.setVideo("http://localhost:8081/api/file/video/" +data.getVideo());
            return lessonsDTO;
        }).collect(Collectors.toList()), (int) page.getTotalElements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        return BaseResponse.success(lessonService.getLessonsById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> insertLesson(@Valid @ModelAttribute LessonRequest request,
                                     @RequestParam(name = "video") MultipartFile file) {
        lessonService.save(request,file);
        return BaseResponse.success("Thêm bài học thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable int id,@Valid @ModelAttribute LessonRequest request,
                                     @RequestParam(name = "video",required = false) MultipartFile file) {
        lessonService.updateLesson(id,request,file);
        return BaseResponse.success("Cập nhật bài học thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id){
        lessonService.deleteLesson(id);
        return BaseResponse.success("Xóa bài học thành công");
    }
}
