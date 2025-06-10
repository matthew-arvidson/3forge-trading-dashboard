import sqlite3
import random
from datetime import datetime, timedelta

# Create database connection
conn = sqlite3.connect('trading_data.db')
cursor = conn.cursor()

print("Creating tables...")

# 1. CREATE TABLES FIRST
# ==========================================

# Trading Data Table
cursor.execute('''
    CREATE TABLE IF NOT EXISTS trades (
        id INTEGER PRIMARY KEY,
        timestamp INTEGER,
        symbol TEXT,
        price REAL,
        volume INTEGER,
        side TEXT,
        trader TEXT,
        desk TEXT,
        pnl REAL
    )
''')

# Portfolio Positions Table  
cursor.execute('''
    CREATE TABLE IF NOT EXISTS positions (
        account TEXT,
        symbol TEXT,
        quantity INTEGER,
        avgPrice REAL,
        marketValue REAL,
        unrealizedPnl REAL,
        realizedPnl REAL,
        lastUpdate INTEGER
    )
''')

# Market Data Table
cursor.execute('''
    CREATE TABLE IF NOT EXISTS marketdata (
        symbol TEXT,
        bid REAL,
        ask REAL,
        last REAL,
        volume INTEGER,
        change REAL,
        changePercent REAL,
        timestamp INTEGER,
        exchange TEXT
    )
''')

# Risk Metrics Table
cursor.execute('''
    CREATE TABLE IF NOT EXISTS riskmetrics (
        desk TEXT,
        var95 REAL,
        var99 REAL,
        expectedShortfall REAL,
        exposure REAL,
        leverage REAL,
        timestamp INTEGER
    )
''')

# Orders Table
cursor.execute('''
    CREATE TABLE IF NOT EXISTS orders (
        orderId TEXT,
        symbol TEXT,
        side TEXT,
        quantity INTEGER,
        price REAL,
        status TEXT,
        timestamp INTEGER,
        trader TEXT,
        fillQuantity INTEGER
    )
''')

print("Tables created successfully!")

# 2. POPULATE WITH SAMPLE DATA
# ==========================================

# Sample data arrays
symbols = ['AAPL', 'GOOGL', 'MSFT', 'AMZN', 'TSLA', 'META', 'NVDA', 'AMD', 'INTC', 'ORCL']
traders = ['John Smith', 'Sarah Jones', 'Mike Chen', 'Lisa Wang', 'Tom Brown', 'Emma Davis']
desks = ['Equity Trading', 'Options', 'Fixed Income', 'Commodities', 'FX']
exchanges = ['NYSE', 'NASDAQ', 'BATS', 'ARCA']
sides = ['BUY', 'SELL']
order_statuses = ['NEW', 'PARTIAL', 'FILLED', 'CANCELLED', 'REJECTED']

current_time = int(datetime.now().timestamp() * 1000)

print("Inserting trades data...")

# Create trader-specific symbol preferences (weighted, not exclusive)
trader_preferences = {
    'John Smith': ['AAPL', 'MSFT', 'GOOGL'],
    'Sarah Jones': ['AAPL', 'TSLA', 'META'],  
    'Mike Chen': ['GOOGL', 'NVDA', 'AMD'],
    'Lisa Wang': ['TSLA', 'META', 'NVDA'],
    'Tom Brown': ['AMZN', 'ORCL', 'INTC'],
    'Emma Davis': ['AAPL', 'AMZN', 'MSFT']
}

# Insert trades data (1000 records)
for i in range(1000):
    trader = random.choice(traders)
    
    # Each trader ONLY trades their preferred symbols (for clear demo)
    symbol = random.choice(trader_preferences[trader])
    
    base_price = 100.0 + (i * 0.1)
    price = round(base_price + random.uniform(-10, 10), 2)
    volume = random.randint(100, 1000)
    side = random.choice(sides)
    desk = random.choice(desks)
    pnl = round(random.uniform(-5000, 5000), 2)
    
    cursor.execute('''
        INSERT INTO trades VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    ''', (
        i,
        current_time - random.randint(0, 3600000),  # Random time in last hour
        symbol,
        price,
        volume,
        side,
        trader,
        desk,
        pnl
    ))

