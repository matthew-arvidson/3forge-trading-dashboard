# 3forge Data Source Setup Guide

## Overview
Complete guide for setting up data sources in 3forge AMI after fresh installation. This documentation ensures you can quickly restore data connections after any system reinstall.

## SQLite Data Source Setup (Primary)

### Step-by-Step Process

1. **Enter Edit Mode**
   - Open 3forge dashboard: `http://localhost:33332/3forge`
   - Click **Edit Mode** button

2. **Access Data Modeler**
   - Click the **center cog/gear icon** 
   - Select **"Show panel in data modeler"**

3. **Attach Data Source**
   - Click **"Attach data source"**
   - Select **SQLite** as database type

4. **Configure Connection**
   - **Name:** `trading_data`
   - **Source URL:** Path to `trading_data.db` file
   - **Full Path:** `C:\Users\{username}\development\3forge\trading_data.db`

5. **Test Connection**
   - Click **Test** to verify connection
   - Should show successful connection status

6. **Save Configuration**
   - Click **Save** to persist the data source
   - Exit edit mode

## Database Schema Reference

### Tables Available
- **trades** - Individual trade records with trader-symbol relationships
- **positions** - Portfolio positions by symbol
- **marketdata** - Current market prices and volumes
- **riskmetrics** - Risk measurements by trading desk
- **orders** - Order status and fill information

### Key Columns
```sql
-- Trades table
id, timestamp, symbol, price, volume, side, trader, desk, pnl

-- Positions table  
account, symbol, quantity, avgPrice, marketValue, unrealizedPnl, realizedPnl

-- Market data table
symbol, bid, ask, last, volume, change, changePercent, timestamp, exchange

-- Risk metrics table
desk, var95, var99, expectedShortfall, exposure, leverage, timestamp

-- Orders table
orderId, symbol, side, quantity, price, status, timestamp, trader, fillQuantity
```

## Troubleshooting

### Common Issues

#### Database file not found
**Solution:** Verify the file path is correct
```bash
# Check if file exists
dir "C:\Users\{username}\development\3forge\trading_data.db"

# Generate database if missing
python data/py/ami.py
```

#### Connection failed
**Solution:** Check file permissions and SQLite installation
- Ensure SQLite drivers are available in 3forge
- Verify file is not locked by another process
- Use full absolute path in connection string

#### No data visible
**Solution:** Check table permissions and data integrity
```sql
-- Test queries in 3forge
SELECT COUNT(*) FROM trades;
SELECT * FROM marketdata LIMIT 5;
```

## Post-Setup Verification

### Quick Test Queries
1. **Trade count:** `SELECT COUNT(*) FROM trades`
2. **Recent trades:** `SELECT * FROM trades ORDER BY timestamp DESC LIMIT 10`
3. **Active positions:** `SELECT * FROM positions WHERE quantity != 0`
4. **Market data:** `SELECT symbol, last, volume FROM marketdata ORDER BY symbol`

### Dashboard Integration
- Verify tables appear in dashboard data panel
- Test table clicking functionality
- Confirm custom Java classes can access data
- Test trader selection updates all panels

## Fresh Install Checklist

### After System Reinstall
- [ ] Install fresh 3forge AMI
- [ ] Verify basic dashboard loads
- [ ] **Set up SQLite data source** (follow steps above)
- [ ] **Deploy custom Java classes** (see below)
- [ ] Test table clicking functionality  
- [ ] Create new master backup (`gradle backupWorkingState`)

### Custom Java Class Deployment (Required)

After setting up data sources, you must deploy custom classes for inter-widget communication:

```bash
# Deploy custom Java classes for table functionality
gradle buildProd

# Restart 3forge to load new classes
.\scripts\rebuild-prod.ps1
```

**What this does:**
- Builds `trading-dashboard.jar`
- Deploys to `C:\Program Files\ami\amione\lib\`
- Updates `ami-custom.properties` with class registration
- Restarts 3forge with new classes loaded

**Verification Steps:**
- Table clicking should work (updates all 4 panels)
- Custom Java methods accessible in AMI script
- No "class not found" errors in 3forge console
- HTML panel shows trader performance metrics

### Data Source Recovery Checklist
- [ ] Access data modeler in edit mode
- [ ] Configure trading_data source with correct path
- [ ] Test connection successfully
- [ ] Verify all tables visible in data modeler
- [ ] Test dashboard functionality with trader clicks
- [ ] Confirm all 4 panels update correctly

## Backup Considerations

### What Gets Preserved in Backups
- Data source configurations (in AMI `data/` directory)
- Connection settings and table mappings
- Custom class registrations

### What Gets Lost After Fresh Install
- All data source configurations must be manually recreated
- Custom class deployments need to be rerun
- Dashboard JSON files need to be reloaded

### Best Practice
- Always document data source configurations for easy restoration
- Keep `trading_data.db` in version control
- Use master backup system for configuration preservation
- Test full recovery process periodically

## Troubleshooting Data Issues

### Empty Tables
```bash
# Regenerate demo data
python data/py/ami.py

# Verify data creation
sqlite3 trading_data.db "SELECT COUNT(*) FROM trades;"
```

### Performance Issues
- Ensure database file is on local disk, not network drive
- Check for file locking by other applications
- Verify sufficient disk space for database operations

### Connection String Examples
```
# SQLite connection string format
jdbc:sqlite:C:/Users/{username}/development/3forge/trading_data.db

# Alternative format
jdbc:sqlite:{full-path-to-database-file}
```

## Related Documentation
- **Configuration Guide**: `config/CONFIG_GUIDE.md` - Complete build and deployment guide
- **Technical Guide**: `docs/TECHNICAL_GUIDE_COMPLETE.md` - Implementation details
- **Getting Started**: `docs/GETTING_STARTED.md` - Quick start guide
- **Database Script**: `data/py/ami.py` - Demo data generation script 