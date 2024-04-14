package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.CategoriesDTO;
import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.dto.LessonsDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.*;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.exception.UpdateException;
import com.example.backendkhoaluan.payload.request.CreateCourseRequest;
import com.example.backendkhoaluan.repository.*;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.CourseService;
import com.example.backendkhoaluan.service.imp.LessonService;
import com.example.backendkhoaluan.utils.SlugUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
public class CourseServiceImp implements CourseService {
    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private RatingCourseRepository ratingCourseRepository;

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @Value("${root.path.image}")
    private String path;

    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public Page<Courses> getAllCourse(CustomCourseQuery.CourseFilterParam param, PageRequest pageRequest) {
        Specification<Courses> specification = CustomCourseQuery.getFilterCourse(param);
        return coursesRepository.findAll(specification, pageRequest);
    }

    @Override
    @Transactional(rollbackFor = {DeleteException.class, Exception.class})
    public void deleteCourse(int id) {
        try {
            Optional<Courses> coursesOptional = coursesRepository.findById(id);
            if (!coursesOptional.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageCourseValidation.NOT_FIND_COURSE_BY_ID + id);
            }
            Courses data = coursesOptional.get();
            cloudinaryService.deleteFile(data.getImage());
            ratingCourseRepository.deleteAll(data.getListRatingCourses());
            lessonService.deleteAll(data.getListLessons());
            courseDetailRepository.deleteAll(data.getListCourseDetail());
            coursesRepository.delete(data);
        } catch (Exception e) {
            throw new DeleteException("Xóa khóa học thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    public CoursesDTO getCourseById(int id) {
        Optional<Courses> coursesOptional = coursesRepository.findById(id);
        if (!coursesOptional.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageCourseValidation.NOT_FIND_COURSE_BY_ID + id);
        }
        Courses data = coursesOptional.get();
        CoursesDTO courseDTO = new CoursesDTO();
        courseDTO.setId(data.getId());
        courseDTO.setDescription(data.getDescription());
        courseDTO.setName(data.getName());
        courseDTO.setSlug(data.getSlug());
        courseDTO.setPrice(data.getPrice());
        courseDTO.setDiscount(data.getDiscount());
        courseDTO.setImage(cloudinaryService.getImageUrl(data.getImage()));
        courseDTO.setTeacher(modelMapper.map(data.getTeacher(),UsersDTO.class));
        courseDTO.setRating(calculatorRating(data.getListRatingCourses()));
        courseDTO.setCreateDate(data.getCreateDate());
        courseDTO.setLimitTime(data.getLimitTime());
        courseDTO.setCategory(modelMapper.map(data.getCategory(), CategoriesDTO.class));
        courseDTO.setFree(data.isFree());

        List<LessonsDTO> list=modelMapper.map(data.getListLessons(),List.class);
        courseDTO.setLessons(list);

        return courseDTO;
    }

    @Transactional(rollbackFor = {InsertException.class, Exception.class})
    @Override
    public void save(CreateCourseRequest createCourseRequest, MultipartFile file) {
        try {
            String fileName = cloudinaryService.uploadFile(file);

            Courses courseEntity = new Courses();
            courseEntity.setName(createCourseRequest.getName());
            courseEntity.setDescription(createCourseRequest.getDescription());
            courseEntity.setDiscount(createCourseRequest.isFree()?0:createCourseRequest.getDiscount());
            courseEntity.setImage(fileName);
            courseEntity.setPrice(createCourseRequest.isFree()?0:createCourseRequest.getPrice());
            courseEntity.setSlug(SlugUtils.toSlug(createCourseRequest.getName()));
            courseEntity.setCreateDate(new Date());
            courseEntity.setFree(createCourseRequest.isFree());
            courseEntity.setLimitTime(createCourseRequest.getLimitTime());

            Categories categories=new Categories();
            categories.setId(createCourseRequest.getCategoryId());
            courseEntity.setCategory(categories);

            User userEntity = new User();
            userEntity.setId(createCourseRequest.getTeacherId());

            courseEntity.setTeacher(userEntity);

            coursesRepository.save(courseEntity);
        } catch (Exception e) {
            throw new InsertException("Lỗi insert course", e.getLocalizedMessage());
        }
    }

