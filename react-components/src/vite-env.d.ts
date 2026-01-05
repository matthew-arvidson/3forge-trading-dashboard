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

