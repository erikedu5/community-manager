package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.CitizenFilters;
import com.meztlisoft.communitymanager.entity.AssociatedEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;


public class AssociationSpecification {

    public static Specification<AssociatedEntity> getFilteredCitizen(CitizenFilters params, Long retinueId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!params.isGetAll()) {
                if (!retinueId.equals(0L)) {
                    predicates.add(criteriaBuilder.equal(root.get("retinue").get("id"), retinueId));
                }

                if (StringUtils.isNoneBlank(params.getName())) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("citizen").get("name")), "%" + params.getName().toUpperCase()+ "%"));
                }

                if (StringUtils.isNoneBlank(params.getDescription())) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("citizen").get("description")), "%" + params.getDescription().toUpperCase() + "%" ));
                }

                if (StringUtils.isNoneBlank(params.getCurp())) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("citizen").get("curp")), "%" + params.getCurp().toUpperCase() + "%"));
                }

                if (Objects.nonNull(params.getRangeBirthdays())
                        && Objects.nonNull(params.getRangeBirthdays().getUpperLimit())
                        && Objects.nonNull(params.getRangeBirthdays().getLowerLimit())) {
                    predicates.add(criteriaBuilder.between(root.get("citizen").get("birthday"),
                            params.getRangeBirthdays().getUpperLimit(),
                            params.getRangeBirthdays().getLowerLimit()));
                }

                predicates.add(criteriaBuilder.equal(root.get("active"), true));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
