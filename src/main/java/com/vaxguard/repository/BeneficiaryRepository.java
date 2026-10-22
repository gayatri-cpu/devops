package com.vaxguard.repository;

import com.vaxguard.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByChildNameContainingIgnoreCase(String childName);

    List<Beneficiary> findByContactPhoneContaining(String phone);

    @Query("SELECT b FROM Beneficiary b WHERE " +
           "LOWER(b.childName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.parentName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "b.contactPhone LIKE CONCAT('%', :query, '%')")
    List<Beneficiary> searchByKeyword(@Param("query") String query);
}
