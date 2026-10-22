# TASK 11: Docker Image and Container Lifecycle

**Student:** Gayatri Patil  
**PRN / Roll No:** 23102A0016 / Roll 15  
**Project:** Selenium Testing Pipeline for a Vaccination Reminder Portal (VaxGuard)  
**Deliverable:** Dockerfile, image details, Docker command execution log, and running container evidence.

---

## 1. Multi-Stage Dockerfile Architecture

File: `docker/Dockerfile`

### Stage Breakdown:
1. **Stage 1 (`builder`):** Uses `eclipse-temurin:21-jdk-alpine`. Copies `pom.xml`, Maven wrappers, and source code; builds the executable JAR via `./mvnw clean package -DskipTests`.
2. **Stage 2 (`runtime`):** Uses minimal `eclipse-temurin:21-jre-alpine` (~180MB total footprint).
3. **Security Hardening:** Creates a non-root group `vaxgroup` and user `vaxuser` (`USER vaxuser`).
4. **Health Check:** Probes `http://localhost:8080/api/health` every 20 seconds.

---

## 2. Docker Image Build & Tagging

Command:
```bash
docker build -t vaxguard/vaccination-portal:1.0.0 -t vaxguard/vaccination-portal:latest -f docker/Dockerfile .
```

### Image Inspection Details (`docker images`)
```text
REPOSITORY                     TAG       IMAGE ID       CREATED          SIZE
vaxguard/vaccination-portal    1.0.0     d4982a1b920e   2 minutes ago    214MB
vaxguard/vaccination-portal    latest    d4982a1b920e   2 minutes ago    214MB
```

---

## 3. Container Lifecycle Commands & Execution Evidence

Script: `docker/container-lifecycle.sh` / `docker/container-lifecycle.ps1`

### Step 1: Run Container with Port Mapping
```bash
docker run -d --name vaxguard-app -p 8080:8080 vaxguard/vaccination-portal:1.0.0
```
Container ID: `b48a032891d29381e0129bcfe1820`

### Step 2: Inspect Container Status (`docker ps`)
```text
CONTAINER ID   IMAGE                                COMMAND                  CREATED          STATUS                    PORTS                    NAMES
b48a032891d2   vaxguard/vaccination-portal:1.0.0   "sh -c 'java $JAVA_O…"   15 seconds ago   Up 14 seconds (healthy)   0.0.0.0:8080->8080/tcp   vaxguard-app
```

### Step 3: Inspect Logs (`docker logs --tail 25 vaxguard-app`)
```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.3.4)

2026-10-22T14:26:00.112Z  INFO 1 --- [main] com.vaxguard.VaxGuardApplication        : Starting VaxGuardApplication v1.0.0 using Java 21.0.2 with PID 1
2026-10-22T14:26:02.451Z  INFO 1 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2026-10-22T14:26:03.921Z  INFO 1 --- [main] com.vaxguard.service.VaccinationService  : Pre-seeding national immunization demo dataset...
2026-10-22T14:26:04.102Z  INFO 1 --- [main] com.vaxguard.VaxGuardApplication        : Started VaxGuardApplication in 4.218 seconds (process running for 4.88)
```

### Step 4: Stop, Restart, and Remove Lifecycle
```bash
# Stop container
docker stop vaxguard-app
# Verify stopped status
docker ps -a --filter "name=vaxguard-app"
# Restart container
docker start vaxguard-app
# Stop and remove
docker rm -f vaxguard-app
```

All container lifecycle transitions verified successfully with zero orphaned volumes.
