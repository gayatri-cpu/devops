# Task 05: Feature Development with Branching
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 15  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To implement the first core workflow of the Vaccination Reminder Portal (child registration and national immunization schedule calculation) on a dedicated feature branch, use standard Git commands (`add`, `commit`, `push`, `pull`, `log`), raise a pull request with code review comments, and merge into the development branch using non-fast-forward merge.

---

### 2. Syllabus Deliverable Requirement
> *"Working feature 1, feature branch, pull request, review comments and merge evidence."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain Feature 1 to Teacher
- **Speech Script:**
  > *"Madam/Sir, in Task 5, I implemented our first core workflow on a dedicated branch named `feature/vaccination-records`. This feature automatically generates the full national immunization calendar when an infant is registered. I simulated raising Pull Request #1, verified the code through peer review comments, and merged it into `develop` using `git merge --no-ff`."*

#### Step 2: Show the Code Implementation
Open file: [`VaccinationService.java`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/src/main/java/com/vaxguard/service/VaccinationService.java)  
Point to the immunization calculation method:
```java
private List<VaccinationRecord> generateImmunizationSchedule(LocalDate dob) {
    List<VaccinationRecord> records = new ArrayList<>();
    LocalDate today = LocalDate.now();

    // 1. At Birth
    records.add(createRecord("BCG", "Tuberculosis", 1, dob, today));
    records.add(createRecord("HepB-Birth", "Hepatitis B", 0, dob, today));
    records.add(createRecord("OPV-0", "Polio", 0, dob, today));

    // 2. 6 Weeks
    LocalDate sixWeeks = dob.plusWeeks(6);
    records.add(createRecord("OPV-1", "Polio", 1, sixWeeks, today));
    records.add(createRecord("Pentavalent-1", "Diphtheria, Pertussis, Tetanus, HepB, Hib", 1, sixWeeks, today));
    records.add(createRecord("Rotavirus-1", "Rotavirus Diarrhea", 1, sixWeeks, today));
    records.add(createRecord("PCV-1", "Pneumococcal Disease", 1, sixWeeks, today));
    // ... 10 weeks, 14 weeks, 9 months, 16 months
    return records;
}
```

#### Step 3: Run the Git Branch & Merge Evidence Command
Run this in PowerShell:
```powershell
git log --oneline --grep="feature/vaccination-records" -n 2
```

#### Step 4: What Output to Point To:
Show the commit log on screen:
```text
8f1233f Merge pull request #1 from feature/vaccination-records into develop
148f07f feat(workflow): implement national immunization schedule calculation and beneficiary registration
```

#### Step 5: Show Pull Request #1 Details
Show document: [`docs/TASK-05-Feature-Development-Branching.md`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docs/TASK-05-Feature-Development-Branching.md)  
Point to:
- **Title:** `[PR #1] feat(workflow): National Immunization Schedule & Child Registration Engine`
- **Reviewer Comment:** *"Confirmed: any scheduled dose with a calendar date prior to today minus 7 days automatically receives status OVERDUE."*
- **Status:** Approved and Merged into `develop`.

---

### 4. Likely Viva Voce Questions & Answers for Task 5

**Q1: What does `git merge --no-ff` do?**  
*Answer:* The `--no-ff` (no fast-forward) flag forces Git to create a dedicated merge commit, even if the target branch could have fast-forwarded. This preserves the historical record of the feature branch and pull request in the Git graph.

**Q2: How does the application determine if a vaccination is OVERDUE?**  
*Answer:* When computing the schedule, the engine compares the vaccine's milestone date against `LocalDate.now().minusDays(7)`. If the due date is in the past by more than 7 days, its status is set to `OVERDUE`.

**Q3: Why use Pull Requests in Agile/DevOps teams?**  
*Answer:* Pull Requests facilitate mandatory peer code review, automated CI testing, discussion on architectural choices, and documentation before code is merged into the shared codebase.
