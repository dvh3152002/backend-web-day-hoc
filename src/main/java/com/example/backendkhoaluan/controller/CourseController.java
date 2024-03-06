package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.payload.request.CreateCourseRequest;
import com.example.backendkhoaluan.payload.request.GetCourseRequest;
import com.example.backendkhoaluan.payload.response.BaseListResponse;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.CourseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    private final ModelMapper modelMapper=new ModelMapper();

    @PostMapping("/insert")
    public BaseResponse insertCourse(@ModelAttribute CreateCourseRequest createCourseRequest,
                                          @RequestPart MultipartFile file) {
        courseService.save(createCourseRequest, file);
        return BaseResponse.success("Thêm khóa học thành công");

    }

    @GetMapping("/get-all")
    public BaseListResponse<CoursesDTO> getAllCourse(@Valid @RequestBody GetCourseRequest request) {
        log.info("request: {}",request);
        Page<Courses> page=courseService.getAllCourse(request, PageRequest.of(request.getStart(),request.getLimit()));
        List<CoursesDTO> coursesDTOS=page.getContent().stream()
                .map(data->{
                    CoursesDTO courseDTO=new CoursesDTO();
                    courseDTO.setId(data.getId());
                    courseDTO.setDescription(data.getDescription());
                    courseDTO.setName(data.getName());
                    courseDTO.setPrice(data.getPrice());
                    courseDTO.setDiscount(data.getDiscount());
                    courseDTO.setIdUser(data.getUser().getId());
                    courseDTO.setImage("http://localhost:8081/api/file/"+data.getImage());
                    courseDTO.setRating(courseService.calculatorRating(data.getListRatingCourses()));
                    return courseDTO;
                }).collect(Collectors.toList());
        return BaseResponse.successListData(coursesDTOS,(int) page.getTotalElements());
    }

    @GetMapping("/{id}")
    public BaseResponse getCourseById(@PathVariable int id) {
        return BaseResponse.success(courseService.getCourseById(id));
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return BaseResponse.success("Xóa khóa học thành công");
    }
}
