/**
 * ReactGuiService - Browser-side JavaScript for 3forge React integration
 * 
 * Phase 1: Basic alert + callback to verify bidirectional communication
 * Phase 2: Load React and mount Hello World component
 * Phase 3: Mount AG-Grid React component with data binding
 * 
 * This singleton communicates with the Java adapter via the peer object
 */

function ReactGuiService() {
    console.log("ReactGuiService constructor called");
    this.peer = null;
}

/**
 * Required by 3forge - registers the peer for bidirectional communication
 */
ReactGuiService.prototype.registerAmiGuiServicePeer = function(peer) {
    this.peer = peer;
    console.log("✅ ReactGuiService peer registered successfully!");
    console.log("Ready to communicate with 3forge backend");
};

/**
 * Phase 1: Show browser alert and send timestamp back to 3forge
 * 
 * Called from AmiScript: __ReactGui.showAlert("Hello from 3forge!")
 */
ReactGuiService.prototype.showAlert = function(message) {
    console.log("showAlert called with message:", message);
    
    // Show the alert to user
    alert(message);
    
    // Get current timestamp
    const timestamp = new Date().toISOString();
    
    // Send callback to AmiScript
    console.log("Sending onAlertClosed callback with timestamp:", timestamp);
    this.peer.sendToServer('onAlertClosed', timestamp);
};

/**
 * Phase 2: Mount a React component into a div element
 * 
 * Called from AmiScript: __ReactGui.mountComponent("myDiv", "HelloWorld", {message: "Hi!"})
 * 
 * @param {string} elementId - The ID of the div element in 3forge dashboard
 * @param {string} componentName - Name of component (e.g., "HelloWorld")
 * @param {object} props - Props object to pass to React component
 */
ReactGuiService.prototype.mountComponent = function(elementId, componentName, props) {
    console.log("mountComponent called:", { elementId, componentName, props });
    
    // Check if ForgeReact bundle is loaded
    if (typeof window.ForgeReact === 'undefined') {
        console.error("❌ ForgeReact bundle not loaded! Check that forge-react.js loaded correctly.");
        return;
    }
    
    // Inject elementId into props so React can call amiJsCallback
    const propsWithElementId = {
        ...props,
        elementId: elementId // Pass elementId so React can find the div for amiJsCallback
    };
    
    // Delegate to ForgeReact.mount()
    try {
        window.ForgeReact.mount(elementId, componentName, propsWithElementId);
        console.log("✅ mountComponent successful with elementId:", elementId);
    } catch (error) {
        console.error("❌ Error in mountComponent:", error);
    }
};

/**
 * Phase 3: Mount AG-Grid component (coming soon)
 */
ReactGuiService.prototype.mountGrid = function(elementId, config) {
    console.log("mountGrid - Phase 3 - coming soon!");
    // Will be: this.mountComponent(elementId, "AgGrid", config);
};

console.log("✅ ReactGuiService.js loaded successfully");

