# Task 12: Jenkins-Docker Continuous Deployment
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 15  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To extend the Vaccination Reminder Portal Jenkins pipeline to build a versioned Docker image, publish/tag it locally or to a container registry, deploy a fresh container automatically after successful Selenium tests, and verify continuous end-to-end commit-to-container deployment.

---

### 2. Syllabus Deliverable Requirement
> *"Versioned image, registry evidence and end-to-end commit-to-container pipeline."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain the Commit-to-Container Pipeline
- **Speech Script:**
  > *"Madam/Sir, in Task 12, I extended our Jenkins pipeline to achieve Continuous Deployment (CD). As soon as the Selenium tests pass in Stage 4, Stage 6 automatically builds a versioned Docker image (`vaxguard/vaccination-portal:1.0.0`). Stage 7 safely replaces the existing container with the new version on port 8080, and Stage 8 performs an automated health check against `/api/health`."*

#### Step 2: Show the Docker Deployment Pipeline Stages
Open file: [`jenkins/Jenkinsfile`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/jenkins/Jenkinsfile)  
Point to lines 75–115:
```groovy
stage('6. Docker Image Build & Tag') {
    steps {
        echo "=== Stage 6: Building Versioned Docker Container Image ==="
        sh "docker build -t ${DOCKER_IMAGE} -t vaxguard/vaccination-portal:latest -f docker/Dockerfile ."
    }
}

stage('7. Continuous Deployment (Docker / Server)') {
    steps {
        echo "=== Stage 7: Deploying Fresh Container to ${params.ENVIRONMENT} ==="
        script {
            sh """
                docker stop ${CONTAINER_NAME} || true
                docker rm ${CONTAINER_NAME} || true
                docker run -d --name ${CONTAINER_NAME} \
                    -p ${params.APP_PORT}:8080 \
                    -e SPRING_PROFILES_ACTIVE=${params.ENVIRONMENT} \
                    ${DOCKER_IMAGE}
            """
        }
    }
}

stage('8. Automated Health Check Verification') {
    steps {
        echo "=== Stage 8: Verifying Application Health Readiness ==="
        sleep 8
        sh "curl -f http://localhost:${params.APP_PORT}/api/health || exit 1"
    }
}
```

#### Step 3: Show Docker Compose Orchestration
Open file: [`docker/docker-compose.yml`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docker/docker-compose.yml)  
Point out:
- Image: `vaxguard/vaccination-portal:1.0.0`
- Container Name: `vaxguard-app`
- Port Mapping: `8080:8080`
- Automated Healthcheck: `CMD wget -qO- http://localhost:8080/api/health`

#### Step 4: Show Versioned Image Details
Show the local image registry output in [`docs/TASK-12-Jenkins-Docker-Continuous-Deployment.md`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docs/TASK-12-Jenkins-Docker-Continuous-Deployment.md):
```text
REPOSITORY                     TAG       IMAGE ID       SIZE
vaxguard/vaccination-portal    1.0.0     d4982a1b920e   214MB
vaxguard/vaccination-portal    latest    d4982a1b920e   214MB
```

---

### 4. Likely Viva Voce Questions & Answers for Task 12

**Q1: What is the difference between Continuous Delivery and Continuous Deployment?**  
*Answer:* In Continuous Delivery, software passes automated builds and tests and is packaged into release-ready artifacts, but deployment to production requires manual human approval. In Continuous Deployment, every change that passes all automated quality gates is deployed directly to production with zero manual intervention.

**Q2: How does your pipeline ensure that a broken Docker container is never deployed?**  
*Answer:* Stage 4 (Selenium Quality Gate) executes *before* Stage 6 (Docker Build) and Stage 7 (Deployment). If any Selenium test fails, the pipeline aborts immediately, preventing the Docker image from ever being built or deployed.

**Q3: Why is health check verification necessary after deployment?**  
*Answer:* Just because a container starts (`docker run`) does not guarantee that the application inside is ready to accept user requests (e.g., database connection might be initializing). Stage 8 verifies HTTP 200 from `/api/health` before marking the deployment as successful.
