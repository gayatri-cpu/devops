# TASK 04: Git and GitHub Repository Initialization

**Student:** Gayatri Patil  
**PRN / Roll No:** 23102A0016 / Roll 15  
**Project:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  
**Deliverable:** Repository structure, README, initial commits, issue templates, and branch policy.

---

## 1. Repository Overview

- **Repository Name:** `vaccination-reminder-portal`
- **Upstream Git URL:** `https://github.com/gayatri-patil/vaccination-reminder-portal.git`
- **Default Baseline Branch:** `main`
- **Active Integration Branch:** `develop`
- **Primary Technology Stack:** Java 21, Spring Boot 3.3.4, Maven 3.9, Selenium WebDriver 4.25, Docker, Ansible.

---

## 2. Directory Hierarchy

```text
vaccination-reminder-portal/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   └── pull_request_template.md
├── ansible/
│   ├── ansible.cfg
│   ├── healthcheck.sh
│   ├── inventory.ini
│   ├── playbook.yml
│   ├── rollback.yml
│   └── templates/
│       └── vaxguard.service.j2
├── docker/
│   ├── Dockerfile
│   ├── container-lifecycle.ps1
│   ├── container-lifecycle.sh
│   └── docker-compose.yml
├── docs/
│   ├── TASK-01-Problem-Definition-and-Scope.md
│   ├── TASK-02-Agile-Planning-and-DevOps-Workflow.md
│   ├── ...
│   └── TASK-15-Final-Project-Report-and-Viva-Prep.md
├── jenkins/
│   ├── Jenkinsfile
│   └── job-config.xml
├── src/
│   ├── main/
│   │   ├── java/com/vaxguard/
│   │   │   ├── VaxGuardApplication.java
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── css/style.css
│   │           ├── js/app.js
│   │           └── index.html
│   └── test/
│       └── java/com/vaxguard/
│           ├── VaxGuardApplicationTests.java
│           └── selenium/
│               ├── BaseSeleniumTest.java
│               ├── BeneficiaryRegistrationJourneyTest.java
│               ├── DashboardMetricsJourneyTest.java
│               ├── DefectSimulationJourneyTest.java
│               ├── ScreenshotOnFailureExtension.java
│               ├── SearchAndFilterJourneyTest.java
│               └── StatusWorkflowJourneyTest.java
├── .gitignore
├── CONTRIBUTING.md
├── Jenkinsfile
├── mvn.cmd
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## 3. Initial Commits Log Evidence

Execution output of `git log --oneline`:
```text
b3b2fd1 chore(release): merge develop into main for MVP release v1.0.0
adab740 Merge pull request #2 from feature/status-workflow-dashboard into develop
c64b7a9 feat(dashboard): interactive UI, role switcher, Chart.js, Selenium test suite and Docker/Ansible automation
8f1233f Merge pull request #1 from feature/vaccination-records into develop
148f07f feat(workflow): implement national immunization schedule calculation and beneficiary registration
9b75b46 feat(core): domain entities, JPA repositories and application configuration
2c666d6 feat(init): repository skeleton, build tools, CI/CD templates and branch policy
```

---

## 4. Branch Management & GitFlow Policy

Adherence to the **GitFlow** branching model:
1. **`main`:** Contains production-ready code. Commits are only made via approved Pull Requests from `develop` and tagged with semantic version tags (`v1.0.0`).
2. **`develop`:** Acts as the ongoing integration branch for all completed feature branches.
3. **`feature/*`:** Isolated branches branched from `develop` for specific user stories (e.g., `feature/vaccination-records`, `feature/status-workflow-dashboard`).
4. **`defect/*`:** Dedicated branches for reproducing, validating, and fixing bug reports caught by the Selenium CI quality gate.

---

## 5. GitHub Issue & Pull Request Templates

The repository enforces standard markdown templates for collaborative tracking:
- **`bug_report.md`:** Standardizes environment details, reproducible steps, expected vs. actual outcomes, and error logs.
- **`feature_request.md`:** Clarifies user problem statement, proposed solution, and acceptance criteria.
- **`pull_request_template.md`:** Enforces self-review, unit test execution, Selenium test pass verification, and Jira/issue linking.
