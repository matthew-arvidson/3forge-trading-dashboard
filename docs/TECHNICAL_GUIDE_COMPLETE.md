# 3forge Trading Dashboard - Technical Implementation Guide

## Project Overview
Professional trading dashboard built on 3forge AMI platform, featuring complete 4-panel inter-widget communication. Clicking a trader name updates all visualizations simultaneously: HTML metrics panel, P&L chart, market heatmap, and data table.

## Architecture Status: Production Ready

### Core Features Implemented
- **4-Panel Dashboard**: Integrated trades table, P&L chart, market heatmap, and HTML metrics panel
- **Inter-Widget Communication**: Table row selection triggers updates across all dashboard panels
- **Dynamic HTML Panel**: Real-time trader performance cards with custom CSS styling
- **Smart Data Filtering**: Heatmap shows only symbols the selected trader has traded
- **Chart Integration**: P&L charts filter by trader selection with aggregated data
- **Reset Functionality**: Click any non-trader column to return to full data view
- **Custom Java Classes**: TradingDashboardManager for HTML generation and metrics calculation
- **Build System**: Gradle-based automated deployment pipeline
- **Demo Data**: Realistic trader-symbol preferences for clear visual differentiation

### Trader-Symbol Mapping
Each trader has specific symbol preferences for clear demo visualization:
- **Tom Brown**: AMZN, ORCL, INTC
- **Mike Chen**: GOOGL, NVDA, AMD  
- **Sarah Jones**: AAPL, TSLA, META
- **Lisa Wang**: TSLA, META, NVDA
- **John Smith**: AAPL, MSFT, GOOGL
- **Emma Davis**: AAPL, AMZN, MSFT

## Technical Implementation

### AMI Script Integration
The dashboard uses 3forge's AMI scripting engine for inter-widget communication. Key implementation patterns:

#### Datamodel Updates
```javascript
// Filter P&L chart for selected trader
Map filterParams = new Map();
filterParams.put("WHERE", "trader = '" + val + "'");
Datamodel dm_trades3 = layout.getDatamodel("trades3");
dm_trades3.process(filterParams);

// Filter heatmap to trader's symbols only  
String heatmapWhereClause = "symbol IN (SELECT DISTINCT symbol FROM trades WHERE trader = '" + val + "')";
Map heatmapParams = new Map();
heatmapParams.put("WHERE", heatmapWhereClause);
Datamodel dm_marketdata1 = layout.getDatamodel("marketdata1");
dm_marketdata1.process(heatmapParams);
```

#### HTML Panel Updates
```javascript
// Generate dynamic trader performance cards
String traderHtml = manager.generateTraderHtml(val);
FormPanel pn_Html1 = layout.getPanel("Html1");
pn_Html1.setHtml(traderHtml);
```

#### Reset Functionality
```javascript
// Reset all panels when clicking non-trader columns
if (column != "Trader") {
    String defaultHtml = "<div style='padding: 20px; text-align: center; font-family: Arial; color: #666;'>Click a trader to see metrics</div>";
    FormPanel pn_Html1 = layout.getPanel("Html1");
    pn_Html1.setHtml(defaultHtml);
    
    Map resetParams = new Map();
    resetParams.put("WHERE", "true");
    
    Datamodel dm_trades3_reset = layout.getDatamodel("trades3");
    Datamodel dm_marketdata1_reset = layout.getDatamodel("marketdata1");
    dm_trades3_reset.process(resetParams);
    dm_marketdata1_reset.process(resetParams);
}
```

### Custom Java Classes

#### TradingDashboardManager (Production Class)
Primary class responsible for HTML generation and trader metrics calculation:

```java
@AmiScriptAccessible(name = "TradingDashboardManager")
public class TradingDashboardManager {
    
    @AmiScriptAccessible(name = "generateTraderHtml", params = { "trader" })
    public String generateTraderHtml(Object trader) {
        try {
            String traderName = trader.toString();
            TraderMetrics metrics = calculateTraderMetrics(traderName, null);
            return generateTraderHTML(metrics);
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
    
    // Additional methods for metrics calculation and HTML styling
}
```

**Key Capabilities:**
- **generateTraderHtml()**: Creates CSS-styled performance cards
- **calculateTraderMetrics()**: Computes P&L, trade counts, win rates
- **Database Integration**: Connects to SQLite trading data
- **Professional Styling**: Responsive HTML with color-coded metrics

#### PanelManager (Legacy Utility Class)
Utility class created during development exploration, now superseded by direct AMI script access:

```java
@AmiScriptAccessible(name = "PanelManager")
public class PanelManager {
    
    @AmiScriptAccessible(name = "updateHtmlPanel")
    public static boolean updateHtmlPanel(String panelId, String htmlContent, Object layout) {
        // Reflection-based panel manipulation
        // Status: Legacy - direct AMI script access preferred
    }
    
    @AmiScriptAccessible(name = "getPanelInfo")
    public static String getPanelInfo(String panelId, Object layout) {
        // Debug utilities for panel introspection
    }
}
```

### 3forge AMI Development Insights

#### IDE Features
- **Ctrl+Space**: Complete API autocomplete in AMI script editor
- **Member Methods Panel**: Live exploration of available object methods
- **Click Functions**: Auto-generates proper syntax patterns
- **Type Discovery**: Shows exact parameter types for method calls

#### AMI Script Capabilities
**Supported Features:**
- Complex datamodel operations with multi-panel updates
- Custom Java method calls with layout object passing
- Panel manipulation via `layout.getPanel()` and `.setHtml()`
- Dynamic WHERE clause construction for SQL filtering
- Map operations with `new Map()` and `.put()` methods
- Comprehensive logging via `session.log()`
- String operations and conditional logic

**Limitations:**
- No try-catch blocks (exception handling not supported)
- Variable interpolation breaks in log statements
- Emoji characters cause parsing issues
- Java utility methods (Math.abs(), etc.) not available
- Direct table access methods break after CREATE TABLE operations

### Database Schema
The demo uses SQLite with realistic trading data:

- **trades**: Individual trade records with trader-symbol relationships
- **positions**: Portfolio positions by symbol
- **marketdata**: Current market prices and volumes
- **riskmetrics**: Risk measurements by trading desk
- **orders**: Order status and fill information

### Build and Deployment

#### Gradle Tasks
- **`gradle buildProd`**: Production deployment with optimized settings
- **`gradle buildDev`**: Development deployment with debug capabilities
- **`gradle backupWorkingState`**: Create master backup of working configuration
- **`gradle restoreWorkingState`**: Restore from master backup

#### Development Workflow
```bash
# Deploy changes and restart 3forge
gradle buildProd
.\scripts\rebuild-prod.ps1

# For JSON/AMI Script changes only
# Manual 3forge restart (no rebuild needed)

# For data changes
python data/py/ami.py
```

### Deployment Architecture
- **Production Class**: TradingDashboardManager handles all HTML generation
- **Direct Panel Access**: AMI scripts use `layout.getPanel().setHtml()` for updates
- **SQL Filtering**: Dynamic WHERE clauses for trader-specific data
- **CSS Styling**: Embedded professional styling in generated HTML
- **Error Handling**: Graceful degradation with informative error messages

## Performance Considerations
- **Efficient Filtering**: SQL-level filtering minimizes data transfer
- **Cached Metrics**: Trader performance calculations optimized for repeated access
- **Minimal HTML**: Lightweight HTML generation for fast panel updates
- **Reset Optimization**: Bulk panel resets using shared parameters

This implementation provides a robust foundation for enterprise trading dashboard development on the 3forge platform. 