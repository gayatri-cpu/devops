package com.vaxguard.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Journey 4: Real-Time Summary Dashboard & Analytics Verification")
public class DashboardMetricsJourneyTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Should display valid KPI counts and interactive chart on dashboard")
    public void testDashboardAnalytics() throws InterruptedException {
        driver.get(getBaseUrl());

        // Wait for stats to load
        Thread.sleep(1000);

        // 1. Verify KPI elements exist and have non-empty numeric contents
        WebElement totalBeneficiaries = waitForVisible(By.id("stat-total-beneficiaries"));
        WebElement completedVaccines = waitForVisible(By.id("stat-completed-vaccines"));
        WebElement upcomingVaccines = waitForVisible(By.id("stat-upcoming-vaccines"));
        WebElement overdueVaccines = waitForVisible(By.id("stat-overdue-vaccines"));
        WebElement complianceRate = waitForVisible(By.id("stat-compliance-rate"));

        int total = Integer.parseInt(totalBeneficiaries.getText().trim());
        assertTrue(total >= 0, "Total registered beneficiaries count should be non-negative");

        int completed = Integer.parseInt(completedVaccines.getText().trim());
        assertTrue(completed >= 0, "Completed vaccines count should be non-negative");

        double compliance = Double.parseDouble(complianceRate.getText().trim());
        assertTrue(compliance >= 0.0 && compliance <= 100.0, "Compliance rate should be between 0 and 100 percent");

        // 2. Verify Chart.js canvas is rendered
        WebElement chartCanvas = waitForVisible(By.id("vaccineChart"));
        assertTrue(chartCanvas.isDisplayed(), "Vaccination analytics chart canvas should be rendered and visible");

        // 3. Test Proactive Reminder Simulator Button
        click(By.id("btn-batch-reminders"));
        WebElement toast = waitForVisible(By.className("toast"));
        assertTrue(toast.getText().contains("Batch SMS broadcast") || toast.getText().contains("parents"),
                "Dispatching batch reminders should trigger confirmation toast");
    }
}
