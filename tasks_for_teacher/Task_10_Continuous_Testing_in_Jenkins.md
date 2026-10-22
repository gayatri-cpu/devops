# Task 10: Continuous Testing in Jenkins (Quality Gate & Defect Remediation)
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 16  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To integrate the Vaccination Reminder Portal Selenium test suite into the Jenkins pipeline as an automated Quality Gate, publish JUnit test reports, configure the pipeline so failed tests halt packaging and deployment, deliberately introduce a software defect, demonstrate build failure and screenshot capture, fix the defect through a code commit, and rerun the pipeline successfully.

---

### 2. Syllabus Deliverable Requirement
> *"Jenkins test report, failed pipeline evidence, defect correction commit and successful rerun."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain the Quality Gate Concept
- **Speech Script:**
  > *"Madam/Sir, Task 10 demonstrates our automated Quality Gate in Jenkins. The syllabus requires demonstrating that a deliberately introduced defect causes the pipeline to fail and capture a screenshot, and then showing that fixing the defect allows the pipeline to pass. I created an intentional defect toggle in the registration engine to prove this exact quality gate behavior."*

#### Step 2: Show the Defect Simulation Code
Open file: [`DefectSimulationJourneyTest.java`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/src/test/java/com/vaxguard/selenium/DefectSimulationJourneyTest.java)  
Explain how the test works:
- **Phase 1 (Defect Introduced):** When `defectModeEnabled = true`, child registration throws an intentional HTTP 400 validation error. The Selenium assertion fails because the expected success toast does not appear.
- **Phase 2 (Screenshot Captured):** The `ScreenshotOnFailureExtension` captures a PNG snapshot of the broken UI into `target/screenshots/`.
- **Phase 3 (Pipeline Aborted):** Jenkins skips the Package, Docker Build, and Deploy stages, protecting production from being contaminated.
- **Phase 4 (Remediation Commit):** Resetting `defectModeEnabled = false` simulates the bugfix commit. The test re-runs and passes cleanly.

#### Step 3: Show the UI Defect Toggle in the Live Portal
1. Open in browser: `http://localhost:8080`
2. Click the navigation tab: **"🚀 DevOps Pipeline & Quality Gate"**
3. Point out the interactive card:
   - *"Task 10: Intentional Defect Injection & Quality Gate Demonstration"*
   - Checkbox: `[ ] Enable Simulated Defect`
   - Real-time status badge: `Status: System Normal (Defect Disabled - All Quality Gates Pass)`

#### Step 4: Show the Failed Build Evidence Log in Documentation
Open [`docs/TASK-10-Continuous-Testing-Jenkins-Quality-Gate.md`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docs/TASK-10-Continuous-Testing-Jenkins-Quality-Gate.md) and point out:
- Failure output: `[ERROR] AssertionFailedError: Success toast message should be displayed ==> expected: <true> but was: <false>`
- Screenshot capture notice: `[SELENIUM QUALITY GATE] Test failure screenshot captured: target/screenshots/BeneficiaryRegistrationJourneyTest_FAILURE.png`
- Skipped downstream stages: `stage (5. Package) - SKIPPED`, `stage (6. Docker) - SKIPPED`, `stage (7. Deploy) - SKIPPED`.
- Successful rerun log showing `Finished: SUCCESS`.

---

### 4. Likely Viva Voce Questions & Answers for Task 10

**Q1: What is a Quality Gate in a CI/CD pipeline?**  
*Answer:* A Quality Gate is an automated enforcement checkpoint. In our pipeline, it ensures that code can only progress to packaging and deployment if 100% of the automated Selenium tests pass without failures or errors.

**Q2: What happens if a test fails in Jenkins?**  
*Answer:* The Maven Surefire plugin records the test failure in an XML report, the failure screenshot listener saves a browser screenshot, and Jenkins marks the stage as FAILED. Using Jenkins pipeline logic, the pipeline halts immediately, preventing defective code from reaching staging or production.

**Q3: How does automated screenshot capture assist developers?**  
*Answer:* Unlike text logs or stack traces, a screenshot provides immediate visual evidence of the exact state of the web page at the moment of failure (e.g., unexpected modal error, missing button, or broken CSS layout), drastically reducing debugging time.
