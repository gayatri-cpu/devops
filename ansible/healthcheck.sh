#!/usr/bin/env bash
# ==============================================================================
# Automated Application Health Check Prober
# ==============================================================================
set -e

ENDPOINT=${1:-"http://localhost:8080/api/health"}
echo "Probing Health Endpoint: $ENDPOINT"

HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$ENDPOINT" || echo "000")

if [ "$HTTP_STATUS" -eq 200 ]; then
    echo "[HEALTH CHECK PASSED] HTTP 200 OK"
    curl -s "$ENDPOINT" | jq . || curl -s "$ENDPOINT"
    exit 0
else
    echo "[HEALTH CHECK FAILED] Expected HTTP 200, Received HTTP $HTTP_STATUS"
    exit 1
fi
