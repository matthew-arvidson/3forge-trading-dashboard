package com.forge.trading;

import com.f1.ami.amicommon.customobjects.AmiScriptAccessible;
import java.lang.reflect.Method;

@AmiScriptAccessible(name = "PanelManager")
public class PanelManager {
    
    @AmiScriptAccessible
    public PanelManager() {
        // Default constructor
    }
    
    /**
     * Updates an HTML panel with new content
     * Uses reflection to access 3forge Panel API since we can't directly access panel methods from AMI script
     */
    @AmiScriptAccessible(name = "updateHtmlPanel")
    public static boolean updateHtmlPanel(String panelId, String htmlContent, Object layout) {
        try {
            // Get the panel from layout
            Method getPanel = layout.getClass().getMethod("getPanel", String.class);
            Object panel = getPanel.invoke(layout, panelId);
            
            if (panel == null) {
                System.err.println("Panel not found: " + panelId);
                return false;
            }
            
            // Try to update the panel's HTML content
            // This uses reflection to call panel.setValue() or similar method
            Method setValue = panel.getClass().getMethod("setValue", String.class);
            setValue.invoke(panel, htmlContent);
            
            System.out.println("Successfully updated HTML panel: " + panelId);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error updating HTML panel '" + panelId + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Alternative method to update HTML template directly
     */
    @AmiScriptAccessible(name = "setHtmlTemplate")
    public static boolean setHtmlTemplate(String panelId, String htmlContent, Object layout) {
        try {
            Method getPanel = layout.getClass().getMethod("getPanel", String.class);
            Object panel = getPanel.invoke(layout, panelId);
            
            if (panel == null) {
                System.err.println("Panel not found: " + panelId);
                return false;
            }
            
            // Try different methods to set HTML content
            try {
                Method setHtmlTemplate = panel.getClass().getMethod("setHtmlTemplate", String.class);
                setHtmlTemplate.invoke(panel, htmlContent);
                return true;
            } catch (NoSuchMethodException e1) {
                try {
                    Method setTemplate = panel.getClass().getMethod("setTemplate", String.class);
                    setTemplate.invoke(panel, htmlContent);
                    return true;
                } catch (NoSuchMethodException e2) {
                    Method setValue = panel.getClass().getMethod("setValue", String.class);
                    setValue.invoke(panel, htmlContent);
                    return true;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error setting HTML template for panel '" + panelId + "': " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Refreshes a specific panel
     */
    @AmiScriptAccessible(name = "refreshPanel")
    public static boolean refreshPanel(String panelId, Object layout) {
        try {
            Method getPanel = layout.getClass().getMethod("getPanel", String.class);
            Object panel = getPanel.invoke(layout, panelId);
            
            if (panel == null) {
                System.err.println("Panel not found: " + panelId);
                return false;
            }
            
            // Try to refresh the panel
            Method refresh = panel.getClass().getMethod("refresh");
            refresh.invoke(panel);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error refreshing panel '" + panelId + "': " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets panel information for debugging
     */
    @AmiScriptAccessible(name = "getPanelInfo")
    public static String getPanelInfo(String panelId, Object layout) {
        try {
            Method getPanel = layout.getClass().getMethod("getPanel", String.class);
            Object panel = getPanel.invoke(layout, panelId);
            
            if (panel == null) {
                return "Panel not found: " + panelId;
            }
            
            StringBuilder info = new StringBuilder();
            info.append("Panel ID: ").append(panelId).append("\n");
            info.append("Panel Class: ").append(panel.getClass().getName()).append("\n");
            info.append("Available Methods: ");
            
            Method[] methods = panel.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().contains("set") || method.getName().contains("get") || 
                    method.getName().contains("Template") || method.getName().contains("Value")) {
                    info.append(method.getName()).append("() ");
                }
            }
            
            return info.toString();
            
        } catch (Exception e) {
            return "Error getting panel info: " + e.getMessage();
        }
    }
    
    /**
     * Refreshes all panels in the layout
     */
    @AmiScriptAccessible(name = "refreshAllPanels")
    public static boolean refreshAllPanels(Object layout) {
        try {
            // This would depend on the specific 3forge Layout API
            // For now, we'll try a generic approach
            Method refresh = layout.getClass().getMethod("refresh");
            refresh.invoke(layout);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error refreshing all panels: " + e.getMessage());
            return false;
        }
    }
} 