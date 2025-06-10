# Getting Started with 3forge Trading Dashboard

## Overview
This guide will help you set up and run the 3forge trading dashboard on your local machine. The dashboard features complete 4-panel inter-widget communication where clicking a trader updates all visualizations simultaneously.

## Prerequisites

### Required Software
- **3forge AMI Platform**: Commercial installation required
- **Java 8+**: For custom class compilation
- **Gradle**: Build automation (included in project)
- **Python 3.7+**: For demo data generation
- **PowerShell**: For automation scripts (Windows)

### System Requirements
- **Operating System**: Windows 10+ (for 3forge AMI)
- **Memory**: 4GB RAM minimum, 8GB recommended
- **Storage**: 2GB free space for 3forge installation
- **Network**: Internet connection for initial setup

## Quick Start (5 Minutes)

### Step 1: Verify 3forge Installation
Ensure 3forge AMI is installed and accessible:
```bash
# Check if 3forge is installed
ls "C:\Program Files\ami\amione\AMI_One.exe"

# If not installed, install 3forge AMI first
# Visit: https://3forge.com/download
```

### Step 2: Clone and Build
```bash
# Navigate to your development directory
cd C:\Users\{username}\development

# Clone this repository
git clone https://github.com/your-org/3forge-trading-dashboard.git
cd 3forge-trading-dashboard

# Build and deploy the custom classes
gradle buildProd
```

### Step 3: Set Up Database
```bash
# Generate demo trading data
python data/py/ami.py

# Verify database creation
ls trading_data.db
```

### Step 4: Configure Data Sources
1. Open 3forge AMI application
2. Navigate to **Data Sources** → **Add New**
3. Configure SQLite connection:
   - **Type**: SQLite
   - **Database Path**: `{project-root}/trading_data.db`
   - **Connection Name**: `trading_data`

### Step 5: Load Dashboard
1. In 3forge AMI, go to **File** → **Open Dashboard**
2. Select `dashboards/3forge.json`
3. Click any trader name to see the 4-panel communication in action

## Detailed Setup Guide

### Database Configuration
The dashboard uses SQLite with realistic trading data:

**Tables Created:**
- **trades**: Individual trade records with trader-symbol relationships
- **positions**: Portfolio positions by symbol
- **marketdata**: Current market prices and volumes
- **riskmetrics**: Risk measurements by trading desk
- **orders**: Order status and fill information

**Data Sources Setup in 3forge:**
1. **Data Source Manager** → **Add SQLite Source**
2. **Connection String**: `jdbc:sqlite:{full-path-to}/trading_data.db`
3. **Test Connection** to verify setup
4. **Save and Apply**

### Dashboard Features

#### 4-Panel Inter-Widget Communication
When you click any trader name in the data table:
1. **HTML Panel**: Shows trader performance metrics with professional styling
2. **Heatmap**: Filters to show only symbols that trader has traded
3. **P&L Chart**: Displays trader-specific performance data
4. **Data Table**: Highlights the selected trader's trades

#### Reset Functionality
Click any non-trader column (Price, Volume, etc.) to return all panels to full data view.

#### Trader-Symbol Mapping
Each trader has specific symbol preferences for clear demo visualization:
- **Tom Brown**: AMZN, ORCL, INTC
- **Mike Chen**: GOOGL, NVDA, AMD
- **Sarah Jones**: AAPL, TSLA, META
- **Lisa Wang**: TSLA, META, NVDA
- **John Smith**: AAPL, MSFT, GOOGL
- **Emma Davis**: AAPL, AMZN, MSFT

### Custom Java Classes
The dashboard includes custom Java classes for enhanced functionality:

**TradingDashboardManager**: Main class for HTML generation and metrics calculation
- Calculates trader performance metrics (P&L, win rate, trade count)
- Generates professional CSS-styled HTML cards
- Integrates with SQLite database for real-time data

## Development Workflow

### Making Changes

