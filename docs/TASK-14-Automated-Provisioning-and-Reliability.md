# TASK 14: Automated Provisioning and Reliability Validation

**Student:** Gayatri Patil  
**PRN / Roll No:** 23102A0016 / Roll 15  
**Project:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  
**Deliverable:** Provisioned target node, Ansible idempotency evidence, health-check result, and rollback demonstration.

---

## 1. Idempotency Demonstration

### 1.1 The Concept of Idempotency
In DevOps configuration management, **idempotency** is the guarantee that running an automation script multiple times against a system will produce the exact same desired state without unintended side effects or redundant changes.

### 1.2 Second Playbook Execution Log (Demonstrating Idempotency)
Rerunning `playbook.yml` immediately following the first provisioning:

```bash
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml
```

Execution Output:
```text
PLAY [Provision and Deploy Vaccination Reminder Portal] *****************************************************************

TASK [Gathering Facts] **************************************************************************************************
ok: [target-node-01]

TASK [1. Update APT package cache] **************************************************************************************
ok: [target-node-01]

TASK [2. Install Server Prerequisites and Dependencies] *****************************************************************
ok: [target-node-01] => (item=openjdk-21-jre-headless)
ok: [target-node-01] => (item=docker.io)
ok: [target-node-01] => (item=docker-compose)
ok: [target-node-01] => (item=curl)
ok: [target-node-01] => (item=wget)
ok: [target-node-01] => (item=git)
ok: [target-node-01] => (item=ufw)

TASK [3. Ensure Docker Service is Enabled and Running] ******************************************************************
ok: [target-node-01]

TASK [4. Create Dedicated Application System User and Group] ************************************************************
ok: [target-node-01]

TASK [4b. Create user and add to docker group] **************************************************************************
ok: [target-node-01]

TASK [5. Create Application Directory Hierarchy] ************************************************************************
ok: [target-node-01] => (item=/opt/vaccination-portal)
ok: [target-node-01] => (item=/opt/vaccination-portal/releases)
ok: [target-node-01] => (item=/opt/vaccination-portal/logs)
ok: [target-node-01] => (item=/opt/vaccination-portal/config)

TASK [6. Configure UFW Firewall Rules for Ports 8080 and 22] ************************************************************
ok: [target-node-01] => (item=22)
ok: [target-node-01] => (item=8080)

TASK [7. Deploy Application Executable JAR] *****************************************************************************
ok: [target-node-01]

TASK [8. Create Symlink to Current Active Release] **********************************************************************
ok: [target-node-01]

TASK [9. Template Systemd Service Configuration] ************************************************************************
ok: [target-node-01]

TASK [10. Ensure VaxGuard Service is Started and Enabled] ***************************************************************
ok: [target-node-01]

TASK [11. Automated Health Check Verification] **************************************************************************
ok: [target-node-01]

TASK [12. Output Provisioning and Health Check Summary] *****************************************************************
ok: [target-node-01] => {
    "msg": "[PROVISIONING COMPLETE] VaxGuard Portal deployed successfully! Service Status: UP Active Release: 1.0.0 Author: Gayatri Patil (23102A0016)\n"
}

PLAY RECAP **************************************************************************************************************
target-node-01             : ok=14   changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
```

**Result:** `changed=0`! The automation proved complete idempotency across packages, users, folders, and service units.

---

## 2. Health-Check Verification Result

Script: `ansible/healthcheck.sh`
```bash
./ansible/healthcheck.sh http://localhost:8080/api/health
```

Output:
```text
Probing Health Endpoint: http://localhost:8080/api/health
[HEALTH CHECK PASSED] HTTP 200 OK
{
  "status": "UP",
  "service": "Vaccination Reminder Portal (VaxGuard)",
  "version": "1.0.0",
  "author": "Gayatri Patil (PRN: 23102A0016, Roll: 15)",
  "timestamp": "2026-10-22T14:26:45.890",
  "startupTime": "2026-10-22T14:26:38.102",
  "database": "H2 In-Memory (Connected)",
  "qualityGate": "PASSED"
}
```

---

## 3. Automated Rollback and Recovery Demonstration

File: `ansible/rollback.yml`

### Scenario
An updated build `v1.1.0` fails downstream health checks. The automated rollback playbook reverts the active deployment back to previous stable baseline `v1.0.0`.

Execution Command:
```bash
ansible-playbook -i ansible/inventory.ini ansible/rollback.yml
```

Execution Output:
```text
PLAY [Automated Rollback to Previous Stable Release] ********************************************************************

TASK [Gathering Facts] **************************************************************************************************
ok: [target-node-01]

TASK [1. Verify Previous Stable Release Artifact Exists] ****************************************************************
ok: [target-node-01]

TASK [2. Re-point Symlink to Previous Stable Artifact] ******************************************************************
changed: [target-node-01]

TASK [3. Restart Application Service with Previous Version] *************************************************************
changed: [target-node-01]

TASK [4. Perform Health Check on Restored Release] **********************************************************************
ok: [target-node-01]

TASK [5. Display Rollback Success Report] *******************************************************************************
ok: [target-node-01] => {
    "msg": "[ROLLBACK SUCCESSFUL] Application safely recovered! Current Active Version: 1.0.0 Health Status: UP Database: H2 In-Memory (Connected)\n"
}

PLAY RECAP **************************************************************************************************************
target-node-01             : ok=6    changed=2    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
```
The node restored the known-good `v1.0.0` artifact and resumed operational readiness within 8 seconds.
