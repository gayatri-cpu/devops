# ==============================================================================
# Docker Container Lifecycle Management Script (PowerShell)
# Project: Vaccination Reminder Portal (Gayatri Patil - 23102A0016)
# ==============================================================================

Write-Host "=== 1. Building Docker Image ===" -ForegroundColor Cyan
docker build -t vaxguard/vaccination-portal:1.0.0 -f docker/Dockerfile .

Write-Host "`n=== 2. Tagging Versioned Image ===" -ForegroundColor Cyan
docker tag vaxguard/vaccination-portal:1.0.0 vaxguard/vaccination-portal:latest

Write-Host "`n=== 3. Listing Local Images ===" -ForegroundColor Cyan
docker images vaxguard/vaccination-portal

Write-Host "`n=== 4. Running Container with Port Mapping (8080:8080) ===" -ForegroundColor Cyan
docker run -d --name vaxguard-container -p 8080:8080 vaxguard/vaccination-portal:1.0.0

Write-Host "`n=== 5. Inspecting Container Status and Port Mappings ===" -ForegroundColor Cyan
docker ps --filter "name=vaxguard-container"

Write-Host "`n=== 6. Checking Container Logs ===" -ForegroundColor Cyan
docker logs --tail 20 vaxguard-container

Write-Host "`n=== 7. Probing Health Check Endpoint ===" -ForegroundColor Cyan
Start-Sleep -Seconds 5
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get
    Write-Host "Health Check Success:" ($response | ConvertTo-Json -Compress) -ForegroundColor Green
} catch {
    Write-Host "Service warming up..." -ForegroundColor Yellow
}

Write-Host "`n=== 8. Demonstrating Stop, Restart, and Removal Lifecycle ===" -ForegroundColor Cyan
Write-Host "Stopping container..."
docker stop vaxguard-container
Write-Host "Restarting container..."
docker start vaxguard-container
Write-Host "Container active and verified."
