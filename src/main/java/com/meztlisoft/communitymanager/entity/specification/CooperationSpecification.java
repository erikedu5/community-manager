package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.CooperationFilters;
import com.meztlisoft.communitymanager.entity.CooperationEntity;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CooperationSpecification {

    public static Specification<CooperationEntity> getCooperationFilters(CooperationFilters params, Long retinueId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNoneBlank(params.getConcept())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("concept")), "%" + params.getConcept().toUpperCase()+ "%"));
            }

            if (Objects.nonNull(params.getLimitDate())) {
                predicates.add(criteriaBuilder.equal(root.get("limitDate"), params.getLimitDate()));
            }

            if (Objects.nonNull(params.getStartDate())) {
                predicates.add(criteriaBuilder.equal(root.get("startDate"), params.getStartDate()));
            }

            if (Objects.nonNull(retinueId)) {
                predicates.add(criteriaBuilder.equal(root.get("retinue").get("id"), retinueId));
            }

            predicates.add(criteriaBuilder.equal(root.get("active"), true));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
