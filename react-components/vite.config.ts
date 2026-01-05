import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  
  build: {
    // Build as simple IIFE bundle (not a library)
    rollupOptions: {
      input: {
        'forge-react': 'src/main.tsx'
      },
      // Don't externalize anything - bundle React + ReactDOM
      external: [],
      output: {
        format: 'iife',
        entryFileNames: '[name].js',
        inlineDynamicImports: true,
        assetFileNames: 'forge-react.[ext]'
      }
    },
    
    // Output to 3forge resources directory
    outDir: '../resources',
    
    // Clean output
    emptyOutDir: false, // Don't delete other files in resources/
    
    // Force CSS inlining
    cssCodeSplit: false,
    
    // Skip minification for now (can add terser later if needed)
    minify: false
  },
  
  // Inline CSS into JS automatically
  css: {
    modules: {
      localsConvention: 'camelCase'
    }
  }
});

