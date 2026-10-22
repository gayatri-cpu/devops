# TASK 09: Selenium Test Design and Local Execution

**Student:** Gayatri Patil  
**PRN / Roll No:** 23102A0016 / Roll 15  
**Project:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  
**Deliverable:** Test plan, Selenium test scripts catalog, local test execution report, and failure screenshot mechanism.

---

## 1. Selenium Automated Test Plan

### 1.1 Objective
Design and execute automated end-to-end (E2E) browser regression tests using **Selenium WebDriver 4** and **JUnit 5** to validate that critical user journeys function properly before code reaches deployment.

### 1.2 Five Critical User Journeys

| Journey # | Test Class | User Journey Objective | Key Assertions |
| :---: | :--- | :--- | :--- |
| **01** | `BeneficiaryRegistrationJourneyTest` | Register new infant with DOB; verify schedule auto-generation. | Success toast displayed; child exists in registry; schedule records created. |
| **02** | `SearchAndFilterJourneyTest` | Search by child name and filter records by status (`COMPLETED`). | Returned rows only match search query and selected status badge. |
| **03** | `StatusWorkflowJourneyTest` | Clinical nurse administers vaccine dose and records lot #. | Status transitions to `COMPLETED`; batch number logged; Parent view restricts action. |
| **04** | `DashboardMetricsJourneyTest` | Verify real-time summary KPI cards and Chart.js canvas render. | KPI values numeric; Chart canvas displayed; batch reminder button fires. |
| **05** | `DefectSimulationJourneyTest` | Quality Gate: detect intentional defect, fail test, and re-pass when resolved. | Defect generates error toast; resolution restores baseline pass. |

---

## 2. Headless Browser & Cross-Browser Architecture

In `BaseSeleniumTest.java`:
- Leverages `WebDriverManager` to automatically download browser driver binaries.
- Auto-detects Google Chrome on host; falls back to Chromium Microsoft Edge (`msedge.exe`).
- Enables headless execution when running in automated pipelines:
  ```java
  ChromeOptions options = new ChromeOptions();
  options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
  ```
- Starts Spring Boot on dynamic port (`@LocalServerPort`) to prevent port collision conflicts.

---

## 3. Automated Failure Screenshot Mechanism

Defined in `ScreenshotOnFailureExtension.java`:
- Implements JUnit 5 `TestWatcher.testFailed()`.
- Whenever an assertion fails or an unexpected exception occurs, it captures a PNG snapshot of the browser DOM state:
  ```java
  File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
  String fileName = String.format("%s_%s_FAILURE_%s.png", className, methodName, timestamp);
  Files.copy(screenshot.toPath(), Paths.get("target/screenshots").resolve(fileName));
  ```
- Captured screenshots are preserved in `target/screenshots/` and archived by Jenkins as build artifacts.

---

## 4. Local Test Execution Log via Maven

Command:
```powershell
.\mvnw.cmd test
```

Output:
```text
[INFO] Scanning for projects...
[INFO] 
[INFO] --------------< com.vaxguard:vaccination-reminder-portal >--------------
[INFO] Building vaccination-reminder-portal 1.0.0
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ vaccination-reminder-portal ---
[INFO] Using auto detected provider: org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.vaxguard.VaxGuardApplicationTests
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.14 s
[INFO] Running com.vaxguard.selenium.BeneficiaryRegistrationJourneyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.21 s
[INFO] Running com.vaxguard.selenium.SearchAndFilterJourneyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.65 s
[INFO] Running com.vaxguard.selenium.StatusWorkflowJourneyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.12 s
[INFO] Running com.vaxguard.selenium.DashboardMetricsJourneyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.91 s
[INFO] Running com.vaxguard.selenium.DefectSimulationJourneyTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.44 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] Total time:  28.910 s
[INFO] Finished at: 2026-10-22T14:24:30+05:30
[INFO] ------------------------------------------------------------------------
```
Surefire XML Test Reports generated in `target/surefire-reports/`.
