package com.rcs.regulatoryComplianceSystem.repositories;

import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.Report;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {
    List<Report> findByStatus(Report.Status status);
    List<Report> findByStatusIn(List<Report.Status> status);
    List<Report> findByUploadedBy(User user);
    List<Report> findByApprovedBy(User user);
    List<Report> findByRejectedBy(User user);
    List<Report> findByInstitution(Institution institution);

    @Query("SELECT EXTRACT(MONTH FROM TO_DATE(r.registrationDate, 'yyyy-MM-dd')), COUNT(r) " +
            "FROM Report r " +
            "WHERE EXTRACT(YEAR FROM TO_DATE(r.registrationDate, 'yyyy-MM-dd')) = :year " +
            "GROUP BY EXTRACT(MONTH FROM TO_DATE(r.registrationDate, 'yyyy-MM-dd')) " +
            "ORDER BY EXTRACT(MONTH FROM TO_DATE(r.registrationDate, 'yyyy-MM-dd'))")
    List<Object[]> getReportCountByMonth(@Param("year") int year);


}
