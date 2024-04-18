package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.entities.Categories;
import com.example.backendkhoaluan.entities.CourseDetail;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.utils.CriteriaBuilderUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomCourseDetailQuery {
    private CustomCourseDetailQuery() {

    }

    @Data
    @NoArgsConstructor
    public static class CourseDetailFilterParam {
        private Long date;
        private Integer idUser;
        private String sortField;
        private String sortType;
    }

    public static Specification<CourseDetail> getFilterCourse(CourseDetailFilterParam param) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (param.idUser != null) {
                predicates.add(criteriaBuilder.equal(root.get("idUser"), (param.idUser)));
            }
            if (param.date != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), (param.date)));
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
        });
    }
}
