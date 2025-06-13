# 3forge AMI Tableset API Reference

## Overview
The Tableset class in 3forge AMI is used to manage collections of tables and their relationships. It provides functionality for data manipulation, filtering, and visualization.

## Key Classes

### Tableset
```java
Tableset tableset = layout.getTableset("tableset_name");
```

#### Properties
- `name`: The name of the tableset
- `tables`: Collection of tables in the tableset
- `filters`: Active filters
- `sorting`: Current sort configuration

#### Common Methods
- `getTable(String name)`: Get a table by name
- `addTable(Table table)`: Add a table to the tableset
- `removeTable(String name)`: Remove a table
- `addFilter(String condition)`: Add a filter
- `clearFilters()`: Remove all filters
- `refresh()`: Refresh all tables

### Table Operations
```java
// Get a table
Table table = tableset.getTable("table_name");

// Add a new table
tableset.addTable(newTable);

// Remove a table
tableset.removeTable("old_table");
```

## Example Usage

### Creating and Managing Tablesets
```java
// Get or create tableset
Tableset ts = layout.getTableset("my_tableset");

// Add tables
ts.addTable(table1);
ts.addTable(table2);

// Apply filters
ts.addFilter("column > value");

// Refresh data
ts.refresh();
```

### Dynamic Table Management
```java
// Create new table
Table newTable = createNewTable();

// Add to tableset
tableset.addTable(newTable);

// Apply filters
tableset.addFilter("status = 'active'");

// Refresh
tableset.refresh();
```

## Best Practices
1. Keep tablesets focused on related data
2. Use meaningful names for tablesets
3. Clean up unused tables
4. Refresh after making changes
5. Use proper error handling

## Common Patterns

### Table Management Pattern
```java
// 1. Get tableset
Tableset ts = layout.getTableset("tableset_name");

// 2. Add or update tables
ts.addTable(newTable);

// 3. Apply filters
ts.addFilter("condition");

// 4. Refresh
ts.refresh();
```

### Dynamic Filter Pattern
```java
// 1. Get tableset
Tableset ts = layout.getTableset("tableset_name");

// 2. Clear existing filters
ts.clearFilters();

// 3. Add new filters
ts.addFilter(buildFilterCondition(userInput));

// 4. Refresh
ts.refresh();
```

## Integration with Datamodel
```java
// Get datamodel
Datamodel dm = layout.getDatamodel("datasource_name");

// Get tableset
Tableset ts = layout.getTableset("tableset_name");

// Add tables from datamodel to tableset
for (Table table : dm.getTables()) {
    ts.addTable(table);
}

// Refresh
ts.refresh();
``` 