print("Inserting positions data...")
# Insert positions data (one per symbol)
for i, symbol in enumerate(symbols):
    quantity = random.randint(-500, 500)
    avg_price = round(100.0 + (i * 2.5), 2)
    market_value = round(quantity * avg_price, 2)
    unrealized_pnl = round(random.uniform(-2500, 2500), 2)
    realized_pnl = round(random.uniform(-1500, 1500), 2)
    
    cursor.execute('''
        INSERT INTO positions VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    ''', (
        "MAIN_PORTFOLIO",
        symbol,
        quantity,
        avg_price,
        market_value,
        unrealized_pnl,
        realized_pnl,
        current_time
    ))

print("Inserting market data...")
# Insert market data with trader-weighted volumes for better filtering
for i, symbol in enumerate(symbols):
    last_price = round(100.0 + (i * 2.5) + random.uniform(-5, 5), 2)
    bid = round(last_price - 0.05, 2)
    ask = round(last_price + 0.05, 2)
    
    # Calculate trader activity for this symbol (sum volume from trades)
    cursor.execute('SELECT SUM(volume) FROM trades WHERE symbol = ?', (symbol,))
    trader_volume_result = cursor.fetchone()
    trader_volume = trader_volume_result[0] if trader_volume_result[0] else 0
    
    # Market volume is base + trader activity (creates trader-specific patterns)
    volume = random.randint(100000, 500000) + (trader_volume * 100)
    
    change = round(random.uniform(-4, 4), 2)
    change_percent = round((change / last_price) * 100, 2)
    exchange = random.choice(exchanges)
    
    cursor.execute('''
        INSERT INTO marketdata VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    ''', (
        symbol,
        bid,
        ask,
        last_price,
        volume,
        change,
        change_percent,
        current_time,
        exchange
    ))

print("Inserting risk metrics...")
# Insert risk metrics (one per desk)
for i, desk in enumerate(desks):
    var95 = round(random.uniform(0, 100000), 2)
    var99 = round(var95 * 1.5, 2)
    expected_shortfall = round(var99 * 1.2, 2)
    exposure = round(random.uniform(0, 1000000), 2)
    leverage = round(1.0 + random.uniform(0, 4), 2)
    
    cursor.execute('''
        INSERT INTO riskmetrics VALUES (?, ?, ?, ?, ?, ?, ?)
    ''', (
        desk,
        var95,
        var99,
        expected_shortfall,
        exposure,
        leverage,
        current_time
    ))

print("Inserting orders data...")
# Insert orders data (200 records)
for i in range(200):
    order_id = f"ORD{1000 + i}"
    symbol = random.choice(symbols)
    side = random.choice(sides)
    quantity = random.randint(100, 500)
    price = round(100.0 + random.uniform(0, 20), 2)
    status = random.choice(order_statuses)
    trader = random.choice(traders)
    fill_quantity = quantity if status == "FILLED" else (int(quantity * 0.5) if status == "PARTIAL" else 0)
    
    cursor.execute('''
        INSERT INTO orders VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    ''', (
        order_id,
        symbol,
        side,
        quantity,
        price,
        status,
        current_time - random.randint(0, 1800000),  # Random time in last 30 min
        trader,
        fill_quantity
    ))

# Commit all changes
conn.commit()
conn.close()

print("✅ Database created successfully!")
print("📊 Sample data populated:")
print("   - 1,000 trades")
print(f"   - {len(symbols)} positions")
print(f"   - {len(symbols)} market data records")
print(f"   - {len(desks)} risk metrics")
print("   - 200 orders")
print("\n🔗 Ready to connect to 3forge!")
print("   Database file: trading_data.db")