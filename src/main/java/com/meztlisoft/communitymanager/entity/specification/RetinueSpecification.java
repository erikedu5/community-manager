package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.RetinueFilters;
import com.meztlisoft.communitymanager.entity.RetinueEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class RetinueSpecification {

    public static Specification<RetinueEntity> getFilteredRetinue(RetinueFilters params) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNoneBlank(params.getName())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), params.getName().toUpperCase()));
            }

            predicates.add(criteriaBuilder.gt(root.get("id"), 0));
            predicates.add(criteriaBuilder.equal(root.get("activo"), true));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
