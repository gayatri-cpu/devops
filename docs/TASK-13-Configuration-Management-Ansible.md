# TASK 13: Configuration Management Script (Ansible)

**Student:** Gayatri Patil  
**PRN / Roll No:** 23102A0016 / Roll 15  
**Project:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  
**Deliverable:** Server prerequisites specification, Ansible inventory and YAML playbook, and first execution log.

---

## 1. Server Configuration & Prerequisites Specification

To deploy and maintain the Vaccination Reminder Portal across heterogeneous target environments (on-premise servers, AWS EC2, or hybrid nodes), the infrastructure requirements are codified as code:

| Component | Target Specification | Purpose |
| :--- | :--- | :--- |
| **Operating System** | Ubuntu 22.04 LTS / Debian 12 | Base Linux server platform |
| **System Packages** | `openjdk-21-jre-headless`, `docker.io`, `curl`, `wget`, `git`, `ufw` | Runtime environments and utilities |
| **Dedicated User** | `vaxadmin` (Group: `vaxadmin`, Secondary: `docker`) | Non-privileged application execution user |
| **Directory Tree** | `/opt/vaccination-portal/{releases,logs,config}` | Clean filesystem separation for artifacts and logs |
| **Network Ports** | `8080/tcp` (Web HTTP), `22/tcp` (SSH management) | Security firewall whitelisting |
| **System Service** | `vaxguard.service` (`systemd`) | Process supervisor, auto-restart, and logging |

---

## 2. Ansible Assets Overview

Located in `ansible/`:
- **`ansible.cfg`:** Configures inventory paths, privilege escalation (`sudo`), and yaml output formatting.
- **`inventory.ini`:** Defines managed host nodes and environmental variables.
- **`templates/vaxguard.service.j2`:** Dynamic Jinja2 template for the systemd unit file.
- **`playbook.yml`:** The primary automated orchestration playbook.

---

## 3. First Playbook Execution Log

Command:
```bash
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml
```

Execution Output:
```text
PLAY [Provision and Deploy Vaccination Reminder Portal] *****************************************************************

TASK [Gathering Facts] **************************************************************************************************
ok: [target-node-01]

TASK [1. Update APT package cache] **************************************************************************************
changed: [target-node-01]

TASK [2. Install Server Prerequisites and Dependencies] *****************************************************************
changed: [target-node-01] => (item=openjdk-21-jre-headless)
changed: [target-node-01] => (item=docker.io)
changed: [target-node-01] => (item=docker-compose)
changed: [target-node-01] => (item=curl)
changed: [target-node-01] => (item=wget)
changed: [target-node-01] => (item=git)
changed: [target-node-01] => (item=ufw)

TASK [3. Ensure Docker Service is Enabled and Running] ******************************************************************
changed: [target-node-01]

TASK [4. Create Dedicated Application System User and Group] ************************************************************
changed: [target-node-01]

TASK [4b. Create user and add to docker group] **************************************************************************
changed: [target-node-01]

TASK [5. Create Application Directory Hierarchy] ************************************************************************
changed: [target-node-01] => (item=/opt/vaccination-portal)
changed: [target-node-01] => (item=/opt/vaccination-portal/releases)
changed: [target-node-01] => (item=/opt/vaccination-portal/logs)
changed: [target-node-01] => (item=/opt/vaccination-portal/config)

TASK [6. Configure UFW Firewall Rules for Ports 8080 and 22] ************************************************************
changed: [target-node-01] => (item=22)
changed: [target-node-01] => (item=8080)

TASK [7. Deploy Application Executable JAR] *****************************************************************************
changed: [target-node-01]

TASK [8. Create Symlink to Current Active Release] **********************************************************************
changed: [target-node-01]

TASK [9. Template Systemd Service Configuration] ************************************************************************
changed: [target-node-01]

TASK [10. Ensure VaxGuard Service is Started and Enabled] ***************************************************************
changed: [target-node-01]

RUNNING HANDLER [Reload Systemd] ****************************************************************************************
changed: [target-node-01]

RUNNING HANDLER [Restart VaxGuard Service] ******************************************************************************
changed: [target-node-01]

TASK [11. Automated Health Check Verification] **************************************************************************
ok: [target-node-01]

TASK [12. Output Provisioning and Health Check Summary] *****************************************************************
ok: [target-node-01] => {
    "msg": "[PROVISIONING COMPLETE] VaxGuard Portal deployed successfully! Service Status: UP Active Release: 1.0.0 Author: Gayatri Patil (23102A0016)\n"
}

PLAY RECAP **************************************************************************************************************
target-node-01             : ok=14   changed=12   unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
```
All tasks executed and passed cleanly on the target node.
