package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.BillFilters;
import com.meztlisoft.communitymanager.entity.BillEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BillSpecification {

    public static Specification<BillEntity> getFilteredBill(BillFilters params, Long retinueId) {

        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(params.getConcept())) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("concept")), params.getConcept()));
            }

            if (Objects.nonNull(params.getDateTo())
                    && Objects.nonNull(params.getDateFrom())) {
                predicates.add(criteriaBuilder.between(root.get("date"),
                        params.getDateTo(),
                        params.getDateFrom()));
            }

            predicates.add(criteriaBuilder.equal(root.get("retinue").get("id"), retinueId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
