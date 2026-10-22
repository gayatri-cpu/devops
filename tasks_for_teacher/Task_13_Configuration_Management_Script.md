# Task 13: Configuration Management Script (Ansible)
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 16  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To identify all server prerequisites for the Vaccination Reminder Portal (packages, users, folders, files, network ports, and system services), construct an Ansible inventory (`inventory.ini`), and develop an automated YAML playbook (`playbook.yml`) with Jinja2 systemd templates to provision the target node.

---

### 2. Syllabus Deliverable Requirement
> *"Configuration specification, Puppet manifest or Ansible playbook and first execution log."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain Ansible Configuration Management
- **Speech Script:**
  > *"Madam/Sir, in Task 13, I automated server provisioning using Ansible. I codified all prerequisite specifications—installing OpenJDK 21, Docker, and UFW firewall; creating an unprivileged system user `vaxadmin`; provisioning directory trees `/opt/vaccination-portal`; templating systemd unit files; and managing firewall ports 8080 and 22 into `ansible/playbook.yml`."*

#### Step 2: Show the Server Prerequisite Specification Table
Show this specification table on screen:

| Component | Target Requirement | Purpose |
| :--- | :--- | :--- |
| **Target OS** | Ubuntu 22.04 LTS / Debian 12 | Standard Linux host server |
| **System Packages** | `openjdk-21-jre-headless`, `docker.io`, `curl`, `git`, `ufw` | Core runtime and administration tools |
| **Application User**| `vaxadmin` (Secondary group: `docker`) | Isolated non-privileged service user |
| **Filesystem Tree** | `/opt/vaccination-portal/{releases,logs,config}` | Clean separation of releases and logs |
| **Firewall Ports** | `8080/tcp` (Web HTTP), `22/tcp` (SSH) | Security access control via UFW |
| **Service Supervisor** | `vaxguard.service` (`systemd`) | Process supervision, auto-restart on failure |

#### Step 3: Show the Ansible Playbook
Open file: [`ansible/playbook.yml`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/ansible/playbook.yml)  
Walk through the 12 tasks:
1. Update APT package cache
2. Install OpenJDK 21, Docker, curl, git, ufw
3. Ensure Docker service is enabled and started
4. Create dedicated user and group `vaxadmin`
5. Create application directory hierarchy
6. Configure UFW firewall rules for ports 8080 and 22
7. Deploy application executable JAR
8. Manage symlink `/opt/vaccination-portal/app.jar`
9. Template systemd service (`templates/vaxguard.service.j2`)
10. Ensure VaxGuard systemd service is active and enabled
11. Automated health check verification
12. Output provisioning summary

#### Step 4: Show the First Execution Log
Open [`docs/TASK-13-Configuration-Management-Ansible.md`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docs/TASK-13-Configuration-Management-Ansible.md) and point out:
- Initial execution recap: `target-node-01 : ok=14 changed=12 unreachable=0 failed=0`
- Confirming that all 12 configuration elements were provisioned cleanly.

---

### 4. Likely Viva Voce Questions & Answers for Task 13

**Q1: What is Configuration Management (CM)?**  
*Answer:* Configuration Management is the practice of maintaining computer systems, servers, and software in a known, desired, and consistent state using automated, version-controlled code rather than manual administration.

**Q2: What is the difference between Ansible and Puppet/Chef?**  
*Answer:* Ansible is **agentless**: it uses standard SSH to connect to target nodes and does not require installing any background daemon on managed servers. Puppet and Chef are **agent-based**, requiring dedicated background client agents running on every target machine.

**Q3: What is the role of the `inventory.ini` file in Ansible?**  
*Answer:* The inventory file defines the target hostnames, IP addresses, groups (e.g., `[vax_servers]`), connection credentials (SSH users and private keys), and environment variables that Ansible connects to and manages.
