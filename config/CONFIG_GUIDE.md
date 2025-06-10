# 3forge Configuration Management Guide

## Current Status: Production Ready

**Completed Features:**
- 4-Panel Inter-Widget Communication: Complete trader selection updates all panels
- Dynamic HTML Panel Generation: Real-time trader performance cards  
- Smart Heatmap Filtering: Trader-specific symbol sets
- Professional Demo Data: Realistic trader-symbol separation
- Automated Build System: Dev/prod deployment pipeline
- Comprehensive Logging: Debug-ready filter operations
- Master Backup System: Bulletproof recovery mechanism

**Production Capabilities:**
- Click any trader → All 4 panels update instantly
- Professional CSS-styled performance metrics
- Realistic trading data with symbol preferences
- Complete reset functionality (click non-trader columns)
- Automated deployment with gradle buildProd

## Directory Structure

```
config/
├── CONFIG_GUIDE.md             # This file
├── ami-baseline/               # Clean AMI installation backup
│   ├── 3fsecurity.properties  # Baseline security config
│   └── build.properties       # Baseline build config
├── ami-custom.properties       # Our custom class registrations
├── working-state-backup/       # Single master backup (current working state)
│   ├── config/                # Configuration files
│   ├── data/                  # Data sources and schemas  
│   └── lib/                   # Custom JARs and licenses
└── ami-backups/               # Legacy timestamped backups (deprecated)
```

## Gradle Build System

### Master Backup System (Recommended)

**`gradle backupWorkingState`** - Create single master backup of current working state:
- Backs up configuration directory
- Backs up data directory (data sources, schemas)
- Backs up custom JARs and licenses  
- Only ~2.6MB vs 104MB full install
- Single master backup - no accumulation
- Golden state preservation - saves your working configuration

**`gradle restoreWorkingState`** - Complete recovery option:
- Restores from master backup
- Complete working state recovery
- Fast recovery - seconds not minutes
- Safe - preserves data sources

### Development Workflow 

```bash
# 1. CREATE MASTER BACKUP (when everything works perfectly)
gradle backupWorkingState

# 2. Test new features safely
gradle buildProd         # Deploy your changes
.\scripts\rebuild-prod.ps1   # Test in 3forge

# 3. IF SOMETHING BREAKS (recovery)
gradle restoreWorkingState
.\scripts\rebuild-prod.ps1   # Back to working state

# 4. IF EVERYTHING WORKS (update master backup)
gradle backupWorkingState  # New golden state
```

### Dual Build System (Development vs Production)

**`gradle buildDev`** - Development Mode (debugging enabled):
- Enables remote debugging on port 5005
- Includes debug VM options
- Optimized for development workflow
- Easy attachment with VS Code debugger

**`gradle buildProd`** - Production Mode (optimized):
- Optimized VM options for performance
- No debugging overhead
- Clean production deployment
- Ready for production use

**`gradle rebuild`** - Alias to buildDev (default development):
- Same as `gradle buildDev`
- Maintains backward compatibility
- Default for active development

### Script Integration

**PowerShell Scripts for Quick Development:**
- `scripts/rebuild-dev.ps1` - Full dev rebuild + 3forge restart
- `scripts/rebuild-prod.ps1` - Full prod rebuild + 3forge restart

**Optimized Workflow Examples:**
```bash
# Development with debugging
gradle buildDev
.\scripts\rebuild-dev.ps1

# Production deployment
gradle buildProd  
.\scripts\rebuild-prod.ps1

# Quick development (default)
gradle rebuild
.\scripts\rebuild-dev.ps1
```

### Legacy Backup Tasks (Still Available)
- **`gradle smartBackup`** - Create lightweight timestamped backup
- **`gradle smartRestore`** - Restore from latest smart backup
- **`gradle backupConfig`** - Backup AMI config with timestamp
- **`gradle deployConfig`** - Deploy only configuration files  
- **`gradle restoreConfig`** - Restore clean baseline configuration
- **`gradle cleanDeploy`** - Remove all deployed files and configs

## Recommended Approach: Master Backup System

### Why Master Backup is Better:
- Single source of truth - One master backup, not accumulating files
- Includes data sources - Preserves your configured data connections
- Working state focus - Backs up when things work perfectly
- Complete recovery - Always get back to a working state
- Space efficient - ~2.6MB vs 104MB full install
- Fast recovery - Seconds not minutes

### When to Use Master Backup:
```bash
# BACKUP: After major milestones
- Table clicking works perfectly
- Custom Java classes deployed  
- Data sources configured
- Dashboard fully functional

# RESTORE: When things break
- Custom Java class breaks table clicking
- Configuration gets corrupted
- Data sources disappear
- Any breaking change
```

