# 3forge AMI Datamodel API Reference

## Overview
The Datamodel class is a core component in 3forge AMI for managing data sources and queries. It provides methods to create, update, and manage data tables and their relationships.

## Key Classes

### Datamodel
```java
Datamodel openaiDatamodel = layout.getDatamodel("_127_0_0_1_5000");
```

#### Properties
- `name`: The name of the datamodel
- `tables`: Collection of tables in the datamodel
- `relationships`: Relationships between tables

#### Common Methods
- `getTable(String name)`: Get a table by name
- `createTable(String sql)`: Create a new table using SQL
- `dropTable(String name)`: Remove a table
- `executeQuery(String sql)`: Execute a SQL query
- `refresh()`: Refresh the datamodel

### Table
Represents a data table within a datamodel.

#### Properties
- `name`: Table name
- `columns`: Collection of columns
- `rows`: Data rows
- `filters`: Active filters

#### Common Methods
- `getColumn(String name)`: Get a column by name
- `addFilter(String condition)`: Add a filter
- `clearFilters()`: Remove all filters
- `refresh()`: Refresh table data

## Example Usage

### Creating a Dynamic Query Table
```java
// Get the datamodel
Datamodel openaiDatamodel = layout.getDatamodel("_127_0_0_1_5000");

// Create a table with dynamic query
String sql = "CREATE TABLE openaiChat AS USE _method=\"GET\" _validateCerts=\"true\" _urlExtension=\"chat?q=" + userQuery + "\" EXECUTE SELECT * FROM openaiChat;";
openaiDatamodel.executeQuery(sql);
```

### Updating Table Data
```java
// Get the table
Table chatTable = openaiDatamodel.getTable("openaiChat");

// Refresh data
chatTable.refresh();
```

## Best Practices
1. Always refresh tables after making changes
2. Use proper error handling when executing queries
3. Clean up unused tables to prevent memory issues
4. Use meaningful table names for better maintainability

## Common Patterns

### Dynamic Query Pattern
```java
// 1. Get or create datamodel
Datamodel dm = layout.getDatamodel("datasource_name");

// 2. Build dynamic query
String query = buildDynamicQuery(userInput);

// 3. Execute query
dm.executeQuery(query);

// 4. Get results
Table results = dm.getTable("result_table");
```

### Table Refresh Pattern
```java
// 1. Get table
Table table = datamodel.getTable("table_name");

// 2. Apply any filters
table.addFilter("column = value");

// 3. Refresh data
table.refresh();

// 4. Process results
for (Row row : table.getRows()) {
    // Process row data
}
``` 