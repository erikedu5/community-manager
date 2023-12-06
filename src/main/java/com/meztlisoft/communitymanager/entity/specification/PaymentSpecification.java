package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.PaymentFilters;
import com.meztlisoft.communitymanager.entity.PaymentEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentSpecification {

    public static Specification<PaymentEntity> getFilteredPayment(PaymentFilters params, Long cooperationId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(params.getCredit())) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("payment")), params.getCredit()));
            }

            if (Objects.nonNull(params.getComplete())) {
                predicates.add(criteriaBuilder.equal(root.get("complete"), params.getComplete()));
            }

            if (Objects.nonNull(params.getVolunteer())) {
                predicates.add(criteriaBuilder.equal(root.get("volunteer"), params.getVolunteer()));
            }

            if (Objects.nonNull(params.getCitizenName())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("associated").get("citizen").get("name")),
                        "%" + params.getCitizenName().toUpperCase() + "%"));
            }


            if (Objects.nonNull(params.getCitizenCurp())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("associated").get("citizen").get("curp")),
                         params.getCitizenCurp().toUpperCase()));
            }

            if (Objects.nonNull(params.getCitizenDescription())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("associated").get("citizen").get("description")),
                        "%" + params.getCitizenDescription().toUpperCase() + "%"));
            }

            predicates.add(criteriaBuilder.equal(root.get("cooperation").get("id"), cooperationId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
