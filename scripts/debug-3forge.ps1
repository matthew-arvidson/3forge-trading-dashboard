#!/usr/bin/env powershell

# 3forge Debug Mode Startup Script
# Enables remote debugging so we can inspect actual 3forge objects

Write-Host "🐞 Starting 3forge in DEBUG mode..." -ForegroundColor Cyan

# Stop any existing 3forge processes
Write-Host "Stopping existing 3forge processes..." -ForegroundColor Yellow
Get-Process | Where-Object {$_.ProcessName -like "*AMI*"} | Stop-Process -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 3

# Build and deploy first
Write-Host "Building and deploying classes..." -ForegroundColor Yellow
gradle clean build deploy
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Build failed! Cannot start debug mode." -ForegroundColor Red
    exit 1
}

# Change to 3forge directory
Set-Location "C:\Program Files\ami\amione"

# Enable remote debugging on port 5005
Write-Host "🔗 Enabling remote debugging on port 5005..." -ForegroundColor Green

# Start 3forge with debug flags
# suspend=n means don't wait for debugger to connect
$debugArgs = @(
    "-Xdebug",
    "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)

Write-Host "🚀 Starting AMI_One.exe with debugging..." -ForegroundColor Green

# Start the process with debug arguments
Start-Process -FilePath ".\AMI_One.exe" -ArgumentList $debugArgs -WindowStyle Normal

# Wait for startup
Start-Sleep -Seconds 8

# Return to original directory
Set-Location $PSScriptRoot

Write-Host "✅ 3forge started in DEBUG mode!" -ForegroundColor Green
Write-Host "" 
Write-Host "🔗 Connect your IDE debugger to:" -ForegroundColor Cyan
Write-Host "   Host: localhost" -ForegroundColor White
Write-Host "   Port: 5005" -ForegroundColor White
Write-Host ""
Write-Host "🎯 Dashboard available at:" -ForegroundColor Cyan  
Write-Host "   http://localhost:33332/3forge" -ForegroundColor White
Write-Host ""
Write-Host "🐛 Next steps:" -ForegroundColor Yellow
Write-Host "   1. Connect debugger to localhost:5005" -ForegroundColor White
Write-Host "   2. Set breakpoints in TradingDashboardManager.java" -ForegroundColor White
Write-Host "   3. Load dashboard and click a trader name" -ForegroundColor White
Write-Host "   4. Inspect layout and panel objects when breakpoint hits" -ForegroundColor White 