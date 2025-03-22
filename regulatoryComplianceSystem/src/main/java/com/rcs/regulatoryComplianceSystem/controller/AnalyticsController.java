package com.rcs.regulatoryComplianceSystem.controller;

import com.rcs.regulatoryComplianceSystem.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class
AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @PostMapping("/rfi-reports-data")
    public Map<String,Long> countReportsUploadedByRfFI(@RequestParam(required = true)Integer year){
        return analyticsService.countReportsUploadedByRFI(year) ;
    }

    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @PostMapping("/rfi-registration-data")
    public Map<String,Long> countRegistrationByRfFI(@RequestParam(required = true)Integer year){
        return analyticsService.countRegistrationByRFI(year) ;
    }

}
