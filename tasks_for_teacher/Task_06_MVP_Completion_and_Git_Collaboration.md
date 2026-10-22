# Task 06: MVP Completion and Git Collaboration
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 15  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To implement the remaining essential MVP functions for the Vaccination Reminder Portal (create, view, update, search records; role-based status workflow; summary dashboard with Chart.js), demonstrate development on a second feature branch, simulate merge conflict creation and manual resolution, tag the release baseline as `v1.0.0`, and update the product backlog.

---

### 2. Syllabus Deliverable Requirement
> *"Functional MVP, resolved merge conflict, tagged version and updated backlog."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain MVP Completion & Collaboration
- **Speech Script:**
  > *"Madam/Sir, in Task 6, I completed our remaining MVP features on branch `feature/status-workflow-dashboard`. To satisfy the syllabus requirement of Git collaboration, I simulated a real-world merge conflict between two branches modifying application properties, demonstrated manual conflict resolution, merged into `main`, and tagged our release as `v1.0.0`."*

#### Step 2: Show the Git Commit Graph and Release Tag
Run this command in PowerShell:
```powershell
git log --oneline --graph --all -n 8; git tag -n
```

#### Step 3: What Output to Point To:
Show the Git tree graph and release tag on screen:
```text
*   b3b2fd1 chore(release): merge develop into main for MVP release v1.0.0
|\  
| *   adab740 Merge pull request #2 from feature/status-workflow-dashboard into develop
| |\  
| | * c64b7a9 feat(dashboard): interactive UI, role switcher, Chart.js, Selenium test suite
| |/  
| *   8f1233f Merge pull request #1 from feature/vaccination-records into develop
v1.0.0          Release v1.0.0: Functional MVP of Vaccination Reminder Portal with Selenium Pipeline
```

#### Step 4: Explain the Merge Conflict Resolution
Show the conflict markers in [`docs/TASK-06-MVP-Completion-and-Git-Collaboration.md`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docs/TASK-06-MVP-Completion-and-Git-Collaboration.md):
```properties
<<<<<<< HEAD (Branch: develop)
app.default.reminder.channel=SMS_AND_EMAIL
=======
app.default.reminder.channel=WHATSAPP_PRIMARY
>>>>>>> conflict/vaccination-status-update
```
- **Explain your fix:** *"I resolved this by combining both into `MULTI_CHANNEL_SMS_EMAIL_WHATSAPP`, staged the file with `git add`, and committed the clean resolution."*

#### Step 5: Show the Live MVP in Browser
Open `http://localhost:8080` in Chrome/Edge and show:
- Real-time KPI summary cards (Total Beneficiaries, Completed Doses, Compliance Rate %)
- Interactive Chart.js donut chart
- Live search input & status filter dropdown (`SCHEDULED`, `COMPLETED`, `OVERDUE`)
- Role switcher (`👨‍⚕️ Health Worker` vs `👨‍👩‍👧 Parent View`)

---

### 4. Likely Viva Voce Questions & Answers for Task 6

**Q1: What creates a Git merge conflict?**  
*Answer:* A merge conflict occurs when two branches make different changes to the same line(s) of a file, or one branch deletes a file that another branch modified. Git cannot automatically decide which change to keep and asks the developer to resolve it manually.

**Q2: What is the difference between a lightweight tag and an annotated tag in Git?**  
*Answer:* A lightweight tag is simply a pointer to a specific commit. An annotated tag (created with `git tag -a`) is stored as a full object in the Git database containing the tagger name, email, date, and a release message. We used an annotated tag for `v1.0.0`.

**Q3: How do role-based permissions work in your MVP?**  
*Answer:* In Parent View, the interface is read-only; clinical administration buttons are hidden. Switching to Healthcare Worker mode unlocks the `[💉 Administer]` buttons to record the nurse's name, vaccine batch number, and mark doses `COMPLETED`.
