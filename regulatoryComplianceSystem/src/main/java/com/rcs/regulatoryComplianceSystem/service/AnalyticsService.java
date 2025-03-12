package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.repositories.InstitutionRepository;
import com.rcs.regulatoryComplianceSystem.repositories.ReportRepository;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.AnalyticsServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AnalyticsService implements AnalyticsServiceImp {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    InstitutionRepository institutionRepository;

    @Override
    public Map<String, Long> countReportsUploadedByRFI(Integer year) {
        List<Object[]> results =  reportRepository.countReportsUploadedByRFIForYear(year);
        for(Object[] result: results){
            logger.info("Month: " + result[0] + ", Count: " + result[1]);
        }

        Map<String,Long> monthsCount = new LinkedHashMap<>();
        String[] monthNames ={"January","February","March","April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        for (String month:monthNames){
            monthsCount.put(month,0L);
        }

        for(Object[] result:results){
            Integer  monthIndex=(Integer) result[0]-1;
            Long count=(Long)result[1];
            monthsCount.put(monthNames[monthIndex],count);
        }

        return monthsCount;
    }

    @Override
    public Map<String, Long> countRegistrationByRFI(Integer year) {
        List<Object[]> results =  institutionRepository.countInstitutionsRegisteredByRFI(year);
        for(Object[] result: results){
            logger.info("Month: " + result[0] + ", Count: " + result[1]);
        }

        Map<String,Long> monthsCount = new LinkedHashMap<>();
        String[] monthNames ={"January","February","March","April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        for (String month:monthNames){
            monthsCount.put(month,0L);
        }

        for(Object[] result:results){
            Integer  monthIndex=(Integer) result[0]-1;
            Long count=(Long)result[1];
            monthsCount.put(monthNames[monthIndex],count);
        }

        return monthsCount;
    }


}
