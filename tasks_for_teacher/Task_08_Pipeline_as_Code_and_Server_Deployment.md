# Task 08: Pipeline as Code and Server Deployment
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 15  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To create a declarative Jenkinsfile for the Vaccination Reminder Portal covering checkout, build, compile, Selenium automated test gate, packaging, and server deployment stages, deploy the application to Tomcat / container server, parameterize deployment environment settings (`staging`/`production`), and verify the deployed application.

---

### 2. Syllabus Deliverable Requirement
> *"Jenkinsfile, successful pipeline run, deployed application URL/screenshot and configuration evidence."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain Declarative Pipeline as Code
- **Speech Script:**
  > *"Madam/Sir, in Task 8, I codified our deployment pipeline into a declarative `Jenkinsfile` placed in the root of our repository. It defines 8 sequential stages and accepts parameters for target environment (`staging` or `production`), port numbers, and Docker image tags. It guarantees that our deployment logic is version-controlled in Git alongside our application source code."*

#### Step 2: Show the Jenkinsfile Stages
Open file: [`jenkins/Jenkinsfile`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/jenkins/Jenkinsfile)  
Walk through the 8 stages:
1. `Stage 1: Checkout SCM` - Pulls branch from Git
2. `Stage 2: Environment Verification` - Validates Java 21, Maven, Docker
3. `Stage 3: Compile & Static Analysis` - Clean compilation
4. `Stage 4: Selenium Automated Testing Quality Gate` - Headless test execution
5. `Stage 5: Package Application Artifact` - Generates executable JAR
6. `Stage 6: Docker Image Build & Tag` - Builds versioned container image
7. `Stage 7: Continuous Deployment` - Deploys to target environment
8. `Stage 8: Automated Health Check Verification` - Validates HTTP 200 OK

#### Step 3: Show the Parameterized Pipeline Settings
Point to lines 15–22 of `Jenkinsfile`:
```groovy
parameters {
    choice(name: 'ENVIRONMENT', choices: ['staging', 'production'], description: 'Deployment Target Environment')
    string(name: 'APP_PORT', defaultValue: '8080', description: 'Application Port Mapping')
    booleanParam(name: 'RUN_SELENIUM_TESTS', defaultValue: true, description: 'Execute Selenium Automated UI Quality Gate')
    string(name: 'DOCKER_IMAGE_TAG', defaultValue: '1.0.0', description: 'Docker Release Tag')
}
```

#### Step 4: Show the Deployed Application URL and Health Proof
Run in PowerShell:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get
```
Point to the verified status:
```json
{
  "status": "UP",
  "service": "Vaccination Reminder Portal (VaxGuard)",
  "version": "1.0.0",
  "author": "Gayatri Patil (PRN: 23102A0016, Roll: 15)",
  "database": "H2 In-Memory (Connected)",
  "qualityGate": "PASSED"
}
```

---

### 4. Likely Viva Voce Questions & Answers for Task 8

**Q1: What is the difference between Scripted Pipeline and Declarative Pipeline?**  
*Answer:* Scripted Pipeline uses Groovy procedural scripting with fewer syntactic constraints. Declarative Pipeline provides a more structured, readable, and opinionated syntax with predefined sections (`pipeline`, `stages`, `steps`, `post`, `parameters`). Declarative is the modern industry standard for Jenkins pipelines.

**Q2: What is the purpose of the `post` block in a Jenkinsfile?**  
*Answer:* The `post` block defines actions that execute automatically based on the stage or pipeline outcome: `always` (e.g., archiving JUnit reports and screenshots), `success` (e.g., notification alerts), or `failure` (e.g., notifying developers of a broken build).

**Q3: How is environment parameterization useful?**  
*Answer:* Parameterization allows the same Jenkinsfile to deploy code to different environments (such as `staging` on port 8080 or `production` on port 80) simply by selecting a dropdown option in the Jenkins UI, without needing separate scripts.
