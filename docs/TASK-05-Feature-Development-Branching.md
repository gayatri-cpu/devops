# TASK 05: Feature Development with Branching

**Student:** Gayatri Patil  
**PRN / Roll No:** 23102A0016 / Roll 16  
**Project:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  
**Deliverable:** Working Feature 1, feature branch logs, pull request evidence, review comments, and merge evidence.

---

## 1. Feature 1 Specification: Core Immunization Schedule Calculation

### Objective
Implement the first core workflow: Child Beneficiary Registration and automated computation of the comprehensive national immunization schedule based on Date of Birth.

### Technical Implementation
- Domain Entity: `com.vaxguard.model.Beneficiary` & `VaccinationRecord`
- Service Logic: `com.vaxguard.service.VaccinationService.generateImmunizationSchedule(LocalDate dob)`
- REST Endpoint: `POST /api/beneficiaries`
- Milestone Schedule Generated:
  - **At Birth:** BCG (Tuberculosis), Hepatitis B (Birth), OPV-0 (Polio).
  - **6 Weeks:** OPV-1, Pentavalent-1 (DPT+HepB+Hib), Rotavirus-1, PCV-1.
  - **10 Weeks:** OPV-2, Pentavalent-2, Rotavirus-2.
  - **14 Weeks:** OPV-3, Pentavalent-3, Rotavirus-3, PCV-2.
  - **9 Months:** Measles/MR-1, PCV-Booster.
  - **16-24 Months:** DPT-Booster-1, MR-2.

---

## 2. Git Branching Execution Log

### 2.1 Branch Creation and Commits
```bash
# Branch out from develop
git checkout develop
git checkout -b feature/vaccination-records

# Add and commit service layer changes
git add src/main/java/com/vaxguard/service/ src/main/java/com/vaxguard/controller/
git commit -m "feat(workflow): implement national immunization schedule calculation and beneficiary registration"
```

### 2.2 Feature Verification Evidence
Local verification via Maven test:
```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.vaxguard.selenium.BeneficiaryRegistrationJourneyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.82 s -- in com.vaxguard.selenium.BeneficiaryRegistrationJourneyTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## 3. Pull Request #1 Artifact

**Title:** `[PR #1] feat(workflow): National Immunization Schedule & Child Registration Engine`  
**Source Branch:** `feature/vaccination-records`  
**Target Branch:** `develop`  
**Author:** Gayatri Patil (`gayatri-patil`)  

### Description
> This PR introduces the core schedule generation engine based on the national immunization guideline. When a parent or health worker registers a child with DOB, the engine calculates 16 mandatory vaccine doses across birth, 6 weeks, 10 weeks, 14 weeks, 9 months, and 16 months.

### Peer Review Comments
- **Reviewer (Tech Lead):** "Code clean, schedule intervals accurately mapped to calendar days. Please confirm whether overdue calculations take the current date into account."
- **Author (Gayatri Patil):** "Confirmed: any scheduled dose with a calendar date prior to `LocalDate.now().minusDays(7)` automatically receives status `OVERDUE`."
- **Status:** **APPROVED & MERGED**

### Merge Log Output
```bash
git checkout develop
git merge --no-ff feature/vaccination-records -m "Merge pull request #1 from feature/vaccination-records into develop"
```
Commit SHA: `8f1233f`
