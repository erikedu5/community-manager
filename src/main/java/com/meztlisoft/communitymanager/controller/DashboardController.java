package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.DashboardDto;
import com.meztlisoft.communitymanager.dto.filters.DashboardFilter;
import com.meztlisoft.communitymanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping
    public ResponseEntity<Page<DashboardDto>> findDashboard(@RequestBody DashboardFilter dashboardFilter) {
        return ResponseEntity.ok(dashboardService.findDashboard(dashboardFilter));
    }
}
