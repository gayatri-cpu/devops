package com.vaxguard.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Journey 1: Beneficiary Registration & Immunization Schedule Generation")
public class BeneficiaryRegistrationJourneyTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Should successfully register a new child beneficiary and generate vaccination schedule")
    public void testBeneficiaryRegistrationFlow() throws InterruptedException {
        driver.get(getBaseUrl());

        // Verify page title and header
        assertTrue(driver.getTitle().contains("VaxGuard"), "Page title should contain VaxGuard");
        assertEquals("VaxGuard", getText(By.className("brand-title")));

        // 1. Open registration modal
        click(By.id("btn-open-register-modal"));

        // 2. Fill registration form
        String childName = "Baby Kiara Patil " + System.currentTimeMillis() % 10000;
        String dob = LocalDate.now().minusWeeks(5).toString();

        type(By.id("childName"), childName);
        type(By.id("dateOfBirth"), dob);
        type(By.id("parentName"), "Sunita Patil");
        type(By.id("contactPhone"), "+91 99887 11223");
        type(By.id("contactEmail"), "sunita.patil@example.com");
        type(By.id("address"), "Flat 502, Lotus Residency, Pune");

        // 3. Submit registration form
        click(By.id("btn-submit-registration"));

        // 4. Verify toast notification appears
        WebElement toast = waitForVisible(By.className("toast"));
        assertTrue(toast.getText().contains("registered with automated schedule") || toast.getText().contains("Success"),
                "Success toast message should be displayed");

        // 5. Navigate to Child Beneficiaries Tab and verify new beneficiary is present
        click(By.id("btn-nav-beneficiaries"));
        Thread.sleep(1000);

        List<WebElement> rows = driver.findElements(By.cssSelector("#beneficiaries-table-body tr"));
        boolean foundChild = false;
        for (WebElement r : rows) {
            if (r.getText().contains(childName)) {
                foundChild = true;
                break;
            }
        }
        assertTrue(foundChild, "Registered child should appear in the beneficiaries registry table");

        // 6. Navigate to Vaccination Records Tab and verify schedule was auto-generated
        click(By.id("btn-nav-schedule"));
        type(By.id("search-input"), childName);

        wait.until(d -> {
            try {
                List<WebElement> tblRows = d.findElements(By.cssSelector("#records-table-body tr"));
                return !tblRows.isEmpty() && tblRows.stream().anyMatch(r -> {
                    try {
                        return r.getText().contains(childName);
                    } catch (org.openqa.selenium.StaleElementReferenceException e) {
                        return false;
                    }
                });
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                return false;
            }
        });

        List<WebElement> recordRows = driver.findElements(By.cssSelector("#records-table-body tr"));
        assertFalse(recordRows.isEmpty(), "Auto-generated vaccination records should exist for the child");
        assertTrue(recordRows.stream().anyMatch(r -> r.getText().contains(childName)), "Record rows should match registered child");
    }
}
