package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.ComitivaFilters;
import com.meztlisoft.communitymanager.entity.ComitivaEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ComitivaSpecification {

    public static Specification<ComitivaEntity> getFilteredComitiva(ComitivaFilters params) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNoneBlank(params.getNombre())) {
                predicates.add(criteriaBuilder.like(root.get("nombre"), params.getNombre()));
            }

            if (Objects.nonNull(params.getActivo())) {
                predicates.add(criteriaBuilder.equal(root.get("activo"), params.getActivo()));
            }

            predicates.add(criteriaBuilder.gt(root.get("id"), 0));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
