package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.payload.request.CreateCourseRequest;
import com.example.backendkhoaluan.repository.CoursesRepository;
import com.example.backendkhoaluan.repository.CustomCourseQuery;
import com.example.backendkhoaluan.service.imp.CourseService;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CourseServiceImp implements CourseService {
    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private FilesStorageService filesStorageService;

    @Override
    public Page<Courses> getAllCourse(CustomCourseQuery.CourseFilterParam param,PageRequest pageRequest) {
        Specification<Courses> specification = CustomCourseQuery.getFilterCourse(param);
        return coursesRepository.findAll(specification, pageRequest);
    }

    @Override
    @Transactional(rollbackFor = {InsertException.class, Exception.class})
    public void deleteCourse(int id) {
        try {
            Optional<Courses> coursesOptional=coursesRepository.findById(id);
            coursesOptional.ifPresent(data->{
                filesStorageService.deleteAll(data.getImage());
                coursesRepository.delete(data);
            });
        }catch (Exception e){
            throw new DeleteException("Xóa khóa học thất bại",e.getLocalizedMessage());
        }
    }

    @Override
    public CoursesDTO getCourseById(int id) {
        Optional<Courses> coursesOptional=coursesRepository.findById(id);
        CoursesDTO courseDTO = new CoursesDTO();
        coursesOptional.ifPresent(data->{
            courseDTO.setId(data.getId());
            courseDTO.setDescription(data.getDescription());
            courseDTO.setName(data.getName());
            courseDTO.setPrice(data.getPrice());
            courseDTO.setDiscount(data.getDiscount());
            courseDTO.setImage("http://localhost:8081/api/file/"+data.getImage());
            courseDTO.setIdUser(data.getUser().getId());
            courseDTO.setRating(calculatorRating(data.getListRatingCourses()));
        });

        return courseDTO;
    }

    @Transactional(rollbackFor = {InsertException.class, Exception.class})
    @Override
    public void save(CreateCourseRequest createCourseRequest, MultipartFile file) {
        try {
            String fileName=filesStorageService.save(file);

            Courses courseEntity = new Courses();
            courseEntity.setName(createCourseRequest.getName());
            courseEntity.setDescription(createCourseRequest.getDescription());
            courseEntity.setDiscount(createCourseRequest.getDiscount());
            courseEntity.setImage(fileName);
            courseEntity.setPrice(createCourseRequest.getPrice());

            User userEntity = new User();
            userEntity.setId(createCourseRequest.getUserId());

            courseEntity.setUser(userEntity);

            coursesRepository.save(courseEntity);
        } catch (Exception e) {
            throw new InsertException("Lỗi insert course", e.getLocalizedMessage());
        }
    }

    @Override
    public double calculatorRating(List<RatingCourse> listRating) {
        double totalPoint = 0;
        for (RatingCourse data : listRating) {
            totalPoint += data.getRatePoint();
        }
        return totalPoint == 0 ? 0 : totalPoint / listRating.size();
    }
}
