package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.AssociatedFilters;
import com.meztlisoft.communitymanager.entity.AssociatedEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;


public class AssociationSpecification {

    public static Specification<AssociatedEntity> getFilterAssociation(AssociatedFilters params) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (params.getCitizenName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("citizen").get("name")), "%" + params.getCitizenName().toUpperCase() + "%"));
            }

            if (params.getIdRetinue() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"),  params.getIdRetinue()));
            }

            predicates.add(criteriaBuilder.equal(root.get("active"), true));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
