import React from 'react';
import './HelloWorld.css';

interface HelloWorldProps {
  message?: string;
  userName?: string;
  timestamp?: string;
  forgeItems?: string[]; // Array of items from 3forge
  elementId?: string; // The div's ID for amiJsCallback
}

/**
 * Simple Hello World component for testing 3forge React integration
 * 
 * Props can be passed from AmiScript:
 * __ReactGui.mountComponent("myDiv", "HelloWorld", {
 *   message: "Welcome to 3forge!",
 *   userName: "John Doe"
 * })
 */
export default function HelloWorld({ 
  message = "Hello from React!", 
  userName = "3forge User",
  timestamp,
  forgeItems = [],
  elementId
}: HelloWorldProps) {
  const [count, setCount] = React.useState(0);
  const [selectedItem, setSelectedItem] = React.useState<string>('');
  
  // Handle dropdown change and notify 3forge via amiJsCallback
  const handleItemSelect = (item: string) => {
    console.log('🔵 React: Item selected:', item);
    setSelectedItem(item);
    
    // Send to 3forge via amiJsCallback (standard 3forge approach)
    if (elementId && item) {
      const element = document.getElementById(elementId);
      console.log('🔍 Looking for element:', elementId, 'Found:', element);
      
      if (element && typeof (window as any).amiJsCallback === 'function') {
        console.log('📤 Calling amiJsCallback - element:', elementId, 'action:', 'itemSelected', 'value:', item);
        (window as any).amiJsCallback(element, 'itemSelected', item);
        console.log('✅ amiJsCallback completed');
      } else {
        console.error('❌ Cannot call amiJsCallback - element:', element, 'function exists:', typeof (window as any).amiJsCallback);
      }
    }
  };
  
  // Handle button click and notify 3forge via amiJsCallback
  const handleButtonClick = () => {
    const newCount = count + 1;
    console.log('🔵 React: Button clicked, new count:', newCount);
    setCount(newCount);
    
    // Send to 3forge via amiJsCallback (standard 3forge approach)
    if (elementId) {
      const element = document.getElementById(elementId);
      console.log('🔍 Looking for element:', elementId, 'Found:', element);
      
      if (element && typeof (window as any).amiJsCallback === 'function') {
        console.log('📤 Calling amiJsCallback - element:', elementId, 'action:', 'buttonClicked', 'count:', newCount);
        (window as any).amiJsCallback(element, 'buttonClicked', newCount);
        console.log('✅ amiJsCallback completed');
      } else {
        console.error('❌ Cannot call amiJsCallback - element:', element, 'function exists:', typeof (window as any).amiJsCallback);
      }
    }
  };
  
  return (
    <div className="hello-world">
      <h1>{message}</h1>
      <p className="user-name">User: {userName}</p>
      
      <div className="counter-section">
        <button onClick={handleButtonClick} className="counter-btn">
          Click Count: {count}
        </button>
        <p className="counter-text">
          This proves React state management works!
        </p>
      </div>
      
      {timestamp && (
        <div className="timestamp">
          Rendered at: {timestamp}
        </div>
      )}
      
      {forgeItems.length > 0 && (
        <div className="forge-items-section">
          <label className="dropdown-label">Forge Items:</label>
          <select 
            value={selectedItem} 
            onChange={(e) => handleItemSelect(e.target.value)}
            className="forge-dropdown"
          >
            <option value="">-- Select an item --</option>
            {forgeItems.map((item, index) => (
              <option key={index} value={item}>
                {item}
              </option>
            ))}
          </select>
          {selectedItem && (
            <div className="selected-item">
              ✓ Selected: <strong>{selectedItem}</strong>
              <span className="callback-indicator"> → Sent to 3forge!</span>
            </div>
          )}
        </div>
      )}
      
      <div className="success-badge">
        ✅ React Component Successfully Mounted in 3forge!
      </div>
    </div>
  );
}

