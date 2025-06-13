package com.forge.trading;

import java.util.ArrayList;
import java.util.List;

import com.f1.ami.amicommon.customobjects.AmiScriptAccessible;

@AmiScriptAccessible(name = "TradingAiChatbotFinal")
public class TradingAiChatbotFinal {
    

    // MINIMAL VERSION - Just copy exact working logic from TradingAiChatbot
    private static List<ChatMessage> conversationHistory = new ArrayList<>();
    private static String lastSuggestedTrader = null;
    
    // Helper class for chat messages - exact copy
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
    public TradingAiChatbotFinal() {
        // Simple constructor - no dependencies
    }
    
    @AmiScriptAccessible(name = "debugInfo")
    public String debugInfo() {
        return "TradingAiChatbotFinal v1.0 - Phase 3: Session context integration (testing access to AMI script layer)";
    }
    
    @AmiScriptAccessible(name = "simpleTest")
    public String simpleTest() {
        return "Hello from TradingAiChatbotFinal!";
    }
    
    @AmiScriptAccessible(name = "processChatMessage", params = {"userMessage"})
    public String processChatMessage(Object userMessage) {
        return processChatMessageWithSession(userMessage, null);
    }
    
    @AmiScriptAccessible(name = "processChatMessageWithSession", params = {"userMessage", "session"})
    public String processChatMessageWithSession(Object userMessage, Object session) {
        try {
            String message = userMessage.toString().toLowerCase();
            
            // Phase 3: Try OpenAI for complex/help queries
            if (message.contains("help") || message.contains("who") || message.contains("what") || 
                message.contains("how") || message.contains("risk") || message.contains("performance")) {
                
                String openAIRequest = tryOpenAICallWithSession(message, session);
                if (openAIRequest != null && openAIRequest.contains("openai_request")) {
                    return openAIRequest; // Return flag for AMI script to handle
                }
                // Otherwise fall through to mock responses
            }
            
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
            } else {
                return getSmartChatResponse(message);
            }
            
        } catch (Exception e) {
            return "{\n" +
                   "  \"message\": \"Error: " + e.getMessage() + "\"\n" +
                   "}";
        }
    }
    
    // Phase 2: OpenAI integration using 3forge REST adapter
    private String tryOpenAICall(String userMessage) {
        try {
            // For now, return null to use fallback
            // We'll implement the actual AMI script execution next
            System.out.println("🔄 OpenAI call attempted for: " + userMessage);
            return null;
            
        } catch (Exception e) {
            System.out.println("❌ OpenAI call failed: " + e.getMessage());
            return null;
        }
    }
    
    // EXACT COPY of working generateChatHtml method
    @AmiScriptAccessible(name = "generateChatHtml", params = {"userInput", "chatResponse"})
    public String generateChatHtml(Object userInput, Object chatResponse) {
        try {
            String userMessage = userInput.toString();
            String jsonResponse = chatResponse.toString();
            String aiMessage = extractMessageFromJson(jsonResponse);
            
            // Add to conversation history
            conversationHistory.add(new ChatMessage(userMessage, aiMessage));
            
            // Build all messages HTML with dynamic sizing
            StringBuilder messagesHtml = new StringBuilder();
            
            for (ChatMessage msg : conversationHistory) {
                int userHeight = 80;  // Simplified - no complex calculations
                int aiHeight = 80;
                int aiTopPosition = 95;
                int totalHeight = 175;
                int containerMargin = 25;
                
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
                    aiHeight + 20,
                    msg.aiResponse
                ));
            }
            
            return String.format(
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body { margin: 0; padding: 0; font-family: Arial, sans-serif; background: #f8f9fa; width: 100%%; height: 100%%; overflow: hidden; }\n" +
                "        .chat-container { position: absolute; top: 0; left: 0; width: 100%%; height: 100%%; }\n" +
                "        .chat-header { position: absolute; top: 0; left: 0; width: 100%%; height: 40px; background: #fff; border-bottom: 2px solid #28a745; color: #333; text-align: center; font-weight: bold; font-size: 14px; line-height: 40px; box-sizing: border-box; }\n" +
                "        .messages-area { position: absolute; top: 50px; left: 10px; right: 10px; bottom: 10px; overflow-y: auto; box-sizing: border-box; padding: 10px 0; }\n" +
                "        .message-pair { position: relative; width: 100%%; }\n" +
                "        .user-message { position: absolute; top: 5px; right: 0; width: 70%%; padding: 12px 16px; background: #28a745; color: white; border-radius: 12px; border-bottom-right-radius: 4px; word-wrap: break-word; line-height: 1.4; font-size: 14px; }\n" +
                "        .ai-message { position: absolute; left: 0; width: 70%%; padding: 12px 16px; background: #fff; color: #333; border: 1px solid #dee2e6; border-radius: 12px; border-bottom-left-radius: 4px; word-wrap: break-word; line-height: 1.4; font-size: 14px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }\n" +
                "        .ai-label { color: #28a745; font-weight: bold; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"chat-container\">\n" +
                "        <div class=\"chat-header\">🤖 AI Trading Assistant (%d messages)</div>\n" +
                "        <div class=\"messages-area\">%s</div>\n" +
                "    </div>\n" +
                "    <script>\n" +
                "        // Auto-scroll to bottom of chat\n" +
                "        var messagesArea = document.querySelector('.messages-area');\n" +
                "        messagesArea.scrollTop = messagesArea.scrollHeight;\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>",
                conversationHistory.size(),
                messagesHtml.toString()
            );
            
        } catch (Exception e) {
            return "Error generating chat HTML: " + e.getMessage();
        }
    }
    
    // Essential utility methods - exact copies
    public String extractMessageFromJson(String jsonResponse) {
        try {
            int messageStart = jsonResponse.indexOf("\"message\":");
            if (messageStart == -1) {
                return jsonResponse;
            }
            
            int valueStart = jsonResponse.indexOf("\"", messageStart + 10);
            if (valueStart == -1) {
                return jsonResponse;
            }
            
            int valueEnd = valueStart + 1;
            while (valueEnd < jsonResponse.length()) {
                char c = jsonResponse.charAt(valueEnd);
                if (c == '"' && jsonResponse.charAt(valueEnd - 1) != '\\') {
                    break;
                }
                valueEnd++;
            }
            
            if (valueEnd >= jsonResponse.length()) {
                return jsonResponse;
            }
            
            String message = jsonResponse.substring(valueStart + 1, valueEnd);
            return message.replace("\\\"", "\"").replace("\\n", "\n").replace("\\\\", "\\");
            
        } catch (Exception e) {
            return jsonResponse;
        }
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
    
    // Compatibility methods for TradingDashboardManager
    public String extractJsonField(String jsonResponse, String fieldName) {
        try {
            String fieldPattern = "\"" + fieldName + "\":";
            int fieldStart = jsonResponse.indexOf(fieldPattern);
            if (fieldStart == -1) return null;
            
            fieldStart = jsonResponse.indexOf("\"", fieldStart + fieldPattern.length());
            if (fieldStart == -1) return null;
            
            int fieldEnd = jsonResponse.indexOf("\"", fieldStart + 1);
            if (fieldEnd == -1) return null;
            
            return jsonResponse.substring(fieldStart + 1, fieldEnd);
        } catch (Exception e) {
            return null;
        }
    }
    
    @AmiScriptAccessible(name = "getChatHistory")
    public String getChatHistory() {
        return String.format("Chat history: %d messages", conversationHistory.size());
    }
    
    @AmiScriptAccessible(name = "clearChatHistory")
    public String clearChatHistory() {
        conversationHistory.clear();
        return "Chat history cleared!";
    }
    
    @AmiScriptAccessible(name = "chatTest")
    public String chatTest() {
        return "Minimal chat functionality working!";
    }
    
    // Phase 3: Request OpenAI processing from AMI script layer
    private String tryOpenAICallWithSession(String userMessage, Object session) {
        try {
            System.out.println("🔄 Requesting OpenAI call for: " + userMessage);
            
            // Return a special response that tells AMI script to make OpenAI call
            return "{\n" +
                   "  \"message\": \"🤖 Processing with AI...\",\n" +
                   "  \"openai_request\": true,\n" +
                   "  \"user_query\": \"" + escapeJson(userMessage) + "\"\n" +
                   "}";
            
        } catch (Exception e) {
            System.out.println("❌ OpenAI request failed: " + e.getMessage());
            return null; // Fall back to mock responses
        }
    }
    
    // Helper method to escape JSON strings
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
    
    private String getSmartChatResponse(String message) {
        // Simple demo-focused conversation patterns from original chatbot
        
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
        
        // HELP SYSTEM
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
} 