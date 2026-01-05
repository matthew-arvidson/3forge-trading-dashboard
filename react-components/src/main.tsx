import React from 'react';
import { createRoot, Root } from 'react-dom/client';
import HelloWorld from './HelloWorld';
import AgGridComponent from './AgGridComponent';
import { agGridCSS } from './ag-grid-styles';

// Import AG-Grid CSS for Shadow DOM injection
import agGridCoreCss from 'ag-grid-community/styles/ag-grid.css?inline';
import agGridAlpineCss from 'ag-grid-community/styles/ag-theme-alpine.css?inline';

// CSS will be injected inline for Shadow DOM
// In production, you'd use a CSS loader or fetch strategy
const componentStyles = `
.hello-world {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
  padding: 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  max-width: 600px;
}

.hello-world h1 {
  margin: 0 0 16px 0;
  font-size: 32px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.user-name {
  font-size: 18px;
  margin: 0 0 24px 0;
  opacity: 0.9;
}

.counter-section {
  background: rgba(255, 255, 255, 0.1);
  padding: 20px;
  border-radius: 8px;
  margin: 20px 0;
  backdrop-filter: blur(10px);
}

.counter-btn {
  background: white;
  color: #667eea;
  border: none;
  padding: 12px 24px;
  font-size: 16px;
  font-weight: bold;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.counter-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

.counter-btn:active {
  transform: translateY(0);
}

.counter-text {
  margin: 12px 0 0 0;
  font-size: 14px;
  opacity: 0.9;
}

.timestamp {
  margin: 16px 0;
  font-size: 14px;
  font-style: italic;
  opacity: 0.8;
}

.forge-items-section {
  background: rgba(255, 255, 255, 0.15);
  padding: 16px;
  border-radius: 8px;
  margin: 20px 0;
}

.dropdown-label {
  display: block;
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 8px;
  opacity: 0.9;
}

.forge-dropdown {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border-radius: 6px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  background: white;
  color: #667eea;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.forge-dropdown:hover {
  border-color: rgba(255, 255, 255, 0.6);
}

.forge-dropdown:focus {
  outline: none;
  border-color: white;
  box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.2);
}

.selected-item {
  margin-top: 12px;
  padding: 8px 12px;
  background: rgba(40, 167, 69, 0.3);
  border-radius: 6px;
  font-size: 14px;
}

.callback-indicator {
  font-size: 12px;
  opacity: 0.8;
  font-style: italic;
  margin-left: 8px;
}

.success-badge {
  background: rgba(40, 167, 69, 0.9);
  padding: 12px 16px;
  border-radius: 6px;
  font-weight: bold;
  text-align: center;
  margin-top: 20px;
  font-size: 16px;
}
`;

/**
 * 3forge React Integration - Main Entry Point
 * 
 * 
 * Components are registered here and mounted via Shadow DOM for style isolation.
 */

// Component registry - add new components here
const COMPONENTS: Record<string, React.ComponentType<any>> = {
  HelloWorld: HelloWorld,
  AgGrid: AgGridComponent
};

// Track mounted instances for cleanup
const mountedInstances = new Map<string, { root: Root; shadowRoot: ShadowRoot }>();

/**
 * Mount a React component into a 3forge div element
 * 
 * @param elementId - The ID of the div element in 3forge dashboard
 * @param componentName - Name of component from registry (e.g., "HelloWorld")
 * @param props - Props to pass to the component
 */
function mount(elementId: string, componentName: string, props: any = {}): void {
  console.log(`🚀 ForgeReact.mount called:`, { elementId, componentName, props });
  
  // Get the target element
  const element = document.getElementById(elementId);
  if (!element) {
    console.error(`❌ Element with id "${elementId}" not found`);
    return;
  }
  
  // Get component from registry
  const Component = COMPONENTS[componentName];
  if (!Component) {
    console.error(`❌ Component "${componentName}" not found in registry`);
    console.log('Available components:', Object.keys(COMPONENTS));
    return;
  }
  
  // Check if already mounted - if so, unmount first
  if (mountedInstances.has(elementId)) {
    console.log(`♻️ Component already mounted on ${elementId}, unmounting first`);
    unmount(elementId);
  }
  
  try {
    // Get or create Shadow DOM for style isolation
    let shadowRoot = element.shadowRoot;
    
    if (!shadowRoot) {
      // Create shadow root if it doesn't exist
      shadowRoot = element.attachShadow({ mode: 'open' });
      
      // Inject component CSS into shadow root
      const styleElement = document.createElement('style');
      
      // For AG-Grid, inject full AG-Grid CSS + custom overrides
      if (componentName === 'AgGrid') {
        styleElement.textContent = agGridCoreCss + '\n' + agGridAlpineCss + '\n' + agGridCSS + '\n' + componentStyles;
        console.log('📦 Injected AG-Grid CSS into Shadow DOM (full styles)');
      } else {
        styleElement.textContent = componentStyles;
      }
      
      shadowRoot.appendChild(styleElement);
    } else {
      // Shadow root exists, clear it for fresh mount
      console.log(`🔄 Reusing existing shadow root on ${elementId}`);
      // Keep styles, just clear React content (styles should be first child)
      while (shadowRoot.childNodes.length > 1) {
        shadowRoot.removeChild(shadowRoot.lastChild!);
      }
    }
    
    // Create React root in shadow DOM
    const root = createRoot(shadowRoot);
    
    // Render component
    root.render(<Component {...props} />);
    
    // Track instance
    mountedInstances.set(elementId, { root, shadowRoot });
    
    console.log(`✅ Component "${componentName}" mounted successfully on ${elementId}`);
    
  } catch (error) {
    console.error(`❌ Error mounting component:`, error);
  }
}

/**
 * Unmount a React component
 * 
 * @param elementId - The ID of the div element
 */
function unmount(elementId: string): void {
  const instance = mountedInstances.get(elementId);
  
  if (!instance) {
    console.warn(`⚠️ No component mounted on ${elementId}`);
    return;
  }
  
  try {
    instance.root.unmount();
    mountedInstances.delete(elementId);
    console.log(`✅ Component unmounted from ${elementId}`);
  } catch (error) {
    console.error(`❌ Error unmounting component:`, error);
  }
}

/**
 * Update props on an already-mounted component
 * 
 * @param elementId - The ID of the div element
 * @param props - New props to pass
 */
function updateProps(elementId: string, componentName: string, props: any): void {
  // For now, just remount with new props
  // TODO: Could optimize with state management
  mount(elementId, componentName, props);
}

// Export as global for 3forge JavaScript bridge
declare global {
  interface Window {
    ForgeReact: {
      mount: typeof mount;
      unmount: typeof unmount;
      updateProps: typeof updateProps;
      components: string[];
    };
  }
}

// Directly set window.ForgeReact (IIFE will execute this)
window.ForgeReact = {
  mount,
  unmount,
  updateProps,
  components: Object.keys(COMPONENTS)
};

console.log('✅ ForgeReact loaded successfully!');
console.log('📦 Available components:', Object.keys(COMPONENTS));
console.log('📋 window.ForgeReact:', window.ForgeReact);

