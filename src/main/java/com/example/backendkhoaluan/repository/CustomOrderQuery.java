package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.entities.*;
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

public class CustomOrderQuery {
    private CustomOrderQuery() {
    }

    @Data
    @NoArgsConstructor
    public static class OrderFilterParam {
        private Integer totalCost;
        private String vnpBankCode;
        private Boolean status;
        private Integer idUser;
        private Long startDate = new Date().getTime() - (12L * 30L * 24L * 60L * 60L * 1000L);
        private Long endDate = new Date().getTime();
        private String sortField;
        private String sortType;
    }

    public static Specification<Orders> getFilterOrder(OrderFilterParam param) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (param.idUser != null) {
                Join<Orders, User> userJoin = root.join("user");
                predicates.add(criteriaBuilder.equal(userJoin.get("id"), (param.idUser)));
            }

            // Kiểm tra liệu bạn muốn lấy tất cả các đơn hàng (bao gồm isSuccess=true và isSuccess=false) hay chỉ một trong hai loại.
            if (param.status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), param.status)); // Lấy các đơn hàng với isSuccess=true
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
        });
    }
}
