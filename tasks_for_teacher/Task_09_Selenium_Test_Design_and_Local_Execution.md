# Task 09: Selenium Test Design and Local Execution
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 16  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To identify 3 to 5 critical user journeys in the Vaccination Reminder Portal, write automated Selenium WebDriver test cases using JUnit 5 with assertions and test fixtures, implement an automated failure screenshot capture mechanism, and execute the test suite locally through Maven.

---

### 2. Syllabus Deliverable Requirement
> *"Test plan, Selenium scripts, local test report and failure screenshot mechanism."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain the Selenium Test Suite
- **Speech Script:**
  > *"Madam/Sir, in Task 9, I designed end-to-end automated UI regression tests using **Selenium WebDriver 4** and **JUnit 5**. I mapped 5 critical user journeys. All tests are configured to run in headless mode so that they can run reliably inside Linux CI/CD agents without requiring a graphical monitor. I also wrote a custom JUnit extension that captures browser screenshots whenever an assertion fails."*

#### Step 2: Show the 5 User Journey Test Classes
Open folder: [`src/test/java/com/vaxguard/selenium/`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/src/test/java/com/vaxguard/selenium/)  
Walk through the 5 test journeys:

| Journey # | Test Class | Core Verification & Assertions |
| :---: | :--- | :--- |
| **01** | `BeneficiaryRegistrationJourneyTest` | Fills child form with DOB, submits, verifies child record in registry, and confirms 16 vaccination doses were generated automatically. |
| **02** | `SearchAndFilterJourneyTest` | Searches by child name and filters records by status (`COMPLETED`, `SCHEDULED`); asserts returned rows match query. |
| **03** | `StatusWorkflowJourneyTest` | Tests clinical role workflow: nurse enters batch lot #, updates status to `COMPLETED`; asserts Parent View cannot edit records. |
| **04** | `DashboardMetricsJourneyTest` | Verifies numeric KPI cards (Total, Completed, Upcoming, Compliance Rate %) and Chart.js canvas render. |
| **05** | `DefectSimulationJourneyTest` | Quality Gate: simulates defect failure, captures failure screenshot, and proves pass upon bugfix. |

#### Step 3: Show the Failure Screenshot Mechanism
Open file: [`ScreenshotOnFailureExtension.java`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/src/test/java/com/vaxguard/selenium/ScreenshotOnFailureExtension.java)  
Explain how it captures screenshots:
```java
public class ScreenshotOnFailureExtension implements TestWatcher {
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        WebDriver driver = ((BaseSeleniumTest) testInstance).getDriver();
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String fileName = String.format("%s_%s_FAILURE_%s.png", className, methodName, timestamp);
        Files.copy(screenshot.toPath(), Paths.get("target/screenshots").resolve(fileName));
    }
}
```

#### Step 4: Run the Maven Test Compile Verification
Run in PowerShell:
```powershell
.\mvnw.cmd test-compile
```
Show: `BUILD SUCCESS` with all 8 test source files compiled cleanly.

---

### 4. Likely Viva Voce Questions & Answers for Task 9

**Q1: What is Headless Browser Testing?**  
*Answer:* A headless browser executes web pages without displaying a graphical user interface (GUI). It runs entirely in background memory. In DevOps CI/CD pipelines (like Jenkins running on Linux servers without a desktop environment), headless mode is mandatory for running browser automation.

**Q2: What is the difference between Implicit Wait and Explicit Wait in Selenium?**  
*Answer:* Implicit Wait tells WebDriver to poll the DOM for a specified period (e.g., 5 seconds) when looking for any element. Explicit Wait (`WebDriverWait` and `ExpectedConditions`) pauses execution until a specific condition is met (e.g., element becomes clickable or visible) before proceeding, avoiding arbitrary thread sleeps.

**Q3: Why use Page Object Model (POM) or Base Test classes?**  
*Answer:* In our suite, `BaseSeleniumTest` abstracts browser setup (auto-detecting Chrome or Edge, configuring headless mode), server port injection, and teardown logic. This keeps individual test classes clean, reusable, and maintainable.
