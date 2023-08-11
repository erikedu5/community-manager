package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.BillDto;
import com.meztlisoft.communitymanager.dto.filters.BillFilters;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface BillService {
    Page<BillDto> getAll(BillFilters billFilters, Long retinueId);

    Resource downloadEvidence(Long id);

    ActionStatusResponse uploadFile(MultipartFile file, Long id);

    BillDto createBill(Long retinueId, BillDto billDto, String token);
}
