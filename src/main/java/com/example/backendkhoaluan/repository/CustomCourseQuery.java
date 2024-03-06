package com.example.backendkhoaluan.repository;

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
        private Integer idUser;
        private Double rating;
    }

    public static Specification<Courses> getFilterCourse(CourseFilterParam param) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!Strings.isEmpty(param.getKeywords())) {
                predicates.add(CriteriaBuilderUtils.createPredicateForSearchInsensitive(root, criteriaBuilder, param.getKeywords(),
                        "name"));
            }
            if (param.getIdUser()!=null) {
                Join<Courses, User> courseJoin = root.join("user");
                predicates.add(criteriaBuilder.equal(courseJoin.get("id"),(param.getIdUser())));
            }
            if (param.getMaxPrice()!=null && param.getMinPrice()!=null){
                predicates.add(criteriaBuilder.between(criteriaBuilder.prod(root.get("price"),criteriaBuilder.diff(1,root.get("discount"))),param.getMinPrice(),param.getMaxPrice()));
            }else if(param.getMinPrice()!=null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.prod(root.get("price"),criteriaBuilder.diff(1,root.get("discount"))),param.getMinPrice()));
            }else if(param.getMaxPrice()!=null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.prod(root.get("price"),criteriaBuilder.diff(1,root.get("discount"))),param.getMaxPrice()));
            }
            if(param.getRating()!=null) {
                query.groupBy(root.get("id"));
                query.having(criteriaBuilder.equal(criteriaBuilder.avg(root.get("listRatingCourses").get("ratePoint")), param.getRating()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
