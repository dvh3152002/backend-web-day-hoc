package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.repository.CustomeRatingCourseQuery;
import com.example.backendkhoaluan.repository.RatingCourseRepository;
import com.example.backendkhoaluan.service.imp.RatingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@Service
public class RatingServiceImp implements RatingService {
    @Autowired
    private RatingCourseRepository ratingCourseRepository;
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
                throw new DataNotFoundException(Constants.ErrorMessageUserValidation.NOT_FIND_USER_BY_ID + id);
            }
            ratingCourseRepository.deleteById(id);
            Optional<RatingCourse> ratingAfterDelete = ratingCourseRepository.findById(id);

            // Kiểm tra xem rating còn tồn tại sau khi xóa hay không
            if (ratingAfterDelete.isPresent()) {
                return "Không xóa được rating có id = " + id;
            } else {
                return "Xóa rating thành công";
            }
        }catch (Exception e){
            throw new DeleteException("Lỗi xóa rating",e.getLocalizedMessage());
        }
    }
}
