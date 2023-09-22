package com.meztlisoft.communitymanager.service;

import com.meztlisoft.communitymanager.dto.DashboardDto;
import com.meztlisoft.communitymanager.dto.filters.DashboardFilter;
import org.springframework.data.domain.Page;

public interface DashboardService {

    Page<DashboardDto> findDashboard(DashboardFilter dashboardFilter);
}
