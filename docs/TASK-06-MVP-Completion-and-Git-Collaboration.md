# TASK 06: MVP Completion and Git Collaboration

**Student:** Gayatri Patil  
**PRN / Roll No:** 23102A0016 / Roll 16  
**Project:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  
**Deliverable:** Functional MVP, resolved merge conflict demonstration, tagged version `v1.0.0`, and updated backlog.

---

## 1. MVP Completion Overview

The second development cycle completed all remaining essential MVP functions:
1. **Interactive Search & Status Filtering:** Real-time query matching across child name, parent phone number, and vaccine name.
2. **Role-Based Status Transition Workflow:** Toggle between Parent View (read-only) and Healthcare Worker (clinical administration mode).
3. **Summary KPI Dashboard:** Real-time aggregate count for total children, completed doses, upcoming doses (7-day horizon), overdue doses, and compliance rate.
4. **Visual Analytics Chart:** Interactive Chart.js doughnut chart breaking down the immunization coverage.
5. **Smart Reminder Simulator:** Batch alert generator simulating dispatch to parents of due/overdue infants.

---

## 2. Second Feature Branch: `feature/status-workflow-dashboard`

```bash
# Branch from develop
git checkout develop
git checkout -b feature/status-workflow-dashboard

# Add UI templates, static stylesheets, scripts, Selenium tests, Docker, and Ansible assets
git add src/main/resources/static/ src/test/ docker/ ansible/ jenkins/
git commit -m "feat(dashboard): interactive UI, role switcher, Chart.js, Selenium test suite and Docker/Ansible automation"

# Merge into develop
git checkout develop
git merge --no-ff feature/status-workflow-dashboard -m "Merge pull request #2 from feature/status-workflow-dashboard into develop"
```

---

## 3. Merge Conflict Creation and Resolution Demonstration

To demonstrate collaborative conflict resolution under real-world multi-developer conditions, a merge conflict was created on branch `conflict/vaccination-status-update` and resolved into `develop`.

### 3.1 Conflict Scenario
Two developers modified the default status label and logging format simultaneously in the application configuration:

**Conflict Markers in `application.properties`:**
```properties
<<<<<<< HEAD (Branch: develop)
# Active Deployment Profile
app.default.reminder.channel=SMS_AND_EMAIL
app.ui.theme=GLASSMORPHIC_DARK
=======
# Active Deployment Profile
app.default.reminder.channel=WHATSAPP_PRIMARY
app.ui.theme=GLASSMORPHIC_DARK
>>>>>>> conflict/vaccination-status-update
```

### 3.2 Resolution Action
The conflict was manually inspected and resolved to combine both channels into a unified multi-channel communication format:
```properties
# Active Deployment Profile - Conflict Resolved
app.default.reminder.channel=MULTI_CHANNEL_SMS_EMAIL_WHATSAPP
app.ui.theme=GLASSMORPHIC_DARK
```

```bash
git add src/main/resources/application.properties
git commit -m "fix(merge-conflict): resolve communication channel setting between develop and feature branch"
```

---

## 4. Release Baseline Tagging (`v1.0.0`)

With all MVP features tested and verified, `develop` was merged into `main` and tagged:

```bash
git checkout main
git merge --no-ff develop -m "chore(release): merge develop into main for MVP release v1.0.0"
git tag -a v1.0.0 -m "Release v1.0.0: Functional MVP of Vaccination Reminder Portal with Selenium Pipeline"
```

Verification output of `git tag -n`:
```text
v1.0.0          Release v1.0.0: Functional MVP of Vaccination Reminder Portal with Selenium Pipeline
```

---

## 5. Updated Product Backlog Status

| Backlog Item | Assigned Feature Branch | Resolution Commit | Sprint Status |
| :--- | :--- | :--- | :--- |
| Child Schedule Engine | `feature/vaccination-records` | `148f07f` | **Closed / Merged** |
| Role-Based Clinical Workflow | `feature/status-workflow-dashboard` | `c64b7a9` | **Closed / Merged** |
| KPI Summary Analytics & Chart | `feature/status-workflow-dashboard` | `c64b7a9` | **Closed / Merged** |
| Merge Conflict Remediation | `conflict/vaccination-status-update` | `b3b2fd1` | **Resolved** |
| Release Tag Baseline | `main` | `v1.0.0` | **Released** |
