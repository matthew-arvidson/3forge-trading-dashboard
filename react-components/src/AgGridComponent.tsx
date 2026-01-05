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
  
  const columnDefs = columns.length > 0 ? columns : defaultColumns;
  const rowData = rows.length > 0 ? rows : defaultRows;
  
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

