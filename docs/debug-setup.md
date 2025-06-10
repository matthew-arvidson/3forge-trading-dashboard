# 3forge Java Debug Setup Guide

## 🎯 Goal: Debug Silent AMI Script Failures

Instead of guessing with reflection, we'll use a Java debugger to:
- See exactly where AMI script callbacks fail
- Inspect real 3forge objects (layout, panel) at runtime
- Discover actual available methods on panels
- Get stack traces for silent failures

## 🚀 Setup Method 1: Remote Debugging (Easiest)

### Step 1: Start 3forge with Debug Flags

Create `debug-3forge.ps1`:
```powershell
# Stop existing 3forge
Get-Process | Where-Object {$_.ProcessName -like "*AMI*"} | Stop-Process -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 2

# Start 3forge with remote debugging enabled
Set-Location "C:\Program Files\ami\amione"

# Enable remote debugging on port 5005
$env:JAVA_OPTS = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

Write-Host "🐞 Starting 3forge with remote debugging on port 5005..." -ForegroundColor Green
Start-Process -FilePath ".\AMI_One.exe" -ArgumentList "-Djava.awt.headless=false"

Write-Host "✅ 3forge started with debugging enabled!" -ForegroundColor Green
Write-Host "🔗 Connect your IDE debugger to localhost:5005" -ForegroundColor Cyan
```

### Step 2: Connect IDE Debugger

**IntelliJ IDEA:**
1. Run → Edit Configurations
2. Add → Remote JVM Debug
3. Host: `localhost`, Port: `5005`
4. Set breakpoints in `TradingDashboardManager.java`
5. Click Debug

**VS Code:**
1. Add to `.vscode/launch.json`:
```json
{
  "type": "java",
  "name": "Debug 3forge Remote",
  "request": "attach",
  "hostName": "localhost",
  "port": 5005
}
```

**Eclipse:**
1. Run → Debug Configurations → Remote Java Application
2. Host: `localhost`, Port: `5005`

### Step 3: Add Debug Breakpoints

Add this method to `TradingDashboardManager.java`:
```java
@AmiScriptAccessible(name = "debugBreakpoint", params = { "trader", "layout" })
public String debugBreakpoint(Object trader, Object layout) {
    // SET BREAKPOINT HERE ⬇️
    String traderName = trader.toString();
    
    // When breakpoint hits, inspect these objects:
    Class<?> layoutClass = layout.getClass();
    
    try {
        java.lang.reflect.Method getPanelMethod = layoutClass.getMethod("getPanel", String.class);
        Object panel = getPanelMethod.invoke(layout, "Html1");
        
        // SET ANOTHER BREAKPOINT HERE ⬇️
        // Inspect 'panel' object in debugger to see available methods
        Class<?> panelClass = panel.getClass();
        
        return "Debug breakpoint hit for trader: " + traderName;
    } catch (Exception e) {
        // SET BREAKPOINT HERE TOO ⬇️
        return "Error: " + e.getMessage();
    }
}
```

### Step 4: Update AMI Script for Debug

Replace the complex test with simple debug call:
```javascript
if (column == "Trader") {
    session.log("Calling debug breakpoint for: " + val);
    
    // This will hit our breakpoint
    String result = TradingDashboardManager.debugBreakpoint(val, layout);
    session.log("Debug result: " + result);
    
    // Keep the working chart update
    Map filterParams = new Map();
    filterParams.put("WHERE", "trader = '" + val + "'");
    layout.getDatamodel("trades3").process(filterParams);
}
```

## 🕵️ What to Inspect When Breakpoint Hits

**1. Layout Object:**
- `layout.getClass().getName()` - What type of object is this?
- `layout.getClass().getMethods()` - What methods are available?

**2. Panel Object:**
- `panel.getClass().getName()` - What type of HTML panel is this?
- `panel.getClass().getMethods()` - What methods can we call?
- Look for methods like: `setHtmlTemplate2`, `setValue`, `setContent`, etc.

**3. Method Parameters:**
- What parameter types do the panel methods expect?
- Are there overloaded versions we missed?

## 🎯 Alternative: Console Debugging

If remote debugging doesn't work, add heavy logging:

```java
@AmiScriptAccessible(name = "verboseDebug", params = { "layout" })
public String verboseDebug(Object layout) {
    StringBuilder debug = new StringBuilder();
    
    try {
        debug.append("=== LAYOUT DEBUG ===\n");
        debug.append("Layout class: ").append(layout.getClass().getName()).append("\n");
        
        java.lang.reflect.Method getPanelMethod = layout.getClass().getMethod("getPanel", String.class);
        Object panel = getPanelMethod.invoke(layout, "Html1");
        
        if (panel != null) {
            debug.append("=== PANEL DEBUG ===\n");
            debug.append("Panel class: ").append(panel.getClass().getName()).append("\n");
            debug.append("Panel methods:\n");
            
            for (java.lang.reflect.Method method : panel.getClass().getMethods()) {
                if (method.getName().startsWith("set") || 
                    method.getName().contains("Html") || 
                    method.getName().contains("Template") ||
                    method.getName().contains("Value")) {
                    debug.append("  ").append(method.getName());
                    debug.append("(").append(java.util.Arrays.toString(method.getParameterTypes())).append(")\n");
                }
            }
        }
        
    } catch (Exception e) {
        debug.append("ERROR: ").append(e.getMessage()).append("\n");
    }
    
    return debug.toString();
}
```

## 🚀 Expected Breakthrough

With debugging, we'll immediately see:
1. **Where exactly** the AMI script breaks
2. **What methods** are actually available on HTML panels
3. **How to call** the panel update method correctly
4. **Why our reflection** attempts are failing

This should solve the HTML panel update challenge in one debugging session! 