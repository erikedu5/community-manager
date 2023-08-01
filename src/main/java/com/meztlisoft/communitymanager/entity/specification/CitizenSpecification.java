package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.CitizenFilters;
import com.meztlisoft.communitymanager.entity.CitizenEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class CitizenSpecification {

    public static Specification<CitizenEntity> getFilteredCitizen(CitizenFilters params, List<Long> citizenIds, boolean isAdmin) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNoneBlank(params.getName())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + params.getName().toUpperCase()+ "%"));
            }

            if (StringUtils.isNoneBlank(params.getDescription())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + params.getDescription().toUpperCase() + "%" ));
            }

            if (StringUtils.isNoneBlank(params.getCurp())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("curp")), "%" + params.getCurp().toUpperCase() + "%"));
            }

            if (Objects.nonNull(params.getRangeBirthdays())
                    && Objects.nonNull(params.getRangeBirthdays().getUpperLimit())
                    && Objects.nonNull(params.getRangeBirthdays().getLowerLimit())) {
                predicates.add(criteriaBuilder.between(root.get("birthday"),
                        params.getRangeBirthdays().getUpperLimit(),
                        params.getRangeBirthdays().getLowerLimit()));
            }

            if (!isAdmin) {
                if (!citizenIds.isEmpty()) {
                    final Predicate[] predicatesList =
                            citizenIds.stream()
                                    .map(citizenId -> criteriaBuilder.equal(root.get("id"), citizenId))
                                    .toArray(Predicate[]::new);
                    return criteriaBuilder.or(predicatesList);
                } else {
                    predicates.add(criteriaBuilder.equal(root.get("id"), -1));
                }
            }

            predicates.add(criteriaBuilder.equal(root.get("active"), true));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
