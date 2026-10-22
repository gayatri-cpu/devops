package com.vaxguard.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Journey 2: Search and Status Filter Vaccination Records")
public class SearchAndFilterJourneyTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Should filter records by status and search by child or vaccine name")
    public void testSearchAndFilterRecords() throws InterruptedException {
        driver.get(getBaseUrl());

        // Navigate to schedule tab
        click(By.id("btn-nav-schedule"));
        Thread.sleep(800);

        // 1. Verify initial records load
        List<WebElement> initialRows = driver.findElements(By.cssSelector("#records-table-body tr"));
        assertTrue(initialRows.size() > 0, "Table should have initial records loaded");

        // 2. Filter by COMPLETED status
        WebElement filterSelectEl = waitForVisible(By.id("status-filter"));
        Select statusSelect = new Select(filterSelectEl);
        statusSelect.selectByValue("COMPLETED");
        Thread.sleep(800);

        List<WebElement> completedRows = driver.findElements(By.cssSelector("#records-table-body tr"));
        for (WebElement row : completedRows) {
            if (!row.getText().contains("No vaccination records")) {
                assertTrue(row.getText().contains("COMPLETED"), "Filtered rows should all have COMPLETED status");
            }
        }

        // 3. Search for specific child name "Aarav"
        statusSelect.selectByValue("ALL");
        type(By.id("search-input"), "Aarav");
        Thread.sleep(800);

        List<WebElement> searchRows = driver.findElements(By.cssSelector("#records-table-body tr"));
        for (WebElement row : searchRows) {
            assertTrue(row.getText().contains("Aarav"), "All search result rows should belong to Aarav");
        }
    }
}
