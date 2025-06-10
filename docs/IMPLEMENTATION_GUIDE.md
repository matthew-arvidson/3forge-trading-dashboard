# 3forge Trading Dashboard - Java Classes Implementation Guide

## 🎯 Overview

This guide shows how to implement custom Java classes to overcome AMI script limitations and enable HTML panel dynamic updates in your 3forge trading dashboard.

## 📁 Files Created

### Java Classes
- `src/main/java/com/forge/trading/TradingDashboardManager.java` - Main business logic
- `src/main/java/com/forge/trading/PanelManager.java` - Panel manipulation utilities

### Configuration
- `ami.properties` - 3forge configuration to register classes
- `build.gradle` - Build configuration
- `updated-ami-script-example.js` - AMI script usage examples

## 🚀 Implementation Steps

### Step 1: Set Up Java Environment

1. **Locate 3forge JAR files** in your installation:
   ```
   [3forge-install]/lib/ami-common.jar
   [3forge-install]/lib/ami-web.jar
   ```

2. **Update build.gradle** with correct paths:
   ```gradle
   dependencies {
       compileOnly files('C:/path/to/3forge/lib/ami-common.jar')
       compileOnly files('C:/path/to/3forge/lib/ami-web.jar')
   }
   ```

3. **Compile the classes**:
   ```bash
   gradle buildFor3forge
   ```

### Step 2: Configure 3forge

1. **Add to your AMI configuration file** (ami.properties or ami.conf):
   ```properties
   ami.web.amiscript.custom.classes=com.forge.trading.TradingDashboardManager,com.forge.trading.PanelManager
   ami.center.amiscript.custom.classes=com.forge.trading.TradingDashboardManager,com.forge.trading.PanelManager
   ```

2. **Add JAR to classpath** (one of these methods):
   - Copy `3forge-trading-dashboard-1.0.0.jar` to `[3forge-install]/lib/`
   - Add to classpath in startup script
   - Set `ami.custom.classes.classpath` property

3. **Restart 3forge AMI** to load the classes

### Step 3: Update AMI Script

Replace your existing `onCellClicked` callback in `3forge.json`:

```javascript
// Current working version (keep this logic)
if (column == "Trader") {
    String selectedTrader = value;
    
    // EXISTING: Update chart (continues to work)
    Map filterParams = new Map();
    filterParams.put("WHERE", "trader = '" + selectedTrader + "'");
    layout.getDatamodel("trades3").process(filterParams);
    
    // NEW: Update HTML panel
    TradingDashboardManager.updateTraderMetrics(selectedTrader, layout);
    
} else {
    // Reset view
    Map resetParams = new Map();
    resetParams.put("WHERE", "1=1");
    layout.getDatamodel("trades3").process(resetParams);
    
    // NEW: Reset HTML panel  
    TradingDashboardManager.resetDashboard(layout);
}
```

## 🔧 Key Features Implemented

### TradingDashboardManager
- `updateTraderMetrics(trader, layout)` - Updates HTML panel with trader-specific data
- `resetDashboard(layout)` - Resets panel to show all traders
- `calculateTraderStats(trader, layout)` - Calculates comprehensive trader metrics

### PanelManager  
- `updateHtmlPanel(panelId, html, layout)` - Updates panel content
- `setHtmlTemplate(panelId, html, layout)` - Alternative update method
- `getPanelInfo(panelId, layout)` - Debug panel capabilities
- `refreshPanel(panelId, layout)` - Refresh specific panel

## 🎨 Generated HTML Features

The classes generate beautiful trader metrics panels with:
- **Gradient background styling**
- **Grid layout** for metrics display
- **Color-coded values** (green/red for P&L)
- **Professional typography**
- **Responsive design**

### Metrics Displayed
- Total P&L
- Total Volume  
- Win Rate
- Trade Count
- Sharpe Ratio
- Max Drawdown

## 🐛 Debugging

### 1. Check Class Registration
```javascript
// Add to AMI script for testing
session.log("Testing class access...");
boolean success = TradingDashboardManager.resetDashboard(layout);
session.log("Class accessible: " + success);
```

### 2. Panel Inspection
```javascript
// Debug panel capabilities
String info = PanelManager.getPanelInfo("htmlTemplate2", layout);
session.log("Panel info: " + info);
```

### 3. Common Issues

**Classes not found:**
- Check ami.properties configuration
- Verify JAR in classpath
- Restart 3forge completely

**Panel not updating:**
- Verify panel ID matches JSON layout
- Check 3forge logs for reflection errors
- Try alternative panel methods

**Compilation errors:**
- Ensure 3forge JARs are in build path
- Check Java version compatibility
- Verify package structure

## 📊 Expected Results

### Before (AMI Script Only)
- ✅ Table-to-chart filtering works
- ❌ HTML panel shows static content
- ❌ No trader-specific metrics

### After (With Java Classes)  
- ✅ Table-to-chart filtering continues working
- ✅ HTML panel updates dynamically
- ✅ Rich trader metrics displayed
- ✅ Professional styling and layout

## 🚀 Next Steps

1. **Test basic functionality** - Verify classes load and execute
2. **Enhance calculations** - Connect to real database queries
3. **Add more metrics** - Extend with additional trading statistics  
4. **Improve styling** - Customize HTML/CSS for your brand
5. **Add error handling** - Implement robust error management
6. **Performance optimization** - Cache calculations where appropriate

## 📝 Production Considerations

- **Database integration** - Replace sample data with real queries
- **Performance** - Cache expensive calculations
- **Security** - Validate all inputs
- **Logging** - Add comprehensive logging
- **Testing** - Unit test all calculation methods
- **Documentation** - Document business logic formulas

## 🎯 Success Criteria

You'll know the implementation is successful when:
1. 3forge starts without errors
2. AMI script can call Java methods
3. HTML panel updates when clicking traders
4. Styled metrics appear in the panel
5. Reset functionality works correctly

---

**This implementation bridges the gap between AMI script limitations and enterprise-grade functionality, enabling your trading dashboard to demonstrate full 3forge capabilities.** 