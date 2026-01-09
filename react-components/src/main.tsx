import React from 'react';
import { createRoot, Root } from 'react-dom/client';
import HelloWorld, { styles as helloWorldStyles } from './HelloWorld';
import AgGridComponent, { styles as agGridStyles } from './AgGridComponent';
import StockChart, { styles as stockChartStyles } from './StockChart';
import TraderDetailsCard, { styles as traderDetailsCardStyles } from './TraderDetailsCard';

/**
 * 3forge React Integration - Main Entry Point
 * 
 * 
 * Components are registered here and mounted via Shadow DOM for style isolation.
 */

// Component registry - add new components here
const COMPONENTS: Record<string, React.ComponentType<any>> = {
  HelloWorld: HelloWorld,
  AgGrid: AgGridComponent,
  StockChart: StockChart,
  TraderDetailsCard: TraderDetailsCard
};

// Styles registry - must match component names above
const COMPONENT_STYLES: Record<string, string> = {
  HelloWorld: helloWorldStyles,
  AgGrid: agGridStyles,
  StockChart: stockChartStyles,
  TraderDetailsCard: traderDetailsCardStyles
};

// Track mounted instances for cleanup
const mountedInstances = new Map<string, { root: Root; shadowRoot: ShadowRoot }>();

/**
 * Mount a React component into a 3forge div element
 * 
 * @param elementId - The ID of the div element in 3forge dashboard
 * @param componentName - Name of component from registry (e.g., "HelloWorld")
 * @param props - Props to pass to the component
 * @param retries - Number of retries if element not found (internal use)
 */
function mount(elementId: string, componentName: string, props: any = {}, retries: number = 10): void {
  console.log(`🚀 ForgeReact.mount called:`, { elementId, componentName, props });
  
  // Get the target element
  const element = document.getElementById(elementId);
  if (!element) {
    if (retries > 0) {
      // Element not ready yet, retry after delay
      console.log(`⏳ Element "${elementId}" not ready, retrying... (${retries} attempts left)`);
      setTimeout(() => mount(elementId, componentName, props, retries - 1), 150);
      return;
    }
    // Exhausted all retries
    console.error(`❌ Element with id "${elementId}" not found after 10 attempts (1.5 seconds)`);
    return;
  }
  
  // Log success if we had to retry
  if (retries < 10) {
    console.log(`✅ Element "${elementId}" found after ${10 - retries} retries`);
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
      
      // Get styles from registry
      const componentStyles = COMPONENT_STYLES[componentName];
      if (componentStyles) {
        styleElement.textContent = componentStyles;
        console.log(`📦 Injected ${componentName} CSS into Shadow DOM`);
      } else {
        console.log(`ℹ️ No styles registered for ${componentName}`);
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

// Directly set window.ForgeReact (IIFE will execute this)
// TypeScript types are defined in vite-env.d.ts
window.ForgeReact = {
  mount,
  unmount,
  updateProps,
  components: Object.keys(COMPONENTS)
};

console.log('✅ ForgeReact loaded successfully!');
console.log('📦 Available components:', Object.keys(COMPONENTS));
console.log('📋 window.ForgeReact:', window.ForgeReact);

