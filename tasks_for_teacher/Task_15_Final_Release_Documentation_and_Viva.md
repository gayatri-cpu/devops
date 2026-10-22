# Task 15: Final Release, Complete Documentation, and Viva Voce
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 15  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To execute the complete end-to-end Vaccination Reminder Portal DevOps workflow (from Git commit to Jenkins build, Selenium automated testing quality gate, Docker containerization, and Ansible server provisioning), assemble complete technical documentation, architecture diagrams, and troubleshooting guides, identify limitations and future enhancements, and successfully defend the project in the Viva Voce examination.

---

### 2. Syllabus Deliverable Requirement
> *"Final repository, live demonstration, complete report, screenshots/video, presentation and viva."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain the Final Release to Teacher
- **Speech Script:**
  > *"Madam/Sir, Task 15 represents the culmination of all 15 stages into our release baseline `v1.0.0`. I have verified the complete pipeline from Git commit, through Maven build, Selenium quality gate, Docker containerization, and Ansible server provisioning. All 15 individual deliverables are documented in our repository. Now I would like to present a live 2-minute demonstration of VaxGuard."*

#### Step 2: Perform the 2-Minute Live Demo (on `http://localhost:8080`)
1. **Show the Summary Dashboard:**
   - Point to the KPI counters: *Registered Children (4), Doses Administered (17), Upcoming (7 Days), Overdue Doses (22), Compliance Rate (23.6%)*.
   - Point to the interactive **Chart.js** doughnut chart breaking down completed, scheduled, and overdue doses.
2. **Demonstrate Child Registration & Automated Schedule Engine:**
   - Click the blue button: `[➕ Register Beneficiary]`.
   - Fill details: Child Name *"Vihaan Patil"*, DOB *today minus 8 weeks*, Gender *Male*, Parent Name *Rohan Patil*, Phone *+91 98200 12345*.
   - Click `[Save & Generate Schedule]`. Show the success toast!
3. **Show Vaccination Records Table:**
   - Click **"Vaccination Records"** tab.
   - Search *"Vihaan Patil"*. Show how all 16 vaccine doses (BCG, HepB, OPV-0, OPV-1, Pentavalent-1, Rotavirus-1, PCV-1, MR, DPT) were automatically calculated!
4. **Demonstrate Role-Based Clinical Administration:**
   - Click `[💉 Administer]` on a scheduled dose.
   - Enter administering nurse name (*Staff Nurse Gayatri Patil*) and batch lot number (*LOT-2026-SERUM*).
   - Click Confirm. Show how the status badge changes to green **COMPLETED** with clinical administration metadata recorded!
   - Switch to **Parent View** role: show how the administration buttons are hidden for parents.
5. **Demonstrate Smart Reminder Simulator:**
   - Click `[🔔 Dispatch Due Reminders]`. Show the simulated SMS and Email broadcast dispatched to parents!

#### Step 3: Show the Master README and Documentation Dossiers
Open file: [`README.md`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/README.md)  
Point to the 15-Deliverable Matrix showing all 15 tasks complete and linked.

---

### 4. Comprehensive Final Viva Voce Questions & Answers

**Q1: What is the primary difference between Unit Testing and Selenium WebDriver Testing in DevOps?**  
*Answer:* Unit tests (like JUnit Mockito tests) verify isolated Java methods in memory without a web browser. Selenium WebDriver tests are end-to-end UI regression tests that control a real browser, click elements, fill forms, verify JavaScript events, and validate that the backend, database, and frontend interact harmoniously.

**Q2: What is a Quality Gate in Jenkins and why is it essential?**  
*Answer:* A Quality Gate is an automated threshold configured in the CI/CD pipeline. In our pipeline, if any Selenium test assertion fails, the build halts immediately, captures a failure screenshot into `target/screenshots/`, and skips packaging and Docker deployment to ensure broken code never reaches users.

**Q3: How does your Docker setup support modern microservices best practices?**  
*Answer:* We implemented a multi-stage Dockerfile using Eclipse Temurin JDK 21 for building and Alpine JRE 21 for execution, shrinking image size to 214MB. We enforced container security by running under an unprivileged user (`vaxuser:vaxgroup`) and configured container health checks (`/api/health`).

**Q4: What is Idempotency in Ansible and how did you prove it?**  
*Answer:* Idempotency guarantees that executing an automation playbook multiple times leaves the system in the exact same state without unintended side effects. We proved it by running `ansible/playbook.yml` twice: the first run made 12 changes, and the second run reported `changed=0`, proving complete idempotency.

**Q5: How does your rollback mechanism work?**  
*Answer:* We developed `ansible/rollback.yml`. Application versions are preserved in `/opt/vaccination-portal/releases/`. If an active release fails health checks, Ansible atomically re-points the symbolic link `app.jar` back to `vaccination-reminder-portal-1.0.0.jar`, restarts the systemd service, and verifies the `/api/health` probe within 8 seconds.
