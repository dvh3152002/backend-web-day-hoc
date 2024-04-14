package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.entities.Categories;
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
import java.util.List;

public class CustomCourseQuery {
    private CustomCourseQuery() {
    }

    @Data
    @NoArgsConstructor
    public static class CourseFilterParam {
        private String keywords;
        private Integer minPrice;
        private Integer maxPrice;
        private Integer idTeacher;
        private Integer idCategory;
        private Double rating;
        private Boolean free;
        private String sortField;
        private String sortType;
    }

    public static Specification<Courses> getFilterCourse(CourseFilterParam param) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!Strings.isEmpty(param.keywords)) {
                predicates.add(CriteriaBuilderUtils.createPredicateForSearchInsensitive(root, criteriaBuilder, param.keywords,
                        "name"));
            }
            if (param.idTeacher != null) {
                Join<Courses, User> courseJoin = root.join("teacher");
                predicates.add(criteriaBuilder.equal(courseJoin.get("id"), (param.idTeacher)));
            }
            if (param.idCategory != null) {
                Join<Courses, Categories> categoriesJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoriesJoin.get("id"), (param.idCategory)));
            }
            if (param.free != null) {
                predicates.add(criteriaBuilder.equal(root.get("free"), (param.free)));
            }
            if (param.maxPrice != null && param.minPrice != null) {
                predicates.add(criteriaBuilder.between(root.get("price"), param.minPrice, param.maxPrice));
            } else if (param.minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), param.minPrice));
            } else if (param.maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), param.maxPrice));
            }
            if (param.rating != null) {
                query.groupBy(root.get("id"));
                query.having(criteriaBuilder.between(criteriaBuilder.avg(root.get("listRatingCourses").get("ratePoint")), 0d,param.rating));
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
