#!/usr/bin/env powershell

Write-Host "Killing all 3forge processes..." -ForegroundColor Red

# Kill all AMI_One.exe processes
$processes = Get-Process -Name "AMI_One" -ErrorAction SilentlyContinue
if ($processes) {
    $processes | ForEach-Object {
        Write-Host "  Killing AMI_One.exe (PID: $($_.Id))" -ForegroundColor Yellow
        Stop-Process -Id $_.Id -Force
    }
    Write-Host "All AMI_One processes killed" -ForegroundColor Green
} else {
    Write-Host "No AMI_One processes found" -ForegroundColor Cyan
}

# Also kill any java processes that might be related
$javaProcesses = Get-Process -Name "java" -ErrorAction SilentlyContinue | Where-Object {
    $_.MainWindowTitle -like "*3forge*" -or $_.MainWindowTitle -like "*AMI*"
}
if ($javaProcesses) {
    $javaProcesses | ForEach-Object {
        Write-Host "  Killing Java process (PID: $($_.Id)): $($_.MainWindowTitle)" -ForegroundColor Yellow
        Stop-Process -Id $_.Id -Force
    }
}

Write-Host "Process cleanup complete!" -ForegroundColor Green 