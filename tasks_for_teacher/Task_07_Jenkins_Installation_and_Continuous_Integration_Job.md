# Task 07: Jenkins Installation and Continuous Integration Job
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 15  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To install and configure Jenkins Continuous Integration server, configure Global Tools (JDK 21, Maven 3.9), connect the Vaccination Reminder Portal Git repository, create an automated build job triggered by commit or scheduled polling, and archive the build artifact.

---

### 2. Syllabus Deliverable Requirement
> *"Configured Jenkins job, successful build log, trigger evidence and archived artefact."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain Jenkins Setup to Teacher
- **Speech Script:**
  > *"Madam/Sir, in Task 7, I set up Jenkins continuous integration. I defined Global Tools for Java 21 and Maven, and configured an automated build job that tracks our repository. When code is pushed or when scheduled polling detects a new commit, Jenkins automatically pulls the code, compiles it, runs test suites, and archives the resulting JAR file."*

#### Step 2: Show the Jenkins Job Configuration File
Open file: [`jenkins/job-config.xml`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/jenkins/job-config.xml)  
Point out:
- **Git SCM Connection:** `<url>https://github.com/gayatri-patil/vaccination-reminder-portal.git</url>`
- **Branch Specifier:** `*/main`
- **Polling Trigger:** `<spec>H/15 * * * *</spec>` (Every 15 minutes)
- **Pipeline Script Path:** `<scriptPath>jenkins/Jenkinsfile</scriptPath>`

#### Step 3: Show the Successful Build Log Evidence
Show the execution log in [`docs/TASK-07-Jenkins-Installation-and-Continuous-Integration-Job.md`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docs/TASK-07-Jenkins-Installation-and-Continuous-Integration-Job.md):
```text
Started by an SCM change
Checking out Revision b3b2fd1 (refs/remotes/origin/main)
[Vaccination-Reminder-Portal-CI] $ cmd.exe /C "mvnw.cmd clean test package && exit %%ERRORLEVEL%%"
[INFO] Scanning for projects...
[INFO] Building vaccination-reminder-portal 1.0.0
[INFO] --- surefire:3.2.5:test (default-test) @ vaccination-reminder-portal ---
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] Building jar: target/vaccination-reminder-portal-1.0.0.jar
[INFO] BUILD SUCCESS
Archiving artifacts: target/*.jar
Recording test results: target/surefire-reports/*.xml
Finished: SUCCESS
```

#### Step 4: Show the Archived Artifact
- Point to file: [`target/vaccination-reminder-portal-1.0.0.jar`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/target/)
- Explain that this is a fully packaged, self-contained Spring Boot executable fat JAR containing all embedded web server and application dependencies.

---

### 4. Likely Viva Voce Questions & Answers for Task 7

**Q1: What is Continuous Integration (CI)?**  
*Answer:* Continuous Integration is a software development practice where developers frequently merge code changes into a central repository. An automated build server (like Jenkins) automatically compiles the code and executes automated tests on every push to detect integration errors early.

**Q2: What is the difference between Webhooks and SCM Polling?**  
*Answer:* SCM Polling is pull-based: Jenkins periodically queries the Git server (e.g., every 15 minutes) to see if new commits exist. A Webhook is push-based: GitHub immediately sends an HTTP POST event to Jenkins the instant a developer pushes code, triggering the build without delay.

**Q3: What does "Archiving an Artifact" mean in Jenkins?**  
*Answer:* Archiving preserves the output binaries produced during a build (such as `.jar` or `.war` files) inside Jenkins storage. This allows teams to download, audit, or deploy a specific build artifact later without having to recompile the source code.
