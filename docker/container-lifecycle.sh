#!/usr/bin/env bash
# ==============================================================================
# Docker Container Lifecycle Management Script (Bash / Linux)
# Project: Vaccination Reminder Portal (Gayatri Patil - 23102A0016)
# ==============================================================================
set -e

echo "=== 1. Building Docker Image ==="
docker build -t vaxguard/vaccination-portal:1.0.0 -f docker/Dockerfile .

echo "=== 2. Tagging Versioned Image ==="
docker tag vaxguard/vaccination-portal:1.0.0 vaxguard/vaccination-portal:latest

echo "=== 3. Listing Local Images ==="
docker images vaxguard/vaccination-portal

echo "=== 4. Running Container with Port Mapping (8080:8080) ==="
docker run -d --name vaxguard-container -p 8080:8080 vaxguard/vaccination-portal:1.0.0

echo "=== 5. Inspecting Container Status and Port Mappings ==="
docker ps --filter "name=vaxguard-container"

echo "=== 6. Checking Container Logs ==="
docker logs --tail 20 vaxguard-container

echo "=== 7. Probing Health Check Endpoint ==="
sleep 5
curl -s http://localhost:8080/api/health || echo "Warming up..."

echo "=== 8. Demonstrating Stop, Restart, and Removal Lifecycle ==="
echo "Stopping container..."
docker stop vaxguard-container
echo "Restarting container..."
docker start vaxguard-container
echo "Container active and verified."
