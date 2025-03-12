package com.rcs.regulatoryComplianceSystem.repositories;

import com.rcs.regulatoryComplianceSystem.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {
    List<Report> findByStatus(Report.Status status);
    List<Report> findByStatusIn(List<Report.Status> status);
    @Query("SELECT EXTRACT(MONTH FROM TO_DATE(r.registrationDate, 'yyyy-MM-dd')), COUNT(r) " +
            "FROM Report r " +
            "WHERE r.uploadedBy.role.roleType = com.rcs.regulatoryComplianceSystem.entity.Role.RoleType.RFI " +
            "AND EXTRACT(YEAR FROM TO_DATE(r.registrationDate, 'yyyy-MM-dd')) = :year " +
            "GROUP BY EXTRACT(MONTH FROM TO_DATE(r.registrationDate, 'yyyy-MM-dd')) " +
            "ORDER BY EXTRACT(MONTH FROM TO_DATE(r.registrationDate, 'yyyy-MM-dd'))")
    List<Object[]> countReportsUploadedByRFIForYear(@Param("year") Integer year);

}
