package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.AdministradorDto;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import com.meztlisoft.communitymanager.entity.AdministradorEntity;
import com.meztlisoft.communitymanager.entity.specification.AdministradorSpecification;
import com.meztlisoft.communitymanager.repository.AdministradorRepository;
import com.meztlisoft.communitymanager.repository.CiudadanoRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final CiudadanoRepository ciudadanoRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return administradorRepository.findByUserName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public Page<AdministradorDto> getAll(AdminFilters adminFilters) {
        Pageable paging = PageRequest.of(adminFilters.getPage(), adminFilters.getSize());
        Specification<AdministradorEntity> specification = AdministradorSpecification.getFilteredAdministrador(adminFilters);
        Page<AdministradorEntity> administradores = administradorRepository.findAll(specification, paging);

        List<AdministradorDto> dtos = new ArrayList<>();
        administradores.stream().forEach(admin -> {
            AdministradorDto administradorDto = new AdministradorDto();
            administradorDto.setPassword(null);
            administradorDto.setRole(admin.getRole());
            administradorDto.setCiudadano(admin.getCiudadano());
            administradorDto.setComitiva(admin.getComitiva());
            administradorDto.setUserName(admin.getUsername());
            dtos.add(administradorDto);
        });

        return new PageImpl<>(dtos, administradores.getPageable(), administradores.getTotalElements());
    }
}
