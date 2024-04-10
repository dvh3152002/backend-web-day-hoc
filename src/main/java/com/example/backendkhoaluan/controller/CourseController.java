package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.CategoriesDTO;
import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.payload.request.CreateCourseRequest;
import com.example.backendkhoaluan.payload.request.GetCourseRequest;
import com.example.backendkhoaluan.payload.response.BaseListResponse;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
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
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CloudinaryService cloudinaryService;

    private final ModelMapper modelMapper=new ModelMapper();

    @PostMapping("")
    public BaseResponse insertCourse(@Valid @ModelAttribute CreateCourseRequest createCourseRequest,
                                     @RequestParam MultipartFile file) {
        log.info("request: {}",createCourseRequest);
        courseService.save(createCourseRequest, file);
        return BaseResponse.success("Thêm khóa học thành công");

    }

    @PutMapping("/{id}")
    public BaseResponse updateCourse(@PathVariable int id,@ModelAttribute @Valid CreateCourseRequest createCourseRequest,
                                     @RequestPart(required = false) MultipartFile file) {
        courseService.updateCourse(id,createCourseRequest, file);
        return BaseResponse.success("Cập nhật khóa học thành công");

    }

    @GetMapping("")
    public BaseListResponse<CoursesDTO> getAllCourse(@Valid @ModelAttribute GetCourseRequest request) {
        log.info("request: {}",request);
        Page<Courses> page=courseService.getAllCourse(request, PageRequest.of(request.getStart(),request.getLimit()));
        List<CoursesDTO> coursesDTOS=page.getContent().stream()
                .map(data->{
                    CoursesDTO courseDTO=new CoursesDTO();
                    courseDTO.setId(data.getId());
                    courseDTO.setDescription(data.getDescription());
                    courseDTO.setName(data.getName());
                    courseDTO.setSlug(data.getSlug());
                    courseDTO.setPrice(data.getPrice());
                    courseDTO.setDiscount(data.getDiscount());
                    courseDTO.setUser(modelMapper.map(data.getUser(), UsersDTO.class));
                    courseDTO.setImage(cloudinaryService.getImageUrl(data.getImage()));
                    courseDTO.setCategory(modelMapper.map(data.getCategory(), CategoriesDTO.class));
                    courseDTO.setRating(courseService.calculatorRating(data.getListRatingCourses()));
                    courseDTO.setCreateDate(data.getCreateDate());
                    courseDTO.setCount(data.getListCourseDetail().size());

                    return courseDTO;
                }).collect(Collectors.toList());
        return BaseResponse.successListData(coursesDTOS,(int) page.getTotalElements());
    }

    @GetMapping("/cart")
    public BaseResponse getCourseByIds(@RequestParam Set<Integer> ids){
        List<CoursesDTO> dtoList=courseService.getCourseByIds(ids);
        return BaseResponse.successListData(dtoList,dtoList.size());
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