#### For Java Code Changes:
```bash
# Rebuild and restart 3forge
gradle buildProd
.\scripts\rebuild-prod.ps1
```

#### For Dashboard JSON Changes:
```bash
# Just restart 3forge manually (no rebuild needed)
# Save changes in 3forge and reload dashboard
```

#### For Data Changes:
```bash
# Regenerate demo data
python data/py/ami.py

# Restart 3forge to pick up new data
```

### Build Commands
- **`gradle buildProd`**: Production deployment with optimized settings
- **`gradle buildDev`**: Development deployment with debug capabilities
- **`gradle backupWorkingState`**: Create backup of current working configuration
- **`gradle restoreWorkingState`**: Restore from backup if something breaks

## Troubleshooting

### Common Issues

#### "Class not found: TradingDashboardManager"
**Solution:**
1. Verify JAR deployment: Check `C:\Program Files\ami\amione\lib\` for `trading-dashboard.jar`
2. Restart 3forge completely: `.\scripts\rebuild-prod.ps1`
3. Check gradle build output for errors

#### Database Connection Issues
**Solution:**
1. Verify `trading_data.db` exists in project root
2. Check data source configuration in 3forge
3. Ensure full path is used in connection string
4. Test connection in 3forge Data Source Manager

#### Dashboard Not Loading
**Solution:**
1. Verify `dashboards/3forge.json` exists
2. Check 3forge console for error messages
3. Ensure data sources are properly configured
4. Try opening dashboard with **File** → **Open Dashboard**

#### Panels Not Updating
**Solution:**
1. Check AMI script console for error messages
2. Verify custom Java classes are deployed
3. Ensure data sources contain data (`python data/py/ami.py`)
4. Test with simple trader click

### Getting Help

#### Debug Information
Enable detailed logging in 3forge:
1. **Tools** → **Console** → **Enable Debug Logging**
2. Check console output when clicking traders
3. Look for filter operation messages

#### Log Files
Check 3forge log files:
- **Location**: `C:\Program Files\ami\amione\logs\`
- **Key Files**: `amione.log`, `error.log`

#### Backup and Recovery
If something breaks:
```bash
# Restore to last working state
gradle restoreWorkingState

# Or restore from specific backup
gradle smartRestore
```

## Next Steps

### Customization Options
1. **Add New Traders**: Update `data/py/ami.py` with new trader preferences
2. **Add New Symbols**: Extend symbol list and regenerate data
3. **Customize HTML Styling**: Modify `TradingDashboardManager.generateTraderHTML()`
4. **Add New Metrics**: Extend `calculateTraderMetrics()` method

### Advanced Features
1. **Real-time Data**: Replace SQLite with live data feeds
2. **Additional Panels**: Add more visualization components
3. **Custom Calculations**: Implement complex trading metrics
4. **User Authentication**: Add trader-specific access controls

### Production Deployment
1. **Performance Optimization**: Use production database (PostgreSQL, MySQL)
2. **Security Configuration**: Implement proper authentication
3. **Monitoring**: Add logging and alerting
4. **Backup Strategy**: Implement automated backups

## Support and Resources

### Documentation
- **Technical Guide**: `docs/TECHNICAL_GUIDE_COMPLETE.md`
- **Configuration Guide**: `config/CONFIG_GUIDE.md`
- **Data Setup Guide**: `docs/DATA_SOURCE_SETUP.md`

### 3forge Resources
- **3forge Documentation**: https://docs.3forge.com
- **AMI Script Reference**: https://docs.3forge.com/amiscript
- **Custom Classes Guide**: https://docs.3forge.com/custom-classes

### Project Repository
- **Issues**: Report bugs and request features
- **Wiki**: Additional documentation and examples
- **Releases**: Version history and upgrade notes

---

**Congratulations!** You now have a fully functional 4-panel trading dashboard with inter-widget communication. Click any trader name to see the magic happen across all panels simultaneously. 