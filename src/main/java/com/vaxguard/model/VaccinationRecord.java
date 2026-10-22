package com.vaxguard.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vaccination_records")
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    @JsonBackReference
    private Beneficiary beneficiary;

    @Column(nullable = false)
    private String vaccineName;

    private String targetDisease;

    private int doseNumber;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    private LocalDate administeredDate;

    @Column(nullable = false)
    private String status; // SCHEDULED, PENDING, COMPLETED, OVERDUE, CANCELLED

    private String administeredBy;

    private String batchNumber;

    @Column(length = 1000)
    private String notes;

    private LocalDateTime reminderSentAt;

    public VaccinationRecord() {
    }

    public VaccinationRecord(String vaccineName, String targetDisease, int doseNumber, LocalDate scheduledDate, String status) {
        this.vaccineName = vaccineName;
        this.targetDisease = targetDisease;
        this.doseNumber = doseNumber;
        this.scheduledDate = scheduledDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getTargetDisease() {
        return targetDisease;
    }

    public void setTargetDisease(String targetDisease) {
        this.targetDisease = targetDisease;
    }

    public int getDoseNumber() {
        return doseNumber;
    }

    public void setDoseNumber(int doseNumber) {
        this.doseNumber = doseNumber;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDate getAdministeredDate() {
        return administeredDate;
    }

    public void setAdministeredDate(LocalDate administeredDate) {
        this.administeredDate = administeredDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdministeredBy() {
        return administeredBy;
    }

    public void setAdministeredBy(String administeredBy) {
        this.administeredBy = administeredBy;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getReminderSentAt() {
        return reminderSentAt;
    }

    public void setReminderSentAt(LocalDateTime reminderSentAt) {
        this.reminderSentAt = reminderSentAt;
    }
}
