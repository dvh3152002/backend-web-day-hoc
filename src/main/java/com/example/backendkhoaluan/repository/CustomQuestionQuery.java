package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.entities.Questions;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.utils.CriteriaBuilderUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomQuestionQuery {
    private CustomQuestionQuery() {
    }

    @Data
    @NoArgsConstructor
    public static class QuestionFilterParam {
        private String keywords;
        private String tags;
        private Integer idUser;
        private String sortField;
        private String sortType;
    }

    public static Specification<Questions> getFilterQuestion(QuestionFilterParam param) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (param.keywords != null) {
                predicates.add(CriteriaBuilderUtils.createPredicateForSearchInsensitive(root, criteriaBuilder,
                        param.keywords, "title","body"));
            }
            if (param.tags != null && !param.tags.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("tags"), "%" + param.tags + "%"));
            }
            if (param.idUser != null) {
                Join<Questions, User> userJoin = root.join("user");
                predicates.add(criteriaBuilder.equal(userJoin.get("id"), (param.idUser)));
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
