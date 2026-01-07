import { useEffect, useMemo } from 'react';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';

interface AgGridComponentProps {
  elementId?: string;
  columns?: any[];
  rows?: any[];
  theme?: string;
}

/**
 * AG-Grid Component for 3forge
 * 
 * Props from 3forge:
 * - columns: Array of column definitions {field, headerName, width, ...}
 * - rows: Array of row data objects
 * - theme: 'alpine' | 'balham' | 'material' (default: alpine)
 * 
 * Events to 3forge (via amiJsCallback):
 * - rowClicked: When user clicks a row
 * - cellClicked: When user clicks a cell
 * - selectionChanged: When row selection changes
 */
export default function AgGridComponent({ 
  elementId,
  columns = [],
  rows = [],
  theme = 'alpine'
}: AgGridComponentProps) {
  
  // Default columns if none provided
  const defaultColumns = [
    { field: 'symbol', headerName: 'Symbol', width: 120 },
    { field: 'price', headerName: 'Price', width: 100 },
    { field: 'quantity', headerName: 'Qty', width: 100 }
  ];
  
  // Default rows if none provided
  const defaultRows = [
    { symbol: 'AAPL', price: 150.25, quantity: 100 },
    { symbol: 'GOOGL', price: 2800.50, quantity: 50 },
    { symbol: 'MSFT', price: 310.75, quantity: 200 }
  ];
  
  /**
   * Safe JSON parser for 3forge data models
   * Handles both JSON strings and already-parsed objects/arrays
   */
  const safeJsonParse = (data: any, fallback: any[] = []): any[] => {
    if (!data) {
      console.log('🔍 safeJsonParse: data is null/undefined/empty');
      return fallback;
    }
    
    // If already an array, return it
    if (Array.isArray(data)) {
      console.log('🔍 safeJsonParse: data is already array, length:', data.length);
      return data.length > 0 ? data : fallback;
    }
    
    // If string, try to parse as JSON
    if (typeof data === 'string') {
      console.log('🔍 safeJsonParse: data is string, length:', data.length, 'preview:', data.substring(0, 100));
      try {
        const parsed = JSON.parse(data);
        console.log('✅ safeJsonParse: parsed successfully, type:', Array.isArray(parsed) ? 'array' : typeof parsed, 'length:', Array.isArray(parsed) ? parsed.length : 'N/A');
        return Array.isArray(parsed) && parsed.length > 0 ? parsed : fallback;
      } catch (e) {
        console.error('❌ safeJsonParse: Failed to parse JSON:', e, 'data:', data);
        return fallback;
      }
    }
    
    console.log('🔍 safeJsonParse: data is unexpected type:', typeof data);
    return fallback;
  };
  
  // Memoize parsed data to ensure React detects changes properly
  const columnDefs = useMemo(() => {
    const parsed = safeJsonParse(columns, defaultColumns);
    console.log('🔄 AG-Grid: Columns updated:', parsed.length, 'columns');
    return parsed;
  }, [columns]);
  
  const rowData = useMemo(() => {
    const parsed = safeJsonParse(rows, defaultRows);
    console.log('🔄 AG-Grid: Rows updated:', parsed.length, 'rows');
    return parsed;
  }, [rows]);
  
  // Log when component receives new props (helps debug double-render issue)
  useEffect(() => {
    console.log('📊 AG-Grid props received - RAW VALUES:', {
      elementId,
      columns: columns,
      rows: rows
    });
    
    const parsedColumns = safeJsonParse(columns, []);
    const parsedRows = safeJsonParse(rows, []);
    
    console.log('📊 AG-Grid props parsed:', {
      elementId,
      columnsRaw: Array.isArray(columns) ? `array[${columns.length}]` : typeof columns,
      columnsParsed: parsedColumns.length,
      rowsRaw: Array.isArray(rows) ? `array[${rows.length}]` : typeof rows,
      rowsParsed: parsedRows.length
    });
  }, [elementId, columns, rows]);
  
  // Handle row click - send back to 3forge
  const handleRowClicked = (event: any) => {
    const rowData = event.data;
    console.log('🔵 AG-Grid: Row clicked:', rowData);
    
    if (elementId) {
      const element = document.getElementById(elementId);
      if (element && typeof (window as any).amiJsCallback === 'function') {
        console.log('📤 Sending rowClicked event to 3forge:', rowData);
        (window as any).amiJsCallback(element, 'rowClicked', rowData);
      }
    }
  };
  
  // Handle cell click - send back to 3forge
  const handleCellClicked = (event: any) => {
    const cellData = {
      field: event.colDef.field,
      value: event.value,
      rowData: event.data
    };
    console.log('🔵 AG-Grid: Cell clicked:', cellData);
    
    if (elementId) {
      const element = document.getElementById(elementId);
      if (element && typeof (window as any).amiJsCallback === 'function') {
        console.log('📤 Sending cellClicked event to 3forge:', cellData);
        (window as any).amiJsCallback(element, 'cellClicked', cellData);
      }
    }
  };
  
  return (
    <div style={{ width: '100%', height: '100%' }}>
      <div className={`ag-theme-${theme}`} style={{ height: '100%', width: '100%' }}>
        <AgGridReact
          columnDefs={columnDefs}
          rowData={rowData}
          onRowClicked={handleRowClicked}
          onCellClicked={handleCellClicked}
          defaultColDef={{
            sortable: true,
            filter: true,
            resizable: true,
            flex: 1,
            minWidth: 150
          }}
          rowSelection="single"
          animateRows={true}
          suppressMovableColumns={true}
          suppressColumnVirtualisation={true}
          suppressCellFocus={true}
          suppressRowHoverHighlight={false}
        />
      </div>
    </div>
  );
}

