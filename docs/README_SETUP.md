# 3forge Trading Dashboard - Developer Setup Guide

## 🚀 Quick Start (15 minutes to running)

### Prerequisites
- **Windows 10/11** 
- **Java 17** (or compatible)
- **3forge AMI installed** with valid license
- **Git** for cloning the repository

---

## Step 1: Clone and Initial Setup

```bash
git clone [repository-url]
cd 3forge-trading-dashboard
```

---

## Step 2: Create Local Configuration

Create `local.properties` in the project root:

```properties
# Add these lines to register our custom Java classes
ami.web.amiscript.custom.classes=com.demo.TestClass,com.forge.trading.TradingDashboardManager
ami.center.amiscript.custom.classes=com.demo.TestClass,com.forge.trading.TradingDashboardManager
```

⚠️ **Important**: This file is git-ignored because it contains local paths.

---

## Step 3: Add 3forge JAR Dependencies

Copy these files from your 3forge installation to `libs/` folder:

```
FROM: [Your 3forge install]/lib/
TO: project/libs/

Copy these files:
- out.jar
- autocode.jar
```

**Example**: If 3forge is at `C:\Program Files\ami\amione\`, copy from:
- `C:\Program Files\ami\amione\lib\out.jar` → `libs/out.jar`
- `C:\Program Files\ami\amione\lib\autocode.jar` → `libs/autocode.jar`

---

## Step 4: Build and Deploy

```bash
# Build the Java classes and deploy to 3forge
gradle clean build deploy

# Start 3forge with the new classes
.\rebuild-3forge.ps1
```

⚠️ **Note**: Update the path in `rebuild-3forge.ps1` to match your 3forge installation.

---

## Step 5: Open Dashboard

3forge should auto-open to the "dashboard poc" layout. If not:
1. File → Open Layout → "dashboard poc"
2. Click on trader names in the table to test inter-widget communication

---

## 🎯 You Should See:

✅ **4-panel dashboard**: trades table, P&L chart, market heatmap, HTML metrics  
✅ **Working table→chart filtering**: Click trader name, chart updates  
✅ **Custom Java classes working**: Check logs for "TradingDashboardManager is working!"  

---

## 🛠️ Development Workflow

```bash
# After making Java code changes:
gradle clean build deploy    # Build and deploy JAR
.\rebuild-3forge.ps1         # Restart 3forge
# Test your changes immediately
```

---

## 📂 Project Structure

```
project/
├── src/main/java/com/forge/trading/
│   └── TradingDashboardManager.java     # Main business logic
├── 3forge.json                         # Dashboard layout configuration  
├── build.gradle                        # Build configuration
├── rebuild-3forge.ps1                  # 3forge restart automation
├── local.properties                    # Your local config (git-ignored)
├── libs/                               # 3forge JAR dependencies
│   ├── out.jar                         # (you copy these)
│   └── autocode.jar                    # (you copy these)
└── database.sqlite                     # Sample trading data
```

---

## 🐛 Troubleshooting

### "Classes not found" error:
- Check `local.properties` has the custom class registration
- Verify JAR deployed to `C:\Program Files\ami\amione\lib\3forge-trading-dashboard.jar`
- Restart 3forge completely

### "Build failed" error:
- Ensure `libs/out.jar` and `libs/autocode.jar` exist
- Check Java version compatibility
- Try `gradle clean` before building

### "Click functionality not working":
- Check 3forge logs for errors
- Verify the dashboard layout opened correctly
- Try clicking different trader names in the table

---

## 🎯 What Works Currently

✅ **Table → Chart Communication**: Click trader, chart filters perfectly  
🔄 **Heatmap Updates**: Next to implement  
🔄 **HTML Panel Updates**: Next to implement  

---

## 📝 Need Help?

- Check the main `README.md` for technical details and progress notes
- Review the Java code in `src/main/java/com/forge/trading/`
- Ask the team for 3forge installation guidance if needed

**Happy coding! 🚀** 

# Emergency Recovery Procedures

## 🚨 Nuclear Option - Complete Clean Install

**When to use:** If AMI installation becomes corrupted and basic functionality (like table clicking) stops working even with fresh installs.

### Step 1: Complete Removal
```powershell
# Stop all AMI processes
taskkill /F /IM AMI_One.exe /T

# Nuclear removal of all AMI files
Remove-Item -Path "C:\Program Files\ami" -Recurse -Force

# Clean any remaining config directories
Remove-Item -Path "$env:APPDATA\ami" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$env:LOCALAPPDATA\ami" -Recurse -Force -ErrorAction SilentlyContinue
```

### Step 2: Fresh Installation
1. Download fresh AMI installer
2. Run installer as Administrator
3. **Test basic functionality FIRST** before any customizations

### Step 3: Restore Data Sources
⚠️ **CRITICAL:** Fresh installs lose data source configurations!

**After install, you must re-add:**
1. **SQLite data source** pointing to your `trading_data.db`
2. **MockData** data source reference
3. **Any other custom data sources**

**Common error if skipped:** Dashboard loads but shows no data

### Step 4: Restore Customizations
Only after confirming basic functionality works:

```bash
# Smart restore of just your customizations
gradle smartRestore

# Or manual restoration
gradle deployConfig    # Deploy your custom classes
gradle deploy          # Deploy JARs
```

## 🛡️ Prevention

**Always create smart backup before major changes:**
```bash
gradle smartBackup
```

**Test configuration changes incrementally:**
1. Deploy one change at a time
2. Test functionality after each change  
3. Create backup checkpoint when working
4. Never deploy multiple changes at once

**Signs you might need nuclear option:**
- Basic table clicking stops working
- Fresh install doesn't fix the issue
- Configuration restore doesn't help
- AMI behaves inconsistently across restarts

---

*Nuclear option documentation added after discovering corrupted AMI installation state that persisted across "fresh" installs.* 