# TASK 07: Jenkins Installation and Continuous Integration Job

**Student:** Gayatri Patil  
**PRN / Roll No:** 23102A0016 / Roll 15  
**Project:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  
**Deliverable:** Configured Jenkins job configuration, successful build log, trigger evidence, and archived build artifact.

---

## 1. Jenkins Installation and Global Tool Configuration

### 1.1 Host Environment
- **Jenkins Controller Version:** Jenkins 2.452.1 LTS
- **Host OS:** Ubuntu 22.04 LTS / Windows 11
- **Installed Plugins:**
  - Git Plugin
  - Pipeline Plugin
  - Maven Integration Plugin
  - JUnit Plugin (for parsing test results)
  - HTML Publisher Plugin
  - Credentials Plugin

### 1.2 Global Tool Definitions
In Jenkins UI (**Manage Jenkins ➔ Tools**):
- **JDK:** `JDK-21` (Path: `/usr/lib/jvm/java-21-openjdk-amd64` or `C:\Program Files\Java\jdk-21`)
- **Maven:** `Maven-3.9.6` (Configured to use bundled Maven Wrapper `./mvnw`)

---

## 2. CI Job Configuration (`job-config.xml`)

The Jenkins Job is configured to track the repository with scheduled polling triggers:
- **Repository URL:** `https://github.com/gayatri-patil/vaccination-reminder-portal.git`
- **Branch Specifier:** `*/main`
- **Build Trigger:** Poll SCM (`H/15 * * * *`) and GitHub webhook push events.
- **Build Step:** `Invoke top-level Maven targets`: `clean test package`
- **Post-Build Actions:**
  - Publish JUnit test result report: `target/surefire-reports/*.xml`
  - Archive the artifacts: `target/*.jar`

---

## 3. Trigger Evidence & Build Log

```text
Started by an SCM change
Running as SYSTEM
Building in workspace C:\ProgramData\Jenkins\.jenkins\workspace\Vaccination-Reminder-Portal-CI
The recommended git tool is: NONE
using credential github-patil-credentials
 > git.exe rev-parse --resolve-git-dir C:\ProgramData\Jenkins\.jenkins\workspace\Vaccination-Reminder-Portal-CI\.git # timeout=10
Fetching changes from the remote Git repository
 > git.exe config remote.origin.url https://github.com/gayatri-patil/vaccination-reminder-portal.git # timeout=10
Fetching upstream changes from https://github.com/gayatri-patil/vaccination-reminder-portal.git
 > git.exe --version # timeout=10
 > git --version # 'git version 2.44.0.windows.1'
 > git.exe fetch --tags --force --progress -- https://github.com/gayatri-patil/vaccination-reminder-portal.git +refs/heads/*:refs/remotes/origin/* # timeout=10
Checking out Revision b3b2fd1 (refs/remotes/origin/main)
 > git.exe config core.sparsecheckout # timeout=10
 > git.exe checkout -f b3b2fd1 # timeout=10
Commit message: "chore(release): merge develop into main for MVP release v1.0.0"

[Vaccination-Reminder-Portal-CI] $ cmd.exe /C "mvnw.cmd clean test package && exit %%ERRORLEVEL%%"
[INFO] Scanning for projects...
[INFO] 
[INFO] --------------< com.vaxguard:vaccination-reminder-portal >--------------
[INFO] Building vaccination-reminder-portal 1.0.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.3.2:clean (default-clean) @ vaccination-reminder-portal ---
[INFO] Deleting C:\ProgramData\Jenkins\.jenkins\workspace\Vaccination-Reminder-Portal-CI\target
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ vaccination-reminder-portal ---
[INFO] Copying 1 resource from src\main\resources to target\classes
[INFO] Copying 3 resources from src\main\resources to target\classes
[INFO] 
[INFO] --- compiler:3.13.0:compile (default-compile) @ vaccination-reminder-portal ---
[INFO] Compiling 9 source files with javac [debug parameters release 21] to target\classes
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ vaccination-reminder-portal ---
[INFO] Compiling 8 source files with javac [debug parameters release 21] to target\test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ vaccination-reminder-portal ---
[INFO] Running com.vaxguard.selenium.BeneficiaryRegistrationJourneyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.12 s
[INFO] Running com.vaxguard.selenium.SearchAndFilterJourneyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.41 s
[INFO] Running com.vaxguard.selenium.StatusWorkflowJourneyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.02 s
[INFO] Running com.vaxguard.selenium.DashboardMetricsJourneyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.88 s
[INFO] Running com.vaxguard.selenium.DefectSimulationJourneyTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.21 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] --- jar:3.4.1:jar (default-jar) @ vaccination-reminder-portal ---
[INFO] Building jar: C:\ProgramData\Jenkins\.jenkins\workspace\Vaccination-Reminder-Portal-CI\target\vaccination-reminder-portal-1.0.0.jar
[INFO] 
[INFO] --- spring-boot:3.3.4:repackage (repackage) @ vaccination-reminder-portal ---
[INFO] Replacing main artifact with repackaged archive, adding nested dependencies in BOOT-INF/.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] Total time:  32.145 s
[INFO] Finished at: 2026-10-22T14:24:00+05:30
[INFO] ------------------------------------------------------------------------

Archiving artifacts
Recording test results
Finished: SUCCESS
```

---

## 4. Archived Build Artifact Evidence

- **Artifact Name:** `vaccination-reminder-portal-1.0.0.jar`
- **Location:** `target/vaccination-reminder-portal-1.0.0.jar`
- **Fingerprint:** Generated and verified via Jenkins MD5 checksum.
- **Type:** Spring Boot Executable Archive with embedded Tomcat web server.
