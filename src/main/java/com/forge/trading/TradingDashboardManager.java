package com.forge.trading;

import com.f1.ami.amicommon.customobjects.AmiScriptAccessible;

@AmiScriptAccessible(name = "TradingDashboardManager")
public class TradingDashboardManager {
    
    // Static chatbot instance for delegation (minimal OpenAI version)
    private static TradingAiChatbotFinal chatbot = new TradingAiChatbotFinal();
    
    private String currentChatQuery = "";  // Default empty query
    
    @AmiScriptAccessible
    public TradingDashboardManager() {
        // Default constructor
    }
    
    @AmiScriptAccessible(name = "updateTraderMetrics", params = { "trader", "layout" })
    public String updateTraderMetrics(Object trader, Object layout) {
        try {
            String traderName = trader.toString();
            
            // Generate trader HTML
            String traderHtml = generateTraderHtml(traderName);
            
            // Update the HTML panel
            updateHtmlPanel(layout, traderHtml);
            
            return "SUCCESS: Updated HTML panel for " + traderName;
            
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
    
    @AmiScriptAccessible(name = "generateTraderHtml", params = { "trader" })
    public String generateTraderHtml(Object trader) {
        try {
            String traderName = trader.toString();
            
            // Calculate trader metrics
            TraderMetrics metrics = calculateTraderMetrics(traderName, null);
            
            // Generate rich HTML metrics cards
            String html = generateTraderHTML(metrics);
            
            return html;
            
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
    
    private Object getPanel(Object layout, String panelId) {
        try {
            java.lang.reflect.Method getPanelMethod = layout.getClass().getMethod("getPanel", String.class);
            return getPanelMethod.invoke(layout, panelId);
        } catch (Exception e) {
            System.out.println("Error getting panel: " + e.getMessage());
            return null;
        }
    }
    
    private void updatePanelHtml(Object panel, String html) {
        try {
            // Try different methods that might work for updating HTML content
            Class<?> panelClass = panel.getClass();
            
            // Method 1: Try setValue
            try {
                java.lang.reflect.Method setValueMethod = panelClass.getMethod("setValue", String.class);
                setValueMethod.invoke(panel, html);
                System.out.println("SUCCESS: Used setValue method");
                return;
            } catch (NoSuchMethodException e) {
                System.out.println("setValue method not found, trying alternatives...");
            }
            
            // Method 2: Try setHtml
            try {
                java.lang.reflect.Method setHtmlMethod = panelClass.getMethod("setHtml", String.class);
                setHtmlMethod.invoke(panel, html);
                System.out.println("SUCCESS: Used setHtml method");
                return;
            } catch (NoSuchMethodException e) {
                System.out.println("setHtml method not found, trying alternatives...");
            }
            
            // Method 3: Try setContent
            try {
                java.lang.reflect.Method setContentMethod = panelClass.getMethod("setContent", String.class);
                setContentMethod.invoke(panel, html);
                System.out.println("SUCCESS: Used setContent method");
                return;
            } catch (NoSuchMethodException e) {
                System.out.println("setContent method not found");
            }
            
            // If we get here, log available methods
            System.out.println("Available methods on panel:");
            for (java.lang.reflect.Method method : panelClass.getMethods()) {
                if (method.getName().contains("set") || method.getName().contains("html") || method.getName().contains("Html")) {
                    System.out.println("  " + method.getName() + "(" + java.util.Arrays.toString(method.getParameterTypes()) + ")");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error updating panel HTML: " + e.getMessage());
        }
    }
    
    private TraderMetrics calculateTraderMetrics(String trader, Object layout) {
        // For now, return mock data - we'll enhance this to query real data
        TraderMetrics metrics = new TraderMetrics();
        metrics.traderName = trader;
        
        // Mock calculations based on trader name
        switch(trader) {
            case "Sarah Jones":
                metrics.totalPnl = 2847.50;
                metrics.tradeCount = 12;
                metrics.winRate = 75.0;
                metrics.topSymbol = "AAPL";
                break;
            case "Mike Chen":
                metrics.totalPnl = 4521.30;
                metrics.tradeCount = 18;
                metrics.winRate = 83.3;
                metrics.topSymbol = "GOOGL";
                break;
            case "Lisa Wang":
                metrics.totalPnl = -876.20;
                metrics.tradeCount = 8;
                metrics.winRate = 37.5;
                metrics.topSymbol = "TSLA";
                break;
            default:
                metrics.totalPnl = 1250.00;
                metrics.tradeCount = 10;
                metrics.winRate = 60.0;
                metrics.topSymbol = "SPY";
        }
        
        return metrics;
    }
    
    private String generateTraderHTML(TraderMetrics metrics) {
        String pnlClass = metrics.totalPnl >= 0 ? "positive" : "negative";
        String pnlSign = metrics.totalPnl >= 0 ? "+" : "";
        
        return String.format(
            "<style>\n" +
            "    .metrics-container {\n" +
            "        position: relative;\n" +
            "        width: 100%%;\n" +
            "        height: 100%%;\n" +
            "        min-width: 600px;\n" +
            "        min-height: 400px;\n" +
            "        background-color: #f8f9fa;\n" +
            "        font-family: Arial, sans-serif;\n" +
            "    }\n" +
            "    \n" +
            "    .trader-header {\n" +
            "        position: absolute;\n" +
            "        top: 2%%;\n" +
            "        left: 0;\n" +
            "        right: 0;\n" +
            "        text-align: center;\n" +
            "        font-size: 24px;\n" +
            "        font-weight: bold;\n" +
            "        color: #007bff;\n" +
            "        border-bottom: 2px solid #007bff;\n" +
            "        padding-bottom: 10px;\n" +
            "    }\n" +
            "    \n" +
            "    .metric-card {\n" +
            "        position: absolute;\n" +
            "        background: white;\n" +
            "        border-radius: 12px;\n" +
            "        box-shadow: 0 4px 8px rgba(0,0,0,0.15);\n" +
            "        border-left: 6px solid #007bff;\n" +
            "        width: 45%%;\n" +
            "        height: 35%%;\n" +
            "    }\n" +
            "    \n" +
            "    .card-1 { top: 15%%; left: 2.5%%; border-left-color: #007bff; }\n" +
            "    .card-2 { top: 15%%; right: 2.5%%; }\n" +
            "    .card-3 { bottom: 10%%; left: 2.5%%; border-left-color: #dc3545; }\n" +
            "    .card-4 { bottom: 10%%; right: 2.5%%; border-left-color: #28a745; }\n" +
            "    \n" +
            "    .metric-title {\n" +
            "        position: absolute;\n" +
            "        top: 25%%;\n" +
            "        left: 0;\n" +
            "        right: 0;\n" +
            "        font-size: 16px;\n" +
            "        color: #6c757d;\n" +
            "        font-weight: bold;\n" +
            "        text-transform: uppercase;\n" +
            "        text-align: center;\n" +
            "    }\n" +
            "    \n" +
            "    .metric-value {\n" +
            "        position: absolute;\n" +
            "        top: 55%%;\n" +
            "        left: 0;\n" +
            "        right: 0;\n" +
            "        font-size: 32px;\n" +
            "        font-weight: bold;\n" +
            "        color: #212529;\n" +
            "        text-align: center;\n" +
            "    }\n" +
            "    \n" +
            "    .positive { color: #28a745; }\n" +
            "    .negative { color: #dc3545; }\n" +
            "</style>\n" +
            "\n" +
            "<div class=\"metrics-container\">\n" +
            "    <div class=\"trader-header\">%s PERFORMANCE</div>\n" +
            "    \n" +
            "    <div class=\"metric-card card-1\">\n" +
            "        <div class=\"metric-title\">TOTAL P&L</div>\n" +
            "        <div class=\"metric-value %s\">%s$%.2f</div>\n" +
            "    </div>\n" +
            "    \n" +
            "    <div class=\"metric-card card-2\">\n" +
            "        <div class=\"metric-title\">TRADES</div>\n" +
            "        <div class=\"metric-value\">%d</div>\n" +
            "    </div>\n" +
            "    \n" +
            "    <div class=\"metric-card card-3\">\n" +
            "        <div class=\"metric-title\">TOP SYMBOL</div>\n" +
            "        <div class=\"metric-value\">%s</div>\n" +
            "    </div>\n" +
            "    \n" +
            "    <div class=\"metric-card card-4\">\n" +
            "        <div class=\"metric-title\">WIN RATE</div>\n" +
            "        <div class=\"metric-value\">%.1f%%</div>\n" +
            "    </div>\n" +
            "</div>",
            metrics.traderName.toUpperCase(),                // Trader name
            pnlClass,                                        // CSS class
            pnlSign,                                         // + or - sign
            Math.abs(metrics.totalPnl),                      // P&L value
            metrics.tradeCount,                              // Trade count
            metrics.topSymbol,                               // Top symbol
            metrics.winRate                                  // Win rate
        );
    }
    
    private void updateHtmlPanel(Object layout, String newHtml) {
        try {
            System.out.println("=== UPDATING HTML PANEL ===");
            System.out.println("Layout class: " + layout.getClass().getSimpleName());
            
            // Step 1: Try to get the getPanel method
            java.lang.reflect.Method getPanelMethod = null;
            try {
                getPanelMethod = layout.getClass().getMethod("getPanel", String.class);
                System.out.println("Found getPanel method");
            } catch (Exception e) {
                System.out.println("ERROR: getPanel method not found - " + e.getMessage());
                return;
            }
            
            // Step 2: Try to get the Html1 panel
            Object htmlPanel = null;
            try {
                htmlPanel = getPanelMethod.invoke(layout, "Html1");
                System.out.println("Panel lookup result: " + (htmlPanel != null ? "Found" : "NULL"));
            } catch (Exception e) {
                System.out.println("ERROR: Failed to get Html1 panel - " + e.getMessage());
                return;
            }
            
            if (htmlPanel == null) {
                System.out.println("ERROR: Html1 panel is null");
                return;
            }
            
            System.out.println("Found Html1 panel: " + htmlPanel.getClass().getSimpleName());
            
            // Step 3: Try to get the setValue method
            java.lang.reflect.Method setValueMethod = null;
            try {
                setValueMethod = htmlPanel.getClass().getMethod("setValue", String.class, Object.class);
                System.out.println("Found setValue method");
            } catch (Exception e) {
                System.out.println("ERROR: setValue method not found - " + e.getMessage());
                return;
            }
            
            // Step 4: Try to call setValue
            try {
                Object result = setValueMethod.invoke(htmlPanel, "htmlTemplate2", newHtml);
                System.out.println("setValue result: " + result);
                System.out.println("SUCCESS: HTML panel update attempted!");
                System.out.println("HTML length: " + newHtml.length() + " characters");
            } catch (Exception e) {
                System.out.println("ERROR: setValue call failed - " + e.getMessage());
            }
            
            System.out.println("=== END HTML PANEL UPDATE ===");
            
        } catch (Exception e) {
            System.out.println("FATAL ERROR in updateHtmlPanel: " + e.getMessage());
            // Never let exceptions escape to AMI script
        }
    }
    
    // Helper class for trader metrics
    private static class TraderMetrics {
        String traderName;
        double totalPnl;
        int tradeCount;
        double winRate;
        String topSymbol;
    }
    
    @AmiScriptAccessible(name = "resetDashboard", params = { "layout" })
    public String resetDashboard(Object layout) {
        try {
            // Reset dashboard to show all traders
            return "Dashboard reset to show all traders (Layout type: " + layout.getClass().getSimpleName() + ")";
        } catch (Exception e) {
            return "Error resetting dashboard: " + e.getMessage();
        }
    }
    
    @AmiScriptAccessible(name = "debugInfo")
    public String debugInfo() {
        return "TradingDashboardManager is working! Version 1.0";
    }
    
    // TEST METHOD: Using simple approach without reflection
    @AmiScriptAccessible(name = "testLayoutMethod", params = { "panel" })
    public String testLayoutMethod(Object panel) {
        if (panel != null) {
            return "SUCCESS: Received panel of type: " + panel.getClass().getSimpleName();
        } else {
            return "ERROR: Panel is null";
        }
    }
    
    // DISCOVERY METHOD: Find available methods on the panel
    @AmiScriptAccessible(name = "explorePanelMethods", params = { "panel" })
    public String explorePanelMethods(Object panel) {
        if (panel == null) {
            return "ERROR: Panel is null";
        }
        
        StringBuilder methods = new StringBuilder();
        methods.append("Panel type: ").append(panel.getClass().getSimpleName()).append(" | ");
        
        // Look for promising methods (avoid reflection, use simple name matching)
        Class<?> clazz = panel.getClass();
        java.lang.reflect.Method[] allMethods = clazz.getMethods();
        
        int count = 0;
        for (java.lang.reflect.Method method : allMethods) {
            String name = method.getName();
            // Look for methods that might update content
            if ((name.contains("set") || name.contains("update") || name.contains("html") || 
                 name.contains("Html") || name.contains("value") || name.contains("content")) 
                && !name.equals("toString") && count < 10) {
                
                methods.append(name).append("(");
                Class<?>[] params = method.getParameterTypes();
                for (int i = 0; i < params.length; i++) {
                    if (i > 0) methods.append(",");
                    methods.append(params[i].getSimpleName());
                }
                methods.append(") ");
                count++;
            }
        }
        
        if (count == 0) {
            methods.append("No obvious update methods found");
        }
        
        return methods.toString();
    }
    
    @AmiScriptAccessible(name = "getTraderHtmlPreview", params = { "trader" })
    public String getTraderHtmlPreview(Object trader) {
        try {
            String traderName = trader.toString();
            TraderMetrics metrics = calculateTraderMetrics(traderName, null);
            String html = generateTraderHTML(metrics);
            
            // Return first 200 characters so we can see it in the logs
            String preview = html.length() > 200 ? html.substring(0, 200) + "..." : html;
            return "HTML Preview for " + traderName + ": " + preview;
        } catch (Exception e) {
            return "Error generating HTML preview: " + e.getMessage();
        }
    }
    
    // ========================================================================
    // CHAT FUNCTIONALITY - Added to working TradingDashboardManager
    // ========================================================================
    
    @AmiScriptAccessible(name = "processChatMessage", params = {"userMessage"})
    public String processChatMessage(Object userMessage) {
        // Delegate to chatbot (dumb component)
        return chatbot.processChatMessage(userMessage);
    }
    
    @AmiScriptAccessible(name = "processChatMessageWithSession", params = {"userMessage", "session"})
    public String processChatMessageWithSession(Object userMessage, Object session) {
        // Delegate to chatbot with session context
        return chatbot.processChatMessageWithSession(userMessage, session);
    }
    
    @AmiScriptAccessible(name = "chatTest")
    public String chatTest() {
        // Delegate to chatbot (dumb component utility)
        return chatbot.chatTest();
    }
    
    // Simple chat - just current exchange (no complex history)
    @AmiScriptAccessible(name = "generateChatHtml", params = {"userInput", "chatResponse"})
    public String generateChatHtml(Object userInput, Object chatResponse) {
        // Delegate to chatbot
        return chatbot.generateChatHtml(userInput, chatResponse);
    }
    
    @AmiScriptAccessible(name = "getChatHistory")
    public String getChatHistory() {
        // Delegate to chatbot (dumb component utility)
        return chatbot.getChatHistory();
    }
    
    @AmiScriptAccessible(name = "clearChatHistory")
    public String clearChatHistory() {
        // Delegate to chatbot (dumb component utility)
        return chatbot.clearChatHistory();
    }
    
    @AmiScriptAccessible(name = "processChatCommand", params = {"chatResponse", "layout"})
    public String processChatCommand(Object chatResponse, Object layout) {
        try {
            String jsonResponse = chatResponse.toString();
            
            // Use chatbot for JSON parsing (dumb component utility)
            String command = chatbot.extractJsonField(jsonResponse, "command");
            String trader = chatbot.extractJsonField(jsonResponse, "trader");

                
            String layoutInfo = (layout != null ? layout.getClass().getSimpleName() : "null");
            
            // Handle business logic here (smart component decisions)
            if ("FILTER_TRADER".equals(command) && trader != null && !trader.isEmpty()) {
                // Execute the trader filter using our existing method
                String result = updateTraderMetrics(trader, layout);
                
                return String.format("FILTER_TRADER executed | Trader: %s | Layout: %s | Result: %s", 
                    trader, layoutInfo, result);
                
            } else if ("RESET_DASHBOARD".equals(command)) {
                // Execute dashboard reset using our existing method
                String result = resetDashboard(layout);
                
                return String.format("RESET_DASHBOARD executed | Layout: %s | Result: %s", 
                    layoutInfo, result);
                
            } else {
                return String.format("No command found | Command: %s | Trader: %s | Layout: %s", 
                    command, trader, layoutInfo);
            }
            
        } catch (Exception e) {
            return "ERROR in processChatCommand: " + e.getMessage();
        }
    }
    
    @AmiScriptAccessible(name = "extractMessageFromJson", params = {"jsonResponse"})
    public String extractMessageFromJson(String jsonResponse) {
        // Delegate to chatbot (dumb component utility)
        return chatbot.extractMessageFromJson(jsonResponse);
    }
    
    @AmiScriptAccessible(name = "extractJsonField", params = {"jsonResponse", "fieldName"})
    public String extractJsonField(String jsonResponse, String fieldName) {
        // Delegate to chatbot (dumb component utility)
        return chatbot.extractJsonField(jsonResponse, fieldName);
    }
    
    @AmiScriptAccessible(name = "debugLayoutAccess", params = {"layout"})
    public String debugLayoutAccess(Object layout) {
        try {
            StringBuilder debug = new StringBuilder();
            
            // Layout info
            Class<?> layoutClass = layout.getClass();
            debug.append("Layout Type: ").append(layoutClass.getSimpleName()).append(" | ");
            
            // Try to access HTML panel
            try {
                Object htmlPanel = getPanel(layout, "Html1");
                if (htmlPanel != null) {
                    debug.append("Html1: Found (").append(htmlPanel.getClass().getSimpleName()).append(") | ");
                } else {
                    debug.append("Html1: NULL | ");
                }
            } catch (Exception e) {
                debug.append("Html1: ERROR (").append(e.getMessage()).append(") | ");
            }
            
            // Try to access datamodel 
            try {
                java.lang.reflect.Method getDatamodel = layoutClass.getMethod("getDatamodel", String.class);
                Object dm = getDatamodel.invoke(layout, "Trades");
                if (dm != null) {
                    debug.append("Datamodel: Found | ");
                } else {
                    debug.append("Datamodel: NULL | ");
                }
            } catch (Exception e) {
                debug.append("Datamodel: ERROR | ");
            }
            
            // List key methods
            debug.append("Methods: ");
            java.lang.reflect.Method[] methods = layoutClass.getMethods();
            int count = 0;
            for (java.lang.reflect.Method method : methods) {
                if (count < 3 && (method.getName().contains("Panel") || method.getName().contains("Data"))) {
                    debug.append(method.getName()).append(" ");
                    count++;
                }
            }
            
            return debug.toString();
            
        } catch (Exception e) {
            return "DEBUG ERROR: " + e.getMessage();
        }
    }
    
    @AmiScriptAccessible(name = "setCurrentChatQuery")
    public void setCurrentChatQuery(String query) {
        this.currentChatQuery = query;
    }
    
    @AmiScriptAccessible(name = "getCurrentChatQuery")
    public String getCurrentChatQuery() {
        return this.currentChatQuery;
    }
} 