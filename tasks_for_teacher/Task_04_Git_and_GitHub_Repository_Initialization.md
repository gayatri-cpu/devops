# Task 04: Git and GitHub Repository Initialization
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 15  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To initialize the Git repository for the Vaccination Reminder Portal, configure comprehensive `.gitignore` rules, establish a professional folder structure, define GitHub issue templates (bug reports and feature requests), construct a pull request template with review checklists, establish a GitFlow branch naming policy, and commit the initial application skeleton.

---

### 2. Syllabus Deliverable Requirement
> *"Repository URL, README, initial commits, issues and branch policy."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain Git Setup and Branch Policy
- **Speech Script:**
  > *"Madam/Sir, in Task 4, I set up our source control foundation following the GitFlow branching standard. I configured `.gitignore` so that generated build artifacts (like the `target/` directory and IDE metadata) are never committed. I also created issue templates for bug tracking, a pull request template, and enforced branch protection rules."*

#### Step 2: Show the Active Git Configuration
Run this command in PowerShell:
```powershell
git status; git branch -a
```

#### Step 3: What Output to Point To:
Show the clean working tree and branches:
```text
On branch main
nothing to commit, working tree clean
* main
  develop
```

#### Step 4: Show the Branch Policy Table
Show this table in [`CONTRIBUTING.md`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/CONTRIBUTING.md):

| Branch | Base | Purpose | Example |
| :--- | :--- | :--- | :--- |
| **`main`** | - | Production-ready stable release baseline. | `main` (Tagged with `v1.0.0`) |
| **`develop`** | `main` | Ongoing integration branch for tested features. | `develop` |
| **`feature/*`** | `develop` | Developing new features in isolation. | `feature/vaccination-records` |
| **`defect/*`** | `develop` | Dedicated bug fixes and remediation. | `defect/fix-registration-validation` |

#### Step 5: Show GitHub Issue Templates
Open folder `.github/ISSUE_TEMPLATE/` and show:
- `bug_report.md` (Standardizes environment, steps to reproduce, and screenshots)
- `feature_request.md` (Standardizes problem statement and acceptance criteria)

---

### 4. Likely Viva Voce Questions & Answers for Task 4

**Q1: Why is a `.gitignore` file necessary?**  
*Answer:* A `.gitignore` prevents unneeded, large, or security-sensitive files (such as compiled `.class` files, `.jar` packages, `.log` files, and IDE configs) from being committed into Git. This keeps the repository lightweight and prevents merge conflicts.

**Q2: What is the purpose of GitFlow branching?**  
*Answer:* GitFlow provides an isolated structure: developers work on `feature/*` branches without affecting the stable `main` branch. Code is integrated into `develop` for testing, and only merged into `main` after passing quality gates.

**Q3: What information should a good commit message contain?**  
*Answer:* Following the Conventional Commits specification, it includes a type (`feat`, `fix`, `test`, `ci`, `docs`), an optional scope, and a concise imperative description (e.g., `feat(workflow): implement national immunization schedule calculation`).
