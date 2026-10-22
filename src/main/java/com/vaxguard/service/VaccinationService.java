package com.vaxguard.service;

import com.vaxguard.model.Beneficiary;
import com.vaxguard.model.VaccinationRecord;
import com.vaxguard.repository.BeneficiaryRepository;
import com.vaxguard.repository.VaccinationRecordRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VaccinationService {

    // Controllable defect flag for Task 10 (Deliberate defect demonstration in CI/CD quality gate)
    public static volatile boolean defectModeEnabled = false;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    @Transactional
    public Beneficiary registerBeneficiary(Beneficiary beneficiary) {
        if (defectModeEnabled) {
            // Deliberate defect: artificially throws exception or corrupts registration when defect mode is active
            throw new IllegalStateException("SIMULATED_DEFECT: Registration service currently locked due to simulated validation bug!");
        }

        // Calculate schedule automatically from Date of Birth
        List<VaccinationRecord> schedule = generateImmunizationSchedule(beneficiary.getDateOfBirth());
        for (VaccinationRecord record : schedule) {
            beneficiary.addVaccinationRecord(record);
        }

        return beneficiaryRepository.save(beneficiary);
    }

    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public Optional<Beneficiary> getBeneficiaryById(Long id) {
        return beneficiaryRepository.findById(id);
    }

    public List<VaccinationRecord> getAllVaccinationRecords() {
        return vaccinationRecordRepository.findAll();
    }

    public List<VaccinationRecord> searchAndFilterRecords(String query, String status) {
        String cleanQuery = (query == null) ? "" : query.trim();
        String cleanStatus = (status == null || status.isBlank() || status.equalsIgnoreCase("ALL")) ? null : status.trim();
        return vaccinationRecordRepository.filterAndSearch(cleanQuery, cleanStatus);
    }

    @Transactional
    public VaccinationRecord updateVaccinationStatus(Long recordId, String status, String administeredBy, String batchNumber, String notes) {
        VaccinationRecord record = vaccinationRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found with ID: " + recordId));

        record.setStatus(status.toUpperCase());
        if ("COMPLETED".equalsIgnoreCase(status) || "ADMINISTERED".equalsIgnoreCase(status)) {
            record.setAdministeredDate(LocalDate.now());
            record.setAdministeredBy((administeredBy != null && !administeredBy.isBlank()) ? administeredBy : "Registered Nurse");
            record.setBatchNumber((batchNumber != null && !batchNumber.isBlank()) ? batchNumber : "LOT-" + System.currentTimeMillis() % 100000);
        }
        if (notes != null) {
            record.setNotes(notes);
        }
        return vaccinationRecordRepository.save(record);
    }

    @Transactional
    public VaccinationRecord triggerReminder(Long recordId) {
        VaccinationRecord record = vaccinationRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found with ID: " + recordId));
        record.setReminderSentAt(LocalDateTime.now());
        return vaccinationRecordRepository.save(record);
    }

    @Transactional
    public int triggerBatchReminders() {
        LocalDate today = LocalDate.now();
        List<VaccinationRecord> dueRecords = vaccinationRecordRepository.findDueOrOverdueVaccinations(today.plusDays(7));
        int count = 0;
        for (VaccinationRecord r : dueRecords) {
            r.setReminderSentAt(LocalDateTime.now());
            vaccinationRecordRepository.save(r);
            count++;
        }
        return count;
    }

    public Map<String, Object> getDashboardStatistics() {
        LocalDate today = LocalDate.now();
        long totalBeneficiaries = beneficiaryRepository.count();
        long totalRecords = vaccinationRecordRepository.count();
        long completedCount = vaccinationRecordRepository.countByStatus("COMPLETED") + vaccinationRecordRepository.countByStatus("ADMINISTERED");
        long scheduledCount = vaccinationRecordRepository.countByStatus("SCHEDULED");
        long overdueCount = vaccinationRecordRepository.countByStatus("OVERDUE");

        // Count upcoming next 7 days
        List<VaccinationRecord> upcoming = vaccinationRecordRepository.findUpcomingVaccinations(today, today.plusDays(7));
        long upcomingCount = upcoming.stream().filter(r -> "SCHEDULED".equalsIgnoreCase(r.getStatus())).count();

        double complianceRate = totalRecords > 0 ? ((double) completedCount / totalRecords) * 100.0 : 0.0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBeneficiaries", totalBeneficiaries);
        stats.put("totalRecords", totalRecords);
        stats.put("completedCount", completedCount);
        stats.put("scheduledCount", scheduledCount);
        stats.put("overdueCount", overdueCount);
        stats.put("upcomingCount", upcomingCount);
        stats.put("complianceRate", Math.round(complianceRate * 10.0) / 10.0);

        return stats;
    }

    private List<VaccinationRecord> generateImmunizationSchedule(LocalDate dob) {
        List<VaccinationRecord> records = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 1. At Birth
        records.add(createRecord("BCG", "Tuberculosis", 1, dob, today));
        records.add(createRecord("HepB-Birth", "Hepatitis B", 0, dob, today));
        records.add(createRecord("OPV-0", "Polio", 0, dob, today));

        // 2. 6 Weeks
        LocalDate sixWeeks = dob.plusWeeks(6);
        records.add(createRecord("OPV-1", "Polio", 1, sixWeeks, today));
        records.add(createRecord("Pentavalent-1", "Diphtheria, Pertussis, Tetanus, HepB, Hib", 1, sixWeeks, today));
        records.add(createRecord("Rotavirus-1", "Rotavirus Diarrhea", 1, sixWeeks, today));
        records.add(createRecord("PCV-1", "Pneumococcal Disease", 1, sixWeeks, today));

        // 3. 10 Weeks
        LocalDate tenWeeks = dob.plusWeeks(10);
        records.add(createRecord("OPV-2", "Polio", 2, tenWeeks, today));
        records.add(createRecord("Pentavalent-2", "Diphtheria, Pertussis, Tetanus, HepB, Hib", 2, tenWeeks, today));
        records.add(createRecord("Rotavirus-2", "Rotavirus Diarrhea", 2, tenWeeks, today));

        // 4. 14 Weeks
        LocalDate fourteenWeeks = dob.plusWeeks(14);
        records.add(createRecord("OPV-3", "Polio", 3, fourteenWeeks, today));
        records.add(createRecord("Pentavalent-3", "Diphtheria, Pertussis, Tetanus, HepB, Hib", 3, fourteenWeeks, today));
        records.add(createRecord("Rotavirus-3", "Rotavirus Diarrhea", 3, fourteenWeeks, today));
        records.add(createRecord("PCV-2", "Pneumococcal Disease", 2, fourteenWeeks, today));

        // 5. 9 Months
        LocalDate nineMonths = dob.plusMonths(9);
        records.add(createRecord("Measles/MR-1", "Measles, Rubella", 1, nineMonths, today));
        records.add(createRecord("PCV-Booster", "Pneumococcal Disease", 3, nineMonths, today));

        // 6. 16-24 Months
        LocalDate sixteenMonths = dob.plusMonths(16);
        records.add(createRecord("DPT-Booster-1", "Diphtheria, Pertussis, Tetanus", 1, sixteenMonths, today));
        records.add(createRecord("MR-2", "Measles, Rubella", 2, sixteenMonths, today));

        return records;
    }

    private VaccinationRecord createRecord(String name, String disease, int dose, LocalDate scheduledDate, LocalDate today) {
        String status = "SCHEDULED";
        if (scheduledDate.isBefore(today.minusDays(7))) {
            status = "OVERDUE";
        }
        return new VaccinationRecord(name, disease, dose, scheduledDate, status);
    }

    @PostConstruct
    public void seedInitialDemoData() {
        if (beneficiaryRepository.count() == 0) {
            LocalDate today = LocalDate.now();

            // Beneficiary 1: Aarav Sharma (Born 10 months ago)
            Beneficiary b1 = new Beneficiary("Aarav Sharma", today.minusMonths(10), "Male", "Pooja Sharma", "+91 98201 12345", "pooja.sharma@example.com", "Plot 42, Sunrise Enclave, Pune");
            registerBeneficiary(b1);

            // Mark initial birth doses as completed
            List<VaccinationRecord> b1Records = vaccinationRecordRepository.findByBeneficiaryId(b1.getId());
            for (VaccinationRecord r : b1Records) {
                if (r.getScheduledDate().isBefore(today.minusMonths(6))) {
                    r.setStatus("COMPLETED");
                    r.setAdministeredDate(r.getScheduledDate());
                    r.setAdministeredBy("Staff Nurse Ananya");
                    r.setBatchNumber("LOT-SERUM-2025");
                    vaccinationRecordRepository.save(r);
                }
            }

            // Beneficiary 2: Ananya Patel (Born 2 months ago)
            Beneficiary b2 = new Beneficiary("Ananya Patel", today.minusWeeks(8), "Female", "Vikram Patel", "+91 97123 45678", "vikram.patel@example.com", "B-204, Green Heights, Mumbai");
            registerBeneficiary(b2);

            // Mark birth doses completed
            List<VaccinationRecord> b2Records = vaccinationRecordRepository.findByBeneficiaryId(b2.getId());
            for (VaccinationRecord r : b2Records) {
                if (r.getScheduledDate().isBefore(today.minusWeeks(4))) {
                    r.setStatus("COMPLETED");
                    r.setAdministeredDate(r.getScheduledDate());
                    r.setAdministeredBy("Dr. Rajesh Kulkarni");
                    r.setBatchNumber("LOT-BHARAT-8841");
                    vaccinationRecordRepository.save(r);
                }
            }

            // Beneficiary 3: Vihaan Deshmukh (Newborn, 1 week ago)
            Beneficiary b3 = new Beneficiary("Vihaan Deshmukh", today.minusWeeks(1), "Male", "Sneha Deshmukh", "+91 98900 67890", "sneha.d@example.com", "15 Shivajinagar, Kolhapur");
            registerBeneficiary(b3);

            // Beneficiary 4: Myra Kulkarni (Born 14 months ago)
            Beneficiary b4 = new Beneficiary("Myra Kulkarni", today.minusMonths(14), "Female", "Rohan Kulkarni", "+91 94220 33445", "rohan.k@example.com", "Rowhouse 7, Park Avenue, Nashik");
            registerBeneficiary(b4);
        }
    }
}
