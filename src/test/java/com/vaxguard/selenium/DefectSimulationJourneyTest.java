package com.vaxguard.selenium;

import com.vaxguard.service.VaccinationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Journey 5 / Task 10: CI/CD Quality Gate & Defect Injection Demonstration")
public class DefectSimulationJourneyTest extends BaseSeleniumTest {

    @AfterEach
    public void resetDefectFlag() {
        // Guarantee system returns to stable state
        VaccinationService.defectModeEnabled = false;
    }

    @Test
    @DisplayName("Quality Gate Verification: System passes under normal baseline conditions")
    public void testHealthyBaselinePassesQualityGate() {
        VaccinationService.defectModeEnabled = false;
        driver.get(getBaseUrl());

        click(By.id("btn-open-register-modal"));
        type(By.id("childName"), "Baseline Healthy Baby " + System.currentTimeMillis() % 1000);
        type(By.id("dateOfBirth"), LocalDate.now().minusWeeks(2).toString());
        type(By.id("parentName"), "Pooja Patil");
        type(By.id("contactPhone"), "+91 91234 56789");

        click(By.id("btn-submit-registration"));

        WebElement toast = waitForVisible(By.className("toast"));
        assertTrue(toast.getText().contains("registered with automated schedule") || toast.getText().contains("Success"),
                "Normal submission must pass the quality gate");
    }

    @Test
    @DisplayName("Task 10 Defect Injection: Simulates failure catching mechanism and defect resolution")
    public void testDefectInjectionAndResolutionLifecycle() throws InterruptedException {
        // Step 1: Simulate the defect introduction
        VaccinationService.defectModeEnabled = true;
        driver.get(getBaseUrl());

        click(By.id("btn-open-register-modal"));
        type(By.id("childName"), "Defect Candidate Baby");
        type(By.id("dateOfBirth"), LocalDate.now().minusWeeks(3).toString());
        type(By.id("parentName"), "Test Parent");
        type(By.id("contactPhone"), "+91 99999 00000");

        click(By.id("btn-submit-registration"));

        // Verify that UI displays error toast (simulating defect caught by quality gate)
        WebElement errorToast = waitForVisible(By.className("toast"));
        assertTrue(errorToast.getText().contains("Error") || errorToast.getText().contains("SIMULATED_DEFECT"),
                "Defect must be detected by the quality gate");

        // Step 2: Simulate defect resolution (Git commit fix)
        VaccinationService.defectModeEnabled = false;
        Thread.sleep(1000);

        // Retry registration with resolved code
        click(By.id("btn-submit-registration"));
        wait.until(d -> d.findElements(By.className("toast")).stream()
                .anyMatch(t -> t.getText().contains("Success") || t.getText().contains("registered")));
    }
}
