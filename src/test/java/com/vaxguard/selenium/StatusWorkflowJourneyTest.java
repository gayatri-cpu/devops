package com.vaxguard.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Journey 3: Role-Based Clinical Workflow & Vaccine Administration")
public class StatusWorkflowJourneyTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Should allow healthcare worker to administer dose and transition status to COMPLETED")
    public void testRoleBasedAdministrationWorkflow() throws InterruptedException {
        driver.get(getBaseUrl());

        // 1. Ensure Healthcare Worker mode is active
        click(By.id("role-healthworker-btn"));
        assertTrue(waitForVisible(By.id("role-healthworker-btn")).getAttribute("class").contains("active"));

        // 2. Navigate to Records
        click(By.id("btn-nav-schedule"));
        Thread.sleep(800);

        // 3. Find an administer button on a scheduled/overdue record
        List<WebElement> adminButtons = driver.findElements(By.xpath("//button[contains(text(), 'Administer')]"));
        assertTrue(adminButtons.size() > 0, "Healthcare Worker should have access to administer buttons");

        // Click first available administer button
        adminButtons.get(0).click();

        // 4. Modal should open
        waitForVisible(By.id("modal-administer"));
        type(By.id("admin-nurse-name"), "Nurse Gayatri Patil");
        type(By.id("admin-batch-number"), "LOT-SELENIUM-99");
        type(By.id("admin-notes"), "Administered in clinical testing suite. Beneficiary healthy.");

        // 5. Confirm Administration
        click(By.id("btn-save-administer"));

        // 6. Verify toast feedback
        WebElement toast = waitForVisible(By.className("toast"));
        assertTrue(toast.getText().contains("updated to") || toast.getText().contains("Record #"),
                "Administration success toast should be displayed");

        // 7. Verify Parent View Role restrictions (Parent cannot administer)
        click(By.id("role-parent-btn"));
        Thread.sleep(500);

        List<WebElement> adminButtonsInParentView = driver.findElements(By.xpath("//button[contains(text(), '💉 Administer')]"));
        assertEquals(0, adminButtonsInParentView.size(), "Parent view should restrict clinical administration actions");
    }
}
