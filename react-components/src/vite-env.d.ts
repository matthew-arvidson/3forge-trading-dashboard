/// <reference types="vite/client" />

// Declare CSS module types for Vite
declare module '*.css' {
  const content: string;
  export default content;
}

declare module '*.css?inline' {
  const content: string;
  export default content;
}

// Global 3forge integration types
declare global {
  interface Window {
    amiJsCallback?: (element: HTMLElement | string, action: string, data: any) => void;
    ForgeReact?: {
      mount: (elementId: string, componentName: string, props?: any) => void;
      unmount: (elementId: string) => void;
      updateProps: (elementId: string, componentName: string, props: any) => void;
      components: string[];
    };
  }
}

export {};

