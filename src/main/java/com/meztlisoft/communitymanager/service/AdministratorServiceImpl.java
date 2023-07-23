package com.meztlisoft.communitymanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meztlisoft.communitymanager.dto.AdministratorDto;
import com.meztlisoft.communitymanager.dto.filters.AdminFilters;
import com.meztlisoft.communitymanager.entity.AdministratorEntity;
import com.meztlisoft.communitymanager.entity.specification.AdministratorSpecification;
import com.meztlisoft.communitymanager.repository.AdministratorRepository;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdministratorServiceImpl implements AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> administratorRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Page<AdministratorDto> getAll(AdminFilters adminFilters) {
        Pageable paging = PageRequest.of(adminFilters.getPage(), adminFilters.getSize());
        Specification<AdministratorEntity> specification = AdministratorSpecification.getFilteredAdministrator(adminFilters);
        Page<AdministratorEntity> administrators = administratorRepository.findAll(specification, paging);

        List<AdministratorDto> dtos = new ArrayList<>();
        administrators.stream().forEach(admin -> {
            AdministratorDto administratorDto = new AdministratorDto();
            administratorDto.setPassword(null);
            administratorDto.setRole(admin.getRole());
            administratorDto.setCitizen(admin.getCitizen());
            administratorDto.setRetinue(admin.getRetinue());
            administratorDto.setUserName(admin.getUsername());
            dtos.add(administratorDto);
        });

        return new PageImpl<>(dtos, administrators.getPageable(), administrators.getTotalElements());
    }

    @Override
    public AdministratorDto getById(long id) {
        AdministratorEntity administratorEntity = administratorRepository.findByIdAndActive(id, true).orElse(new AdministratorEntity());
        return objectMapper.convertValue(administratorEntity, AdministratorDto.class);
    }
}
