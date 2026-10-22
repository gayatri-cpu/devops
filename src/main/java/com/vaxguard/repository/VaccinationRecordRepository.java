package com.vaxguard.repository;

import com.vaxguard.model.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {

    List<VaccinationRecord> findByStatus(String status);

    List<VaccinationRecord> findByBeneficiaryId(Long beneficiaryId);

    @Query("SELECT r FROM VaccinationRecord r WHERE r.scheduledDate <= :targetDate AND r.status = 'SCHEDULED'")
    List<VaccinationRecord> findDueOrOverdueVaccinations(@Param("targetDate") LocalDate targetDate);

    @Query("SELECT r FROM VaccinationRecord r WHERE r.scheduledDate BETWEEN :startDate AND :endDate")
    List<VaccinationRecord> findUpcomingVaccinations(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM VaccinationRecord r WHERE " +
           "(:status IS NULL OR r.status = :status) AND (" +
           "LOWER(r.vaccineName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.beneficiary.childName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "r.beneficiary.contactPhone LIKE CONCAT('%', :keyword, '%'))")
    List<VaccinationRecord> filterAndSearch(@Param("keyword") String keyword, @Param("status") String status);

    long countByStatus(String status);
}
