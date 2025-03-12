package com.rcs.regulatoryComplianceSystem.repositories;

import com.rcs.regulatoryComplianceSystem.entity.Institution;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution,Long> {
    List<Institution> findByStatus(Institution.Status status);


    @Query("SELECT EXTRACT(MONTH FROM TO_DATE(i.dateOfIncorporation, 'yyyy-MM-dd')), COUNT(i) " +
            "FROM Institution i " +
            "WHERE i.createdBy.role.roleType = com.rcs.regulatoryComplianceSystem.entity.Role.RoleType.RFI " +
            "AND EXTRACT(YEAR FROM TO_DATE(i.dateOfIncorporation, 'yyyy-MM-dd')) = :year " +
            "GROUP BY EXTRACT(MONTH FROM TO_DATE(i.dateOfIncorporation, 'yyyy-MM-dd')) " +
            "ORDER BY EXTRACT(MONTH FROM TO_DATE(i.dateOfIncorporation, 'yyyy-MM-dd'))")
    List<Object[]>countInstitutionsRegisteredByRFI(@Param("year") Integer year);


}