### Enterprise Recovery Strategy:
1. **Master backup** = Your working state (use this 90% of the time)
2. **Baseline backup** = Clean AMI install (use for fresh starts)
3. **Nuclear option** = Complete AMI reinstall (requires new license)

## Custom Java Class Registration

### Overview
Complete process for registering custom Java classes with 3forge AMI, including troubleshooting steps and solutions.

### Working Configuration

**Step 1: Create Custom Java Class**
```java
package com.forge.trading;
import com.f1.ami.amicommon.customobjects.AmiScriptAccessible;

@AmiScriptAccessible(name = "TradingDashboardManager")
public class TradingDashboardManager {
    
    @AmiScriptAccessible(name = "debugInfo")
    public String debugInfo() {
        return "TradingDashboardManager is working! Version 1.0";
    }
}
```

**Step 2: Compile to JAR**
```bash
gradle buildProd  # Creates trading-dashboard.jar in config/lib/
```

**Step 3: Register in ami-custom.properties**
```properties
# Custom class registrations
ami.script.custom.objects.TradingDashboardManager=com.forge.trading.TradingDashboardManager
```

**Step 4: Deploy Configuration**
```bash
gradle buildProd  # Deploys JAR + properties to AMI installation
```

**Step 5: Test in AMI Script**
```javascript
// Test custom class access
TradingDashboardManager manager = ami.getTradingDashboardManager();
session.log("Debug Info: " + manager.debugInfo());
```

### Class Registration Patterns

**Single Class Registration:**
```properties
ami.script.custom.objects.ClassName=com.package.ClassName
```

**Multiple Class Registration:**
```properties
ami.script.custom.objects.TradingDashboardManager=com.forge.trading.TradingDashboardManager
ami.script.custom.objects.PanelManager=com.forge.trading.PanelManager
ami.script.custom.objects.DataUtils=com.forge.trading.utils.DataUtils
```

### Troubleshooting Guide

**Common Issues and Solutions:**

1. **Class Not Found in AMI Script**
   - Verify JAR deployment: Check `C:\Program Files\ami\amione\lib\` for your JAR
   - Verify properties deployment: Check `C:\Program Files\ami\amione\config\` for properties file
   - Restart 3forge completely after deployment

2. **Method Not Accessible**
   - Ensure `@AmiScriptAccessible` annotation on class and methods
   - Check method parameter types - use simple types (String, int, boolean)
   - Complex objects require proper `@AmiScriptAccessible(params = {...})` configuration

3. **JAR Deployment Issues**
   - Run `gradle clean buildProd` to ensure fresh compilation
   - Check gradle output for deployment confirmation
   - Verify JAR file size - empty JARs indicate compilation issues

4. **Properties File Issues**
   - Ensure proper syntax: `ami.script.custom.objects.Name=com.package.Class`
   - No spaces around equals sign
   - Case-sensitive class names and packages

### Advanced Class Development

**Method Parameter Handling:**
```java
@AmiScriptAccessible(name = "generateReport", params = {"traderId", "dateRange"})
public String generateReport(Object traderId, Object dateRange) {
    // Handle Object parameters from AMI script
    String trader = traderId.toString();
    String range = dateRange.toString();
    // ... implementation
}
```

**Layout Object Integration:**
```java
@AmiScriptAccessible(name = "updateDashboard", params = {"layout", "data"})
public boolean updateDashboard(Object layout, Object data) {
    // Access 3forge layout components
    // Use reflection or direct casting as needed
    return true;
}
```

**Error Handling Best Practices:**
```java
@AmiScriptAccessible(name = "safeOperation")
public String safeOperation(Object input) {
    try {
        // Your logic here
        return "SUCCESS: " + result;
    } catch (Exception e) {
        return "ERROR: " + e.getMessage();
    }
}
```

## Development Best Practices

### Safe Development Cycle
1. **Create master backup** when everything works
2. **Develop new features** in isolated branches/commits
3. **Test thoroughly** before committing to master backup
4. **Update master backup** only after verification
5. **Document changes** in configuration files

### Configuration File Management
- Keep `ami-custom.properties` in version control
- Use meaningful class names for easy identification
- Document complex class relationships
- Maintain backward compatibility when possible

### Deployment Automation
- Use gradle tasks for consistent deployment
- Verify deployment success before testing
- Maintain deployment logs for troubleshooting
- Automate 3forge restart after deployment

This guide provides the foundation for robust 3forge application development with proper configuration management and recovery procedures.