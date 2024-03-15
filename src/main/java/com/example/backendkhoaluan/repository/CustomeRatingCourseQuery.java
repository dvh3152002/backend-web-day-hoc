package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.utils.CriteriaBuilderUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomeRatingCourseQuery {
    private CustomeRatingCourseQuery() {
    }

    @Data
    @NoArgsConstructor
    public static class RatingCourseFilterParam {
        private String keywords;
        private String content;
        private Integer idCourse;
        private Float ratePoint;
        private Integer idUser;
        private Long startDate;
        private Long endDate;
        private String sortField;
        private String sortType;
    }

    public static Specification<RatingCourse> getFilterRating(RatingCourseFilterParam param) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (param.keywords != null) {
                predicates.add(CriteriaBuilderUtils.createPredicateForSearchInsensitive(root, criteriaBuilder,
                        param.keywords, "content"));
            }
            if (param.ratePoint != null) {
                predicates.add(criteriaBuilder.equal(root.get("ratePoint"), param.ratePoint));
            }
            if (param.idCourse != null) {
                Join<RatingCourse, Courses> coursesJoin=root.join("course");
                predicates.add(criteriaBuilder.equal(coursesJoin.get("id"),param.idCourse));
            }
            if (param.idUser != null) {
                Join<RatingCourse, User> userJoin=root.join("user");
                predicates.add(criteriaBuilder.equal(userJoin.get("id"),param.idUser));
            }
            if (param.startDate != null && param.endDate!=null) {
                Date startDateValue=new Date(param.startDate);
                Date endDateValue=new Date(param.endDate);
                predicates.add(criteriaBuilder.between(root.get("create_date"),startDateValue,endDateValue));
            }
            if (param.sortField != null && !param.sortField.equals("")) {
                if (param.sortType.equals(Constants.SortType.DESC) || param.sortType.equals("")) {
                    query.orderBy(criteriaBuilder.desc(root.get(param.sortField)));
                }
                if (param.sortType.equals(Constants.SortType.ASC)) {
                    query.orderBy(criteriaBuilder.asc(root.get(param.sortField)));
                }
            } else {
                query.orderBy(criteriaBuilder.desc(root.get("id")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }
}
