# Contributing & Branch Policy Guide

## Project: Vaccination Reminder Portal (VaxGuard)
**Author:** Gayatri Patil (PRN: 23102A0016, Roll: 15)

---

### 1. Branch Naming Strategy
All development in this repository adheres to a strict GitFlow-inspired branching strategy:

| Branch Type | Syntax | Example | Purpose |
| :--- | :--- | :--- | :--- |
| **Main** | `main` | `main` | Release-ready, production code. Tagged with versions (e.g., `v1.0.0`). |
| **Develop** | `develop` | `develop` | Integration branch for validated features. |
| **Feature** | `feature/<feature-name>` | `feature/vaccination-records` | Individual MVP user story development. |
| **Defect / Fix** | `defect/<defect-name>` | `defect/registration-validation` | Bug fixes and quality gate remediation. |
| **Release** | `release/<version>` | `release/v1.0.0` | Release stabilization and final testing. |

---

### 2. Commit Message Conventions
Commit messages must follow the Conventional Commits specification:
- `feat(records): add auto-generation of national immunization schedule from DOB`
- `feat(dashboard): implement real-time KPI metrics and Chart.js distribution chart`
- `test(selenium): add 5 critical user journey end-to-end tests with failure screenshots`
- `ci(jenkins): configure declarative pipeline quality gate with surefire reporting`
- `fix(validation): resolve simulated defect in child registration endpoint`
- `docs(reports): add complete 15-stage DevOps deliverables dossier`

---

### 3. Pull Request & Code Review Process
1. Create a feature branch from `develop`.
2. Commit small, logical changes with descriptive messages.
3. Push branch to remote and open a Pull Request targeting `develop`.
4. Ensure all automated Selenium quality gates in Jenkins pass (`RUN_SELENIUM_TESTS=true`).
5. Require at least one peer review approval.
6. Merge using `--no-ff` (no fast-forward) to preserve historical branch topology.
