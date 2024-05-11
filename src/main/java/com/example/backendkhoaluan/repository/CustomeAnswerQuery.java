package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.entities.Answers;
import com.example.backendkhoaluan.entities.Courses;
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

public class CustomeAnswerQuery {
    private CustomeAnswerQuery() {
    }

    @Data
    @NoArgsConstructor
    public static class AnswerFilterParam {
        private String keywords;
        private Integer idQuestion;
        private Integer idUser;
        private String sortField;
        private String sortType;
    }

    public static Specification<Answers> getFilterAnswer(AnswerFilterParam param) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (param.keywords != null) {
                predicates.add(CriteriaBuilderUtils.createPredicateForSearchInsensitive(root, criteriaBuilder,
                        param.keywords, "body"));
            }
            if (param.idQuestion != null) {
                Join<Answers, Questions> questionsJoin = root.join("question");
                predicates.add(criteriaBuilder.equal(questionsJoin.get("id"), (param.idQuestion)));
            }
            if (param.idUser != null) {
                Join<Answers, User> userJoin = root.join("user");
                predicates.add(criteriaBuilder.equal(userJoin.get("id"), (param.idQuestion)));
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
