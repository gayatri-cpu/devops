package com.vaxguard.controller;

import com.vaxguard.model.Beneficiary;
import com.vaxguard.model.VaccinationRecord;
import com.vaxguard.service.VaccinationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VaccinationApiController {

    @Autowired
    private VaccinationService vaccinationService;

    // 1. Beneficiary Endpoints
    @GetMapping("/beneficiaries")
    public List<Beneficiary> getAllBeneficiaries() {
        return vaccinationService.getAllBeneficiaries();
    }

    @PostMapping("/beneficiaries")
    public ResponseEntity<?> registerBeneficiary(@Valid @RequestBody Beneficiary beneficiary) {
        try {
            Beneficiary created = vaccinationService.registerBeneficiary(beneficiary);
            return ResponseEntity.ok(created);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/beneficiaries/{id}")
    public ResponseEntity<Beneficiary> getBeneficiary(@PathVariable Long id) {
        return vaccinationService.getBeneficiaryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 2. Vaccination Records Endpoints
    @GetMapping("/vaccinations")
    public List<VaccinationRecord> getVaccinations(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        return vaccinationService.searchAndFilterRecords(search, status);
    }

    @PutMapping("/vaccinations/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        String administeredBy = payload.get("administeredBy");
        String batchNumber = payload.get("batchNumber");
        String notes = payload.get("notes");

        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Status is required"));
        }

        try {
            VaccinationRecord updated = vaccinationService.updateVaccinationStatus(id, status, administeredBy, batchNumber, notes);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. Reminders
    @PostMapping("/vaccinations/{id}/reminder")
    public ResponseEntity<VaccinationRecord> sendSingleReminder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(vaccinationService.triggerReminder(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/reminders/send-batch")
    public ResponseEntity<Map<String, Object>> sendBatchReminders() {
        int dispatched = vaccinationService.triggerBatchReminders();
        Map<String, Object> res = new HashMap<>();
        res.put("dispatchedCount", dispatched);
        res.put("message", "Simulated batch reminders sent to " + dispatched + " parents via SMS & Email.");
        return ResponseEntity.ok(res);
    }

    // 4. Dashboard Statistics
    @GetMapping("/dashboard/stats")
    public Map<String, Object> getDashboardStats() {
        return vaccinationService.getDashboardStatistics();
    }

    // 5. Defect Simulation Controller (For Task 10 CI/CD Quality Gate Demonstration)
    @GetMapping("/admin/defect-mode")
    public Map<String, Object> getDefectStatus() {
        return Map.of("defectModeEnabled", VaccinationService.defectModeEnabled);
    }

    @PostMapping("/admin/defect-mode")
    public Map<String, Object> toggleDefectMode(@RequestBody Map<String, Boolean> body) {
        Boolean enabled = body.getOrDefault("enabled", false);
        VaccinationService.defectModeEnabled = enabled;
        return Map.of(
            "defectModeEnabled", VaccinationService.defectModeEnabled,
            "message", enabled ? "DEFECT ACTIVATED: Child registration service will fail intentional validation" : "DEFECT RESOLVED: System operational"
        );
    }
}