    @Transactional(rollbackFor = {UpdateException.class, Exception.class})
    @Override
    public void updateCourse(int id,CreateCourseRequest createCourseRequest, MultipartFile file) {
        try {
            Optional<Courses> coursesOptional = coursesRepository.findById(id);
            if (!coursesOptional.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageCourseValidation.NOT_FIND_COURSE_BY_ID + id);
            }
            Courses courseEntity=coursesOptional.get();
            courseEntity.setName(createCourseRequest.getName());
            courseEntity.setDescription(createCourseRequest.getDescription());
            courseEntity.setDiscount(createCourseRequest.isFree()?0:createCourseRequest.getDiscount());
            if(file!=null){
                String fileName = cloudinaryService.uploadFile(file);
                courseEntity.setImage(fileName);
            }
            courseEntity.setPrice(createCourseRequest.isFree()?0:createCourseRequest.getPrice());
            courseEntity.setFree(createCourseRequest.isFree());
            courseEntity.setCreateDate(new Date());
            courseEntity.setSlug(SlugUtils.toSlug(createCourseRequest.getName()));
            courseEntity.setLimitTime(createCourseRequest.getLimitTime());

            Categories categories=new Categories();
            categories.setId(createCourseRequest.getCategoryId());
            courseEntity.setCategory(categories);

            User userEntity = new User();
            userEntity.setId(createCourseRequest.getTeacherId());

            courseEntity.setTeacher(userEntity);

            coursesRepository.save(courseEntity);
        } catch (Exception e) {
            throw new UpdateException("Lỗi cập nhật khóa học", e.getLocalizedMessage());
        }
    }

    @Override
    public List<CoursesDTO> getCourseByIds(Set<Integer> ids) {
        List<Courses> courses=coursesRepository.findAllById(ids);
        List<CoursesDTO> dtoList=new ArrayList<>();
        for (Courses data:courses){
            CoursesDTO courseDTO=new CoursesDTO();
            courseDTO.setId(data.getId());
            courseDTO.setName(data.getName());
            courseDTO.setPrice(data.getPrice());
            courseDTO.setDiscount(data.getDiscount());
            courseDTO.setFree(data.isFree());
            courseDTO.setTeacher(modelMapper.map(data.getTeacher(), UsersDTO.class));
            courseDTO.setImage(cloudinaryService.getImageUrl(data.getImage()));
            courseDTO.setRating(calculatorRating(data.getListRatingCourses()));
            courseDTO.setCreateDate(data.getCreateDate());
            courseDTO.setLimitTime(data.getLimitTime());

            dtoList.add(courseDTO);
        }
        return dtoList;
    }

    @Override
    public List<CoursesDTO> getListCourse(int idUser) {
        List<CourseDetail> detailList=courseDetailRepository.findAllByIdUser(idUser);
        List<CoursesDTO> dtoList=new ArrayList<>();
        for (CourseDetail courseDetail:detailList){
            Courses courses=courseDetail.getCourse();
            CoursesDTO dto=new CoursesDTO();
            dto.setId(courses.getId());
            dto.setName(courses.getName());
            dto.setSlug(courses.getSlug());
            dto.setImage(cloudinaryService.getImageUrl(courses.getImage()));
            dto.setTeacher(modelMapper.map(courses.getTeacher(),UsersDTO.class));
            dto.setRating(calculatorRating(courses.getListRatingCourses()));
            dto.setFree(courses.isFree());
            if(courses.getListLessons().size()>0){
                dto.setIdStart(courses.getListLessons().get(0).getId());
            }
            dto.setLimitTime(courses.getLimitTime());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public double calculatorRating(List<RatingCourse> listRating) {
        double totalPoint = 0;
        for (RatingCourse data : listRating) {
            totalPoint += data.getRatePoint();
        }
        return totalPoint == 0 ? 0 : totalPoint / listRating.size();
    }

    @Override
    public boolean isCoursePurchased(int idUser, int idCourse) {
        boolean isSuccess=false;
        Courses courses=new Courses();
        courses.setId(idCourse);
        Optional<CourseDetail> courseDetailOptional=courseDetailRepository.findByCourseAndIdUser(courses,idUser);
        if(courseDetailOptional.isPresent()){
            CourseDetail courseDetail=courseDetailOptional.get();
            isSuccess=courseDetail.getEndDate().after(new Date());
            System.out.println("date: "+new Date());
            System.out.println("limit: "+courseDetail.getEndDate());
        }

        return isSuccess;
    }
}
