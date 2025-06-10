#!/usr/bin/env pwsh

Write-Host "[DEV] Rebuilding 3forge (DEVELOPMENT MODE)..." -ForegroundColor Cyan

# Stop 3forge processes
Write-Host "Stopping 3forge..." -ForegroundColor Yellow
try {
    taskkill /F /IM AMI_One.exe 2>$null
    Start-Sleep -Seconds 2
} catch {
    Write-Host "3forge was not running" -ForegroundColor Gray
}

# Navigate to project root and build with development settings
$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot
Write-Host "Building and deploying (DEVELOPMENT)..." -ForegroundColor Yellow
gradle buildDev

if ($LASTEXITCODE -eq 0) {
    Write-Host "Starting 3forge in DEBUG MODE..." -ForegroundColor Green
    Set-Location "C:\Program Files\ami\amione"
    Start-Process -FilePath ".\AMI_One.exe" -WindowStyle Normal
    
    Write-Host "[SUCCESS] Done! Dashboard should be ready at:" -ForegroundColor Green
    Write-Host "   http://localhost:33332/3forge" -ForegroundColor White
    Write-Host ""
    Write-Host "[DEBUG] DEBUG MODE ACTIVE:" -ForegroundColor Yellow
    Write-Host "   Remote debugging port: 5005" -ForegroundColor White
    Write-Host "   Attach debugger from VS Code!" -ForegroundColor White
} else {
    Write-Host "[ERROR] Build failed!" -ForegroundColor Red
    exit 1
} 