# Task 11: Docker Image and Container Lifecycle
## DevOps Laboratory Assignment & Viva Voce Document

**Student Name:** Gayatri Patil  
**PRN / Student ID:** 23102A0016  
**Roll Number:** 15  
**Class / Semester:** TE Computer Engineering / DevOps Lab  
**Project Title:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  

---

### 1. Aim of the Experiment
To create a multi-stage Dockerfile for the Vaccination Reminder Portal, build and tag the Docker image (`vaxguard/vaccination-portal:1.0.0`), run the container with port mapping (`8080:8080`), inspect runtime logs, verify container health, execute the complete container lifecycle (run, stop, start, restart, remove), and document the lifecycle commands.

---

### 2. Syllabus Deliverable Requirement
> *"Dockerfile, image details, Docker command log and running container evidence."*

---

### 3. Step-by-Step Guide: What to Show the Teacher

#### Step 1: Explain the Multi-Stage Dockerfile Architecture
- **Speech Script:**
  > *"Madam/Sir, in Task 11, I containerized VaxGuard using a production-ready multi-stage Dockerfile. Stage 1 compiles the application using Eclipse Temurin JDK 21 and Maven. Stage 2 copies only the packaged fat JAR into a minimal Eclipse Temurin JRE 21 Alpine image. I secured the container by running under an unprivileged user `vaxuser` rather than root, and configured an automated health check."*

#### Step 2: Show the Dockerfile Code
Open file: [`docker/Dockerfile`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docker/Dockerfile)  
Point out key production lines:
```dockerfile
# STAGE 1: Builder
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build
COPY pom.xml mvnw ./
COPY src ./src
RUN ./mvnw clean package -DskipTests

# STAGE 2: Lightweight Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Non-root user creation
RUN addgroup -S vaxgroup && adduser -S vaxuser -G vaxgroup
COPY --from=builder /build/target/vaccination-reminder-portal-1.0.0.jar app.jar
RUN chown -R vaxuser:vaxgroup /app
USER vaxuser

EXPOSE 8080

HEALTHCHECK --interval=20s --timeout=5s --retries=3 \
  CMD wget --spider http://localhost:8080/api/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Step 3: Show the Complete Container Lifecycle Script
Open file: [`docker/container-lifecycle.ps1`](file:///c:/Users/ishwar/Desktop/devops%20gayatri/docker/container-lifecycle.ps1)  
Explain the 8 lifecycle commands demonstrated:
1. `docker build -t vaxguard/vaccination-portal:1.0.0 -f docker/Dockerfile .` (Builds image)
2. `docker tag vaxguard/vaccination-portal:1.0.0 vaxguard/vaccination-portal:latest` (Tags latest)
3. `docker images vaxguard/vaccination-portal` (Lists local image details: ~214MB)
4. `docker run -d --name vaxguard-container -p 8080:8080 vaxguard/vaccination-portal:1.0.0` (Runs with port mapping)
5. `docker ps --filter "name=vaxguard-container"` (Inspects container status: `Up (healthy)`)
6. `docker logs --tail 20 vaxguard-container` (Inspects Spring Boot startup logs)
7. `docker stop vaxguard-container` (Gracefully stops container)
8. `docker start vaxguard-container` (Restarts container)

---

### 4. Likely Viva Voce Questions & Answers for Task 11

**Q1: What are the advantages of a multi-stage Docker build?**  
*Answer:* Multi-stage builds keep intermediate build dependencies (like JDK 21 compiler, Maven plugins, and source code) out of the final runtime image. This dramatically shrinks image size (from ~800MB to ~214MB) and hardens container security by eliminating compilers and debuggers from production.

**Q2: Why should containers not run as the root user?**  
*Answer:* If an attacker exploits an application vulnerability inside a container running as root, they could potentially escape the container and gain root administrative access over the host machine. Creating a dedicated non-root user (`vaxuser:vaxgroup`) prevents this privilege escalation.

**Q3: What does Docker port mapping (`-p 8080:8080`) do?**  
*Answer:* The `-p <host_port>:<container_port>` flag forwards network traffic hitting port 8080 on the host machine to port 8080 inside the container's isolated network namespace, allowing external browsers to access the portal.
