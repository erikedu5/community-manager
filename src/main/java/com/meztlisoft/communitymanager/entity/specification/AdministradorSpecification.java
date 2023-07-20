package com.meztlisoft.communitymanager.entity.specification;

import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import com.meztlisoft.communitymanager.entity.AdministradorEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class AdministradorSpecification {

    public static Specification<AdministradorEntity> getFilteredAdministrador(AdminFilters params) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getRole() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), params.getRole()));
            }

            if (params.getNombreComitiva() != null) {
                predicates.add(criteriaBuilder.like(root.get("comitiva").get("nombre"), "%" + params.getNombreComitiva() + "%"));
            }

            if (params.getNombreCiudadano() != null) {
                predicates.add(criteriaBuilder.like(root.get("ciudadano").get("nombre"), "%" + params.getNombreCiudadano() + "%"));
            }

            predicates.add(criteriaBuilder.gt(root.get("id"), 0));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
