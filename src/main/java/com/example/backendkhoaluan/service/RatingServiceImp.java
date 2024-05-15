package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.RatingCourseDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.payload.request.CreateRatingRequest;
import com.example.backendkhoaluan.repository.CustomeRatingCourseQuery;
import com.example.backendkhoaluan.repository.RatingCourseRepository;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.RatingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class RatingServiceImp implements RatingService {
    @Autowired
    private RatingCourseRepository ratingCourseRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    private ModelMapper modelMapper=new ModelMapper();
    @Override
    public Page<RatingCourse> getAllRating(CustomeRatingCourseQuery.RatingCourseFilterParam param,
                                           PageRequest pageRequest) {
        Specification<RatingCourse> specification = CustomeRatingCourseQuery.getFilterRating(param);
        return ratingCourseRepository.findAll(specification,pageRequest);
    }

    @Override
    public String deleteRating(int id) {
        try {
            Optional<RatingCourse> ratingCourse = ratingCourseRepository.findById(id);
            if (!ratingCourse.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageRatingValidation.NOT_FIND_RATING_BY_ID + id);
            }
            ratingCourseRepository.deleteById(id);
            return "Xóa đánh giá thành công";
        }catch (Exception e){
            throw new DeleteException("Lỗi xóa rating",e.getLocalizedMessage());
        }
    }

    @Override
    public void createRating(CreateRatingRequest request) {
        log.info("request: {}",request);
        try {
            RatingCourse ratingCourse=new RatingCourse();
            ratingCourse.setRatePoint(request.getRatePoint());
            ratingCourse.setContent(request.getContent());
            ratingCourse.setCreateDate(new Date());

            Courses courses=new Courses();
            courses.setId(request.getIdCourse());
            ratingCourse.setCourse(courses);

            User user=new User();
            user.setId(request.getIdUser());
            ratingCourse.setUser(user);

            ratingCourseRepository.save(ratingCourse);
        }catch (Exception e){
            throw new InsertException("Lỗi thêm đánh giá: ",e.getLocalizedMessage());
        }
    }

    @Override
    public void updateRating(int id, CreateRatingRequest request) {
        Optional<RatingCourse> ratingCourse = ratingCourseRepository.findById(id);
        if (!ratingCourse.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_ID + id);
        }
        RatingCourse rating=ratingCourse.get();
        rating.setCreateDate(new Date());
        rating.setRatePoint(request.getRatePoint());
        rating.setContent(request.getContent());

        ratingCourseRepository.save(rating);
    }

    @Override
    public RatingCourseDTO getRating(int idCourse, int idUser) {
        User user=new User();
        user.setId(idUser);

        Courses courses=new Courses();
        courses.setId(idCourse);
        Optional<RatingCourse> ratingCourseOptional=ratingCourseRepository.findByUserAndAndCourse(user,courses);

        if(!ratingCourseOptional.isPresent()){
            return null;
        }

        RatingCourse ratingCourse=ratingCourseOptional.get();
        RatingCourseDTO dto=new RatingCourseDTO();

        dto.setContent(ratingCourse.getContent());
        dto.setRatePoint(ratingCourse.getRatePoint());
        dto.setId(ratingCourse.getId());
        dto.setCreateDate(ratingCourse.getCreateDate());

        UsersDTO usersDTO=new UsersDTO();
        usersDTO.setFullname(ratingCourse.getUser().getFullname());
        if(ratingCourse.getUser().getAvatar()!=null){
            usersDTO.setAvatar(cloudinaryService.getImageUrl(ratingCourse.getUser().getAvatar()));
        }
        dto.setUser(usersDTO);
        return dto;
    }
}
