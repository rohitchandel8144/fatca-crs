package com.rcs.regulatoryComplianceSystem.service.serviceImp;


import java.util.Map;

public interface AnalyticsServiceImp {
    public Map<String,Long> countReportsUploadedByRFI(Integer year);
    public Map<String, Long> countRegistrationByRFI(Integer year) ;
}
