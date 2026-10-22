# Task 01: Problem Definition and Scope
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 16  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To study the real-time clinical and operational need for an automated Vaccination Reminder Portal, identify target users, analyze existing healthcare pain points, identify key stakeholders, outline technical/operational constraints, and freeze a 15-task Minimum Viable Product (MVP) scope.

---

### 2. Syllabus Deliverable Requirement
> *"Problem statement, stakeholder list, objectives, constraints and approved MVP scope."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain the Problem Definition to Teacher
- **Speech Script:**
  > *"Madam/Sir, in Task 1, I studied the real-world challenge of childhood immunization dropouts. In physical card-based systems, parents frequently miss subsequent booster shots (such as Pentavalent-2, Measles, and DPT boosters) due to the long intervals between doses. I created the scope for **VaxGuard**, an automated web portal that calculates the complete immunization calendar based on Date of Birth and sends proactive reminders."*

#### Step 2: Show the Stakeholder Analysis Table
Show the following stakeholder matrix on your screen or in print:

| Stakeholder | Category | Core Responsibilities & Needs |
| :--- | :--- | :--- |
| **Parents & Caregivers** | Primary End-Users | Check child's upcoming vaccine milestones, receive automated SMS/Email reminders, view historical vaccination records. |
| **Healthcare Staff & Nurses** | Clinical Users | Update vaccine status from `SCHEDULED` to `COMPLETED`, record administering nurse name and vaccine batch/lot numbers. |
| **Public Health Administrators**| System Supervisors | Monitor overall clinic immunization compliance %, identify overdue children in the catchment area, dispatch batch reminders. |
| **DevOps & QA Engineers** | Technical Maintainers | Automate end-to-end regression testing with Selenium WebDriver, enforce Jenkins CI/CD quality gates, automate container rollout. |

#### Step 3: Show the Objectives & Measurable Success Criteria
1. **Automated Schedule Calculation:** 100% adherence to national immunization guidelines computed automatically from DOB.
2. **Automated Quality Gate:** Continuous testing in Jenkins that catches defects in < 90 seconds.
3. **Containerized Deployment:** Multi-stage Docker container deployed with non-root security.
4. **Reliability & Idempotency:** Ansible server provisioning with zero drift (`changed=0` on re-runs).

#### Step 4: Show the Approved 15-Task Frozen Scope
Point out the 15 sequential milestones:
1. Problem Definition and Scope
2. Agile Planning and DevOps Workflow
3. Requirements, Architecture and Technology Setup
4. Git and GitHub Repository Initialization
5. Feature Development with Branching (Schedule Engine)
6. MVP Completion and Git Collaboration (Merge Conflict & Tagging v1.0.0)
7. Jenkins Installation and Continuous Integration Job
8. Pipeline as Code and Server Deployment (Jenkinsfile)
9. Selenium Test Design and Local Execution (5 Journeys)
10. Continuous Testing in Jenkins (Quality Gate & Defect Remediation)
11. Docker Image and Container Lifecycle
12. Jenkins-Docker Continuous Deployment
13. Configuration Management Script (Ansible)
14. Automated Provisioning and Reliability Validation (Idempotency & Rollback)
15. Final End-to-End Release, Documentation and Viva

---

### 4. Likely Viva Voce Questions & Answers for Task 1

**Q1: What are the primary pain points in the existing immunization tracking system?**  
*Answer:* Physical immunization cards get lost or damaged; parents lack automated alerts; healthcare centers cannot easily filter overdue children; and there is a high dropout rate between birth doses and multi-month boosters.

**Q2: What is an MVP (Minimum Viable Product)?**  
*Answer:* An MVP is the core version of a product containing only essential features needed to deliver immediate value to users and validate hypotheses with minimal development effort. For VaxGuard, the MVP covers registration, automated schedule calculation, role-based status updates, search/filter, and a KPI dashboard.

**Q3: How does DevOps add value to a healthcare application?**  
*Answer:* Healthcare applications require near-zero error tolerance. DevOps ensures reliability through automated Selenium regression testing on every commit, automated container deployment, zero-downtime rollouts, and instant automated rollback if an issue occurs.
