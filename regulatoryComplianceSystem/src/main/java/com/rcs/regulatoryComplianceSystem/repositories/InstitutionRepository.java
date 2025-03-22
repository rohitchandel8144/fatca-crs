package com.rcs.regulatoryComplianceSystem.repositories;

import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution,Long> {
    List<Institution> findByStatus(Institution.Status status);
    List<Institution> findByCreatedBy(User user);
    List<Institution> findByApprovedBy(User user);
    List<Institution> findByRejectedBy(User user);
        List<Institution> findByStatusIn(List<Institution.Status> statuses);


    @Query("SELECT EXTRACT(MONTH FROM TO_DATE(i.dateOfIncorporation, 'yyyy-MM-dd')), COUNT(i) " +
            "FROM Institution i " +
            "WHERE EXTRACT(YEAR FROM TO_DATE(i.dateOfIncorporation, 'yyyy-MM-dd')) = :year " +
            "GROUP BY EXTRACT(MONTH FROM TO_DATE(i.dateOfIncorporation, 'yyyy-MM-dd')) " +
            "ORDER BY EXTRACT(MONTH FROM TO_DATE(i.dateOfIncorporation, 'yyyy-MM-dd'))")
    List<Object[]> countInstitutionsRegisteredByRFI(@Param("year") int year);




}
