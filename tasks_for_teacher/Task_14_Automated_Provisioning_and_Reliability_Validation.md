# Task 14: Automated Provisioning and Reliability Validation
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 16  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To provision a clean target environment using Ansible, deploy the application/container, rerun the automation to demonstrate idempotency, perform automated health checks on the running service, and demonstrate automated rollback and recovery to the previous stable release (`v1.0.0`).

---

### 2. Syllabus Deliverable Requirement
> *"Provisioned node, idempotency evidence, health-check result and rollback/recovery demonstration."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain Idempotency & Rollback to Teacher
- **Speech Script:**
  > *"Madam/Sir, in Task 14, I validated two foundational DevOps reliability principles: **Idempotency** and **Automated Rollback**. I proved idempotency by rerunning our Ansible playbook against the already-provisioned node; the re-run made zero redundant modifications, reporting `changed=0`. I also created an automated rollback playbook `rollback.yml` that reverts the deployment back to previous stable release `v1.0.0` within 8 seconds if a post-deployment health check fails."*

#### Step 2: Show the Idempotency Proof (`changed=0`)
Show the second execution log in [`docs/TASK-14-Automated-Provisioning-and-Reliability.md`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docs/TASK-14-Automated-Provisioning-and-Reliability.md):
```text
PLAY RECAP **************************************************************************************************************
target-node-01             : ok=14   changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
```
- **Explain to Teacher:** *"Look at `changed=0`. This proves that Ansible checked all packages, users, folders, and systemd units, found them already in the desired state, and made zero redundant changes."*

#### Step 3: Show the Automated Health Check Script
Open file: [`ansible/healthcheck.sh`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/ansible/healthcheck.sh)  
Show how it probes the application endpoint:
```bash
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$ENDPOINT")
if [ "$HTTP_STATUS" -eq 200 ]; then
    echo "[HEALTH CHECK PASSED] HTTP 200 OK"
    exit 0
else
    echo "[HEALTH CHECK FAILED] Expected HTTP 200, Received HTTP $HTTP_STATUS"
    exit 1
fi
```

#### Step 4: Show the Automated Rollback Playbook
Open file: [`ansible/rollback.yml`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/ansible/rollback.yml)  
Explain the 5 rollback steps:
1. Verify previous stable release `vaccination-reminder-portal-1.0.0.jar` exists in `/opt/vaccination-portal/releases/`.
2. Atomically re-point the symlink `/opt/vaccination-portal/app.jar` to `1.0.0.jar`.
3. Restart `vaxguard.service` via systemd.
4. Perform health check against `http://localhost:8080/api/health` until HTTP 200 is verified.
5. Output successful recovery report.

---

### 4. Likely Viva Voce Questions & Answers for Task 14

**Q1: What is Idempotency in DevOps automation?**  
*Answer:* Idempotency is the property where executing an operation multiple times produces the exact same result as executing it once, without causing configuration drift, side effects, or duplicate resources.

**Q2: How does your deployment architecture facilitate fast rollbacks?**  
*Answer:* Rather than overwriting the executable file, each release is version-stamped in `/opt/vaccination-portal/releases/`. The active service runs via a symbolic link (`app.jar`). To roll back, Ansible simply updates the symlink pointer to the previous version and restarts the service, completing rollback in under 8 seconds.

**Q3: What triggers an automated rollback in a CI/CD pipeline?**  
*Answer:* If a post-deployment health check (`/api/health`) or automated smoke test fails to return HTTP 200 within a defined retry window, the pipeline automatically invokes the rollback playbook to prevent user-facing downtime.
