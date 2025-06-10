#!/usr/bin/env pwsh

Write-Host "Rebuilding 3forge (PRODUCTION MODE)..." -ForegroundColor Green

# Stop 3forge processes
Write-Host "Stopping 3forge..." -ForegroundColor Yellow
taskkill /F /IM AMI_One.exe /T 2>$null
Start-Sleep -Seconds 3

# Build and deploy
Write-Host "Building and deploying..." -ForegroundColor Yellow
gradle buildProd

if ($LASTEXITCODE -eq 0) {
    Write-Host "Starting 3forge..." -ForegroundColor Green
    Set-Location "C:\Program Files\ami\amione"
    Start-Process -FilePath ".\AMI_One.exe" -WindowStyle Normal
    
    Write-Host "Done! Dashboard should be ready at:" -ForegroundColor Green
    Write-Host "   http://localhost:33332/3forge" -ForegroundColor White
} else {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
} 