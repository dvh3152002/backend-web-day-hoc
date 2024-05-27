package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.entities.Post;
import com.example.backendkhoaluan.utils.CriteriaBuilderUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomPostQuery {
    private CustomPostQuery() {
    }

    @Data
    @NoArgsConstructor
    public static class PostFilterParam {
        private String keywords;
        private Long startDate;
        private Long endDate;
        private String sortField;
        private String sortType;
    }

    public static Specification<Post> getFilterPost(PostFilterParam param) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (param.keywords != null) {
                predicates.add(CriteriaBuilderUtils.createPredicateForSearchInsensitive(root, criteriaBuilder,
                        param.keywords, "name"));
            }
            if (param.startDate != null && param.endDate != null) {
                Date startDateValue = new Date(param.startDate);
                Date endDateValue = new Date(param.endDate);
                predicates.add(criteriaBuilder.between(root.get("createDate"), startDateValue, endDateValue));
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
