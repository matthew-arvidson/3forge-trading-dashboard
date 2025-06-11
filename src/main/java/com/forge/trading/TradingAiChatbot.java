package com.forge.trading;

import java.util.ArrayList;
import java.util.List;

import com.f1.ami.amicommon.customobjects.AmiScriptAccessible;

@AmiScriptAccessible(name = "TradingAiChatbot")
public class TradingAiChatbot {
    
    // Add conversation history for message stacking
    private static List<ChatMessage> conversationHistory = new ArrayList<>();
    
    // Remember last suggested trader for context
    private static String lastSuggestedTrader = null;
    
    // Helper class for chat messages
    private static class ChatMessage {
        String userInput;
        String aiResponse;
        String timestamp;
        
        ChatMessage(String userInput, String aiResponse) {
            this.userInput = userInput;
            this.aiResponse = aiResponse;
            this.timestamp = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());
        }
    }
    
    @AmiScriptAccessible
    public TradingAiChatbot() {
        // Simple constructor - no dependencies
    }
    

    
    @AmiScriptAccessible(name = "debugInfo")
    public String debugInfo() {
        return "TradingAiChatbot v1.0 - Mock AI responses active";
    }
    
    @AmiScriptAccessible(name = "simpleTest")
    public String simpleTest() {
        return "Hello from TradingAiChatbot!";
    }
    
    @AmiScriptAccessible(name = "processChatMessage", params = {"userMessage"})
    public String processChatMessage(Object userMessage) {
        try {
            String message = userMessage.toString().toLowerCase();
            
            // Simple command parsing for demo
            if (message.contains("sarah jones") || message.contains("sarah")) {
                return "{\n" +
                       "  \"message\": \"📊 Showing performance for Sarah Jones. I've updated all dashboard panels with their trading data.\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Sarah Jones\"\n" +
                       "}";
            } else if (message.contains("mike chen") || message.contains("mike")) {
                return "{\n" +
                       "  \"message\": \"📊 Showing performance for Mike Chen. I've updated all dashboard panels with their trading data.\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Mike Chen\"\n" +
                       "}";
            } else if (message.contains("lisa wang") || message.contains("lisa")) {
                return "{\n" +
                       "  \"message\": \"📊 Showing performance for Lisa Wang. I've updated all dashboard panels with their trading data.\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Lisa Wang\"\n" +
                       "}";
            } else if (message.contains("reset") || message.contains("clear") || message.contains("show all")) {
                return "{\n" +
                       "  \"message\": \"🔄 Dashboard reset! Showing all traders and market data.\",\n" +
                       "  \"command\": \"RESET_DASHBOARD\"\n" +
                       "}";
            } else if (message.contains("help")) {
                return getChatHelpMessage();
            } else {
                return getSmartChatResponse(message);
            }
            
        } catch (Exception e) {
            return "{\n" +
                   "  \"message\": \"Error: " + e.getMessage() + "\"\n" +
                   "}";
        }
    }
    
    private String getSmartChatResponse(String message) {
        // Simple demo-focused conversation patterns
        
        // TOP PERFORMER QUERIES  
        if (message.contains("best") || message.contains("top performer") || message.contains("top trader") || message.contains("who's the")) {
            lastSuggestedTrader = "Mike Chen";  // Remember for context
            return "{\n" +
                   "  \"message\": \"🏆 Mike Chen is currently our top performer with +$4,521 P&L. He specializes in tech/semiconductor trades.\\n\\nWould you like me to filter the dashboard to show his detailed performance?\"\n" +
                   "}";
        }
        
        // "WHO TRADES TSLA?" - DEMO ESSENTIAL
        else if (message.contains("who trades") && (message.contains("TSLA") || message.contains("tesla"))) {
            return "{\n" +
                   "  \"message\": \"⚡ <strong>TSLA Traders:</strong><br><br>🚀 <strong>Sarah Jones</strong> - Growth specialist<br>⚡ <strong>Lisa Wang</strong> - Innovation/EV focus<br><br>Both are aggressive with high-growth names. Want to see their performance?\"\n" +
                   "}";
        }
        
        // DASHBOARD CONTROL COMMANDS
        else if (message.contains("reset") || message.contains("clear") || message.contains("show all")) {
            return "{\n" +
                   "  \"message\": \"🔄 Resetting dashboard to show all traders and clearing any filters...\",\n" +
                   "  \"command\": \"RESET_DASHBOARD\"\n" +
                   "}";
        }
        
        // SPECIFIC TRADER QUERIES (enhanced with personalities)
        else if (message.contains("john smith") || message.contains("john")) {
            lastSuggestedTrader = "John Smith";  // Remember for context
            return "{\n" +
                   "  \"message\": \"👔 John Smith is our Big Tech specialist, focusing on AAPL, MSFT, and GOOGL. Steady performer with consistent volume.\\n\\nWould you like me to filter the dashboard to show John's detailed metrics?\"\n" +
                   "}";
        }
        else if (message.contains("lisa wang") || message.contains("lisa")) {
            lastSuggestedTrader = "Lisa Wang";  // Remember for context
            return "{\n" +
                   "  \"message\": \"🚀 Lisa Wang is our innovation trader - TSLA, META, NVDA are her specialties. She's aggressive with AI and EV stocks.\\n\\nShall I show you Lisa's detailed performance dashboard?\"\n" +
                   "}";
        }
        else if (message.contains("tom brown") || message.contains("tom")) {
            lastSuggestedTrader = "Tom Brown";  // Remember for context
            return "{\n" +
                   "  \"message\": \"💼 Tom Brown handles enterprise tech - AMZN, ORCL, INTC. More conservative approach but solid fundamentals.\\n\\nWant to see Tom's dashboard in detail?\"\n" +
                   "}";
        }
        else if (message.contains("emma davis") || message.contains("emma")) {
            lastSuggestedTrader = "Emma Davis";  // Remember for context
            return "{\n" +
                   "  \"message\": \"💎 Emma Davis focuses on mega-cap stability - AAPL, AMZN, MSFT. Strong risk-adjusted returns.\\n\\nShould I filter to show Emma's position details?\"\n" +
                   "}";
        }
        
        // VOLUME & TRADING ACTIVITY
        else if (message.contains("volume") || message.contains("activity") || message.contains("busy")) {
            return "{\n" +
                   "  \"message\": \"📊 Trading volume varies by trader and symbol. Mike Chen typically shows highest activity in semiconductor names. Want to see current volume leaders?\"\n" +
                   "}";
        }
        
        // CONFIRMATION RESPONSES FOR FILTERING
        else if (message.contains("yes") || message.contains("sure") || message.contains("ok") || message.contains("please")) {
            // Use remembered context first, then check for explicit names
            if (lastSuggestedTrader != null) {
                String trader = lastSuggestedTrader;
                lastSuggestedTrader = null; // Clear after use
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show " + trader + "'s performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"" + trader + "\"\n" +
                       "}";
            }
            // Fallback to checking for explicit names in message
            else if (message.contains("mike") || message.contains("chen")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Mike Chen's performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Mike Chen\"\n" +
                       "}";
            } else if (message.contains("sarah") || message.contains("jones")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Sarah Jones' performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Sarah Jones\"\n" +
                       "}";
            } else if (message.contains("lisa") || message.contains("wang")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Lisa Wang's performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Lisa Wang\"\n" +
                       "}";
            } else if (message.contains("john") || message.contains("smith")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show John Smith's performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"John Smith\"\n" +
                       "}";
            } else if (message.contains("tom") || message.contains("brown")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Tom Brown's performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Tom Brown\"\n" +
                       "}";
            } else if (message.contains("emma") || message.contains("davis")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Emma Davis' performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Emma Davis\"\n" +
                       "}";
            } else {
                return "{\n" +
                       "  \"message\": \"I'd be happy to filter the dashboard! Just let me know which trader you'd like to see: Mike Chen, Sarah Jones, Lisa Wang, John Smith, Tom Brown, or Emma Davis.\"\n" +
                       "}";
            }
        }
        
        // DIRECT FILTER REQUESTS
        else if (message.contains("show me") || message.contains("filter")) {
            // Handle direct "show me [trader]" requests
            if (message.contains("mike") || message.contains("chen")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Mike Chen's performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Mike Chen\"\n" +
                       "}";
            } else if (message.contains("sarah") || message.contains("jones")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Sarah Jones' performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Sarah Jones\"\n" +
                       "}";
            } else if (message.contains("lisa") || message.contains("wang")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Lisa Wang's performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Lisa Wang\"\n" +
                       "}";
            } else if (message.contains("john") || message.contains("smith")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show John Smith's performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"John Smith\"\n" +
                       "}";
            } else if (message.contains("tom") || message.contains("brown")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Tom Brown's performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Tom Brown\"\n" +
                       "}";
            } else if (message.contains("emma") || message.contains("davis")) {
                return "{\n" +
                       "  \"message\": \"📊 Filtering dashboard to show Emma Davis' performance...\",\n" +
                       "  \"command\": \"FILTER_TRADER\",\n" +
                       "  \"trader\": \"Emma Davis\"\n" +
                       "}";
            } else {
                return "{\n" +
                       "  \"message\": \"I'd be happy to filter the dashboard! Just let me know which trader you'd like to see: Mike Chen, Sarah Jones, Lisa Wang, John Smith, Tom Brown, or Emma Davis.\"\n" +
                       "}";
            }
        }
        
        // HELP SYSTEM - SIMPLE
        else if (message.contains("help") || message.contains("what can you do") || message.contains("examples")) {
            return getChatHelpMessage();
        }
        
        // DEFAULT INTELLIGENT RESPONSE
        else {
            return "{\n" +
                   "  \"message\": \"🤖 I can help you analyze our trading team! Try asking about:\\n• Specific traders: 'How is Mike Chen doing?'\\n• Stock focus: 'Who trades TSLA?'\\n• Performance: 'Who's the top performer?'\\n• Risk: 'Show me risk metrics'\\n• Or just say 'help' for more options\"\n" +
                   "}";
        }
    }
    
    private String getChatHelpMessage() {
        return "{\n" +
               "  \"message\": \"🚀 <strong>Demo Trading Assistant</strong><br><br>" +
               "Try these key questions:<br><br>" +
               "🏆 <strong>'Who's the top performer?'</strong> - See our best trader<br>" +
               "👥 <strong>'How is Mike Chen doing?'</strong> - Filter to specific trader<br>" +
               "🔄 <strong>'Reset dashboard'</strong> - Clear all filters<br><br>" +
               "Just ask naturally - I understand trading language!\"\n" +
               "}";
    }
    
    @AmiScriptAccessible(name = "generateChatHtml", params = {"userInput", "chatResponse"})
    public String generateChatHtml(Object userInput, Object chatResponse) {
        try {
            String userMessage = userInput.toString();
            String jsonResponse = chatResponse.toString();
            String aiMessage = extractMessageFromJson(jsonResponse);
            
            System.out.println("💬 Generating chat HTML for: " + userMessage);
            System.out.println("🤖 AI Response: " + aiMessage);
            
            // Add to conversation history
            conversationHistory.add(new ChatMessage(userMessage, aiMessage));
            
            // Build all messages HTML with dynamic sizing based on content
            StringBuilder messagesHtml = new StringBuilder();
            
            for (ChatMessage msg : conversationHistory) {
                // Calculate dynamic heights based on message content
                int userHeight = estimateMessageHeight(msg.userInput);
                int aiHeight = estimateMessageHeight(msg.aiResponse);
                int aiTopPosition = Math.max(userHeight + 15, 60); // User message + padding, min 60px
                int totalHeight = aiTopPosition + aiHeight + 20; // Actual space needed: ai start + ai height + padding
                int containerMargin = 25; // Consistent spacing between pairs
                
                messagesHtml.append(String.format(
                    "            <div class=\"message-pair\" style=\"min-height: %dpx; margin-bottom: %dpx;\">\n" +
                    "                <div class=\"user-message\">\n" +
                    "                    %s\n" +
                    "                </div>\n" +
                    "                \n" +
                    "                <div class=\"ai-message\" style=\"top: %dpx; max-height: %dpx;\">\n" +
                    "                    <span class=\"ai-label\">🤖</span> %s\n" +
                    "                </div>\n" +
                    "            </div>\n",
                    totalHeight,
                    containerMargin,
                    escapeHtml(msg.userInput),
                    aiTopPosition,
                    aiHeight + 20, // AI message max height with padding
                    msg.aiResponse  // Don't escape AI response - it contains intentional HTML
                ));
            }
            
            return String.format(
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background: #f8f9fa;\n" +
                "            width: 100%%;\n" +
                "            height: 100%%;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        \n" +
                "        .chat-container {\n" +
                "            position: absolute;\n" +
                "            top: 0;\n" +
                "            left: 0;\n" +
                "            width: 100%%;\n" +
                "            height: 100%%;\n" +
                "        }\n" +
                "        \n" +
                "        .chat-header {\n" +
                "            position: absolute;\n" +
                "            top: 0;\n" +
                "            left: 0;\n" +
                "            width: 100%%;\n" +
                "            height: 40px;\n" +
                "            background: #fff;\n" +
                "            border-bottom: 2px solid #28a745;\n" +
                "            color: #333;\n" +
                "            text-align: center;\n" +
                "            font-weight: bold;\n" +
                "            font-size: 14px;\n" +
                "            line-height: 40px;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        \n" +
                "        .messages-area {\n" +
                "            position: absolute;\n" +
                "            top: 50px;\n" +
                "            left: 10px;\n" +
                "            right: 10px;\n" +
                "            bottom: 10px;\n" +
                "            overflow-y: auto;\n" +
                "            box-sizing: border-box;\n" +
                "            padding: 10px 0;\n" +
                "            display: grid;\n" +
                "            grid-template-columns: 1fr;\n" +
                "            grid-auto-rows: min-content;\n" +
                "            gap: 5px;\n" +
                "        }\n" +
                "        \n" +
                "        .message-pair {\n" +
                "            position: relative;\n" +
                "            width: 100%%;\n" +
                "            /* Dynamic sizing via inline styles */\n" +
                "        }\n" +
                "        \n" +
                "        .user-message {\n" +
                "            position: absolute;\n" +
                "            top: 5px;\n" +
                "            right: 0;\n" +
                "            width: 70%%;\n" +
                "            padding: 12px 16px;\n" +
                "            background: #28a745;\n" +
                "            color: white;\n" +
                "            border-radius: 12px;\n" +
                "            border-bottom-right-radius: 4px;\n" +
                "            box-sizing: border-box;\n" +
                "            word-wrap: break-word;\n" +
                "            line-height: 1.4;\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "        \n" +
                "        .ai-message {\n" +
                "            position: absolute;\n" +
                "            left: 0;\n" +
                "            width: 70%%;\n" +
                "            padding: 12px 16px;\n" +
                "            background: #fff;\n" +
                "            color: #333;\n" +
                "            border: 1px solid #dee2e6;\n" +
                "            border-radius: 12px;\n" +
                "            border-bottom-left-radius: 4px;\n" +
                "            box-sizing: border-box;\n" +
                "            word-wrap: break-word;\n" +
                "            line-height: 1.4;\n" +
                "            font-size: 14px;\n" +
                "            box-shadow: 0 1px 3px rgba(0,0,0,0.1);\n" +
                "            overflow-y: auto;\n" +
                "            /* Dynamic positioning and sizing via inline styles */\n" +
                "        }\n" +
                "        \n" +
                "        .ai-label {\n" +
                "            color: #28a745;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"chat-container\">\n" +
                "        <div class=\"chat-header\">\n" +
                "            🤖 AI Trading Assistant (%d messages)\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"messages-area\">\n" +
                "%s" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>",
                conversationHistory.size(),
                messagesHtml.toString()
            );
            
        } catch (Exception e) {
            System.out.println("❌ Error generating chat HTML: " + e.getMessage());
            return "Error generating chat HTML: " + e.getMessage();
        }
    }
    
    public String extractMessageFromJson(String jsonResponse) {
        // Simple JSON message extraction
        try {
            // Look for "message": "content" pattern
            int messageStart = jsonResponse.indexOf("\"message\":");
            if (messageStart == -1) {
                return jsonResponse; // Return as-is if no JSON message field
            }
            
            // Find the start of the message value (after the colon and optional whitespace)
            int valueStart = jsonResponse.indexOf("\"", messageStart + 10);
            if (valueStart == -1) {
                return jsonResponse;
            }
            
            // Find the end of the message value
            int valueEnd = valueStart + 1;
            while (valueEnd < jsonResponse.length()) {
                char c = jsonResponse.charAt(valueEnd);
                if (c == '"' && jsonResponse.charAt(valueEnd - 1) != '\\') {
                    break; // Found unescaped quote
                }
                valueEnd++;
            }
            
            if (valueEnd >= jsonResponse.length()) {
                return jsonResponse; // Malformed JSON
            }
            
            String message = jsonResponse.substring(valueStart + 1, valueEnd);
            
            // Unescape basic JSON escapes
            message = message.replace("\\\"", "\"")
                            .replace("\\n", "\n")
                            .replace("\\t", "\t")
                            .replace("\\\\", "\\");
            
            return message;
            
        } catch (Exception e) {
            System.out.println("Error extracting message from JSON: " + e.getMessage());
            return jsonResponse; // Return original if extraction fails
        }
    }
    
    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    private int estimateMessageHeight(String message) {
        if (message == null || message.isEmpty()) {
            return 40; // Minimum height for empty message
        }
        
        // Hardcoded heights based on actual DOM measurements for SIMPLIFIED chatbot
        if (message.contains("Demo Trading Assistant") || message.contains("Try these key questions")) {
            return 245; // Simplified help message
        } else if (message.contains("top performer") || message.contains("Mike Chen is currently our top performer")) {
            return 130; // Performance messages
        } else if (message.contains("TSLA Traders") || message.contains("Sarah Jones</strong> - Growth specialist")) {
            return 130; // "Who trades TSLA?" response
        } else if (message.contains("Filtering dashboard") || message.contains("Resetting dashboard")) {
            return 65;  // Confirmation messages
        } else if (message.contains("specializes in") || message.contains("tech/semiconductor trades")) {
            return 100; // Trader description messages
        } else {
            return 80;  // Default short messages
        }
    }
    
    @AmiScriptAccessible(name = "getChatHistory")
    public String getChatHistory() {
        return String.format("Chat history: %d messages", conversationHistory.size());
    }
    
    @AmiScriptAccessible(name = "clearChatHistory")
    public String clearChatHistory() {
        conversationHistory.clear();
        System.out.println("🧹 Chat history cleared");
        return "Chat history cleared - ready for new conversation!";
    }
    
    @AmiScriptAccessible(name = "chatTest")
    public String chatTest() {
        return "🤖 Chat functionality is working through TradingAiChatbot!";
    }
    
    public String extractJsonField(String jsonResponse, String fieldName) {
        try {
            String fieldPattern = "\"" + fieldName + "\":";
            int fieldStart = jsonResponse.indexOf(fieldPattern);
            if (fieldStart == -1) {
                return null;
            }
            
            fieldStart = jsonResponse.indexOf("\"", fieldStart + fieldPattern.length());
            if (fieldStart == -1) {
                return null;
            }
            
            int fieldEnd = jsonResponse.indexOf("\"", fieldStart + 1);
            if (fieldEnd == -1) {
                return null;
            }
            
            return jsonResponse.substring(fieldStart + 1, fieldEnd);
            
        } catch (Exception e) {
            System.out.println("Error extracting field " + fieldName + ": " + e.getMessage());
            return null;
        }
    }
    

} 