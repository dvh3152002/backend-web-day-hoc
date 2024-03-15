package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.Lessons;
import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.utils.CriteriaBuilderUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomeLessonQuery {
    private CustomeLessonQuery() {
    }

    @Data
    @NoArgsConstructor
    public static class LessonFilterParam {
        private String keywords;
        private Integer idCourse;
        private String sortField;
        private String sortType;
    }

    public static Specification<Lessons> getFilterLesson(LessonFilterParam param) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (param.keywords != null) {
                predicates.add(CriteriaBuilderUtils.createPredicateForSearchInsensitive(root, criteriaBuilder,
                        param.keywords, "title"));
            }
            if (param.idCourse != null) {
                Join<Lessons, Courses> coursesJoin = root.join("course");
                predicates.add(criteriaBuilder.equal(coursesJoin.get("id"), param.idCourse));
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
