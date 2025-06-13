#!/usr/bin/env python3
"""
OpenAI Proxy Server for 3forge Trading Dashboard
Simple Flask app that receives requests from 3forge and proxies them to OpenAI API
"""

from flask import Flask,jsonify,request,Response
from flask_restful import Resource,Api,reqparse
import requests
import json
import os
import sqlite3
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

parser = reqparse.RequestParser()
parser.add_argument('title',required=True)
app = Flask(__name__)
api = Api(app)

class Home(Resource):
    def __init__(self):
        pass
    def get(self):
        return {
            "msg": "welcome to the home page"
        }
api.add_resource(Home, '/')

# OpenAI Configuration
OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"
OPENAI_API_KEY = os.getenv('OPENAI_API_KEY')

if not OPENAI_API_KEY:
    raise ValueError("No OpenAI API key found. Please set OPENAI_API_KEY in .env file")

# Restore original SYSTEM_PROMPT
SYSTEM_PROMPT = """
You are a trading dashboard AI assistant for a demo environment.
Here is everything you know about the dashboard and its data:

Traders:
- Mike Chen: top performer, tech/semiconductor, trades GOOGL, NVDA, AMD
- Sarah Jones: growth specialist, trades AAPL, TSLA, META
- Lisa Wang: innovation/EV, trades TSLA, META, NVDA
- John Smith: big tech, trades AAPL, MSFT, GOOGL
- Tom Brown: enterprise, trades AMZN, ORCL, INTC
- Emma Davis: mega-cap, trades AAPL, AMZN, MSFT

Symbols:
- AAPL, GOOGL, MSFT, AMZN, TSLA, META, NVDA, AMD, INTC, ORCL

Dashboard Panels:
- Top left: Trades table (1,000 trades)
- Top right: P&L bar chart
- Bottom left: Heatmap by symbol
- Bottom center: Metrics (P&L, trades, top trader, win rate)
- Bottom right: Chat assistant

Commands:
- To filter for a trader, return: {"message": "...", "command": "FILTER_TRADER", "trader": "<name>"}
- To reset the dashboard, return: {"message": "...", "command": "RESET_DASHBOARD"}

IMPORTANT:
- The only traders in the system are: Mike Chen, Sarah Jones, Lisa Wang, John Smith, Tom Brown, Emma Davis. There are no other traders.
- If a user asks for a trader not in this list, respond that no such trader exists.
- Do not invent or suggest traders that are not in the list.
- If a user asks for a list of traders, or for all traders, provide the full list as shown above.
- If a user asks for a list of traders and their performance, provide the list with P&L values.
- When a user types "reset", "reset dashboard", "clear", "show all", or similar commands, ALWAYS return the RESET_DASHBOARD command.

EXAMPLES:
Q: Show me Sarah
A: {"message": "Showing performance for Sarah Jones...", "command": "FILTER_TRADER", "trader": "Sarah Jones"}

Q: Show me Jim Smith
A: {"message": "I'm sorry, there is no trader with the name Sarah Smith in the system. Please try again.", "command": null, "trader": null}

Q: Who is the best performer?
A: {"message": "The top performer is Mike Chen.", "command": null, "trader": null}

Q: Show me symbols? (or show me all symbols) (or show me all symbols and their performance)
A: {"message": "The top symbols are AAPL, GOOGL, MSFT, AMZN, TSLA, META, NVDA, AMD, INTC, ORCL.", "command": null, "trader": null}

Q: Show me all traders
A: {"message": "The traders in the system are: Mike Chen, Sarah Jones, Lisa Wang, John Smith, Tom Brown, Emma Davis.", "command": null, "trader": null}

Q: Show me all top traders
A: {"message": "The top traders are: Mike Chen, Sarah Jones, Lisa Wang, John Smith, Tom Brown, Emma Davis.", "command": null, "trader": null}

Q: List all traders and their P&L
A: {"message": "Mike Chen: +$8,247, Sarah Jones: +$2,847.50, Lisa Wang: +$1,500, John Smith: +$1,000, Tom Brown: +$500, Emma Davis: +$250", "command": null, "trader": null}

Q: reset
A: {"message": "🔄 Dashboard reset! Showing all traders and market data.", "command": "RESET_DASHBOARD"}

Q: reset dashboard
A: {"message": "🔄 Dashboard reset! Showing all traders and market data.", "command": "RESET_DASHBOARD"}

Q: clear
A: {"message": "🔄 Dashboard reset! Showing all traders and market data.", "command": "RESET_DASHBOARD"}

Q: show all
A: {"message": "🔄 Dashboard reset! Showing all traders and market data.", "command": "RESET_DASHBOARD"}

Always respond in this JSON format:
{
  "message": "<your natural language response>",
  "command": "<optional: FILTER_TRADER, RESET_DASHBOARD, etc.>",
  "trader": "<optional: trader name>"
}
Do not use code blocks or trailing commas.
"""

# Query and append top 5 rows from each table
conn = sqlite3.connect('trading_data.db')
cursor = conn.cursor()
tables = ['trades', 'positions', 'marketdata', 'riskmetrics', 'orders']
table_summaries = "\n\nDATA SNAPSHOT (top 5 rows per table):\n"
for table in tables:
    cursor.execute(f"SELECT * FROM {table} LIMIT 5")
    rows = cursor.fetchall()
    col_names = [description[0] for description in cursor.description]
    table_summaries += f"\nTable: {table}\nColumns: {', '.join(col_names)}\n"
    for row in rows:
        table_summaries += f"{row}\n"
conn.close()

SYSTEM_PROMPT += table_summaries

#show general info
#url:http://127.0.0.1:5000/info/
all = {     
 "page": 1, 
 "per_page": 6, 
 "total": 12, 
 "total_pages":14, 
 "data": [
     {
      "id": 1,
      "name": "alice",
      "year": 2000,
     },
     {
      "id": 2,
      "name": "bob", 
      "year": 2001, 
     },
     {
      "id": 3, 
      "name": "charlie", 
      "year": 2002, 
     }, 
     {
      "id": 4, 
      "name": "Doc",
      "year": 2003,
     }
      ]
}

@app.route('/info/', methods=['GET'])
def show_info():
    return jsonify(all)

@app.route('/test/', methods=['GET'])
def test_simple():
    return jsonify({"message": "Hello from Flask test endpoint"})

user_histories = {}  # user_id -> list of messages

@app.route('/chat', methods=['GET'])
def chat():
    """
    Main chat endpoint that 3forge will call
    Expects GET with query parameter: /chat?q=user_question&user_id=someuser
    Returns JSON: {"message": ..., "command": ..., "trader": ..., "status": ...}
    """
    try:
        # Get user query and user_id from URL parameters
        user_query = request.args.get('q', '')
        user_id = request.args.get('user_id', 'default')
        if not user_query:
            return jsonify({
                "message": "🤖 Error: No query provided",
                "command": None,
                "trader": None,
                "status": "error"
            })
        
        print(f"📨 Received query: {user_query} (user_id: {user_id})")
        
        # Get or create history for this user
        history = user_histories.setdefault(user_id, [])
        # Add user message
        history.append({"role": "user", "content": user_query})
        
        # Build messages for OpenAI
        messages = [{"role": "system", "content": SYSTEM_PROMPT}] + history
        
        openai_request = {
            "model": "gpt-3.5-turbo",
            "messages": messages,
            "max_tokens": 150,
            "temperature": 0.7
        }
        
        # Make OpenAI API call
        headers = {
            "Authorization": f"Bearer {OPENAI_API_KEY}",
            "Content-Type": "application/json"
        }
        
        print("🔄 Calling OpenAI API...")
        response = requests.post(
            OPENAI_API_URL, 
            headers=headers, 
            json=openai_request, 
            timeout=10
        )
        
        if response.status_code == 200:
            openai_data = response.json()
            ai_response = openai_data['choices'][0]['message']['content']
            print(f"✅ OpenAI response: {ai_response[:100]}...")
            
            # Try to parse the response as JSON
            try:
                parsed = json.loads(ai_response)
                message = parsed.get("message")
                command = parsed.get("command")
                trader = parsed.get("trader")
            except Exception as e:
                # Try to clean up common issues and parse again
                try:
                    cleaned = ai_response.strip('` \n')
                    cleaned = cleaned.replace(",\n}", "\n}").replace(",\r\n}", "\r\n}")
                    parsed = json.loads(cleaned)
                    message = parsed.get("message")
                    command = parsed.get("command")
                    trader = parsed.get("trader")
                except Exception:
                    message = ai_response
                    command = None
                    trader = None
            
            # Add assistant response to history
            history.append({"role": "assistant", "content": ai_response})
            # Optionally trim history to last 20 messages
            if len(history) > 20:
                user_histories[user_id] = history[-20:]
            
            return jsonify({
                "message": message,
                "command": command,
                "trader": trader,
                "status": "success"
            })
        else:
            print(f"❌ OpenAI API error: {response.status_code}")
            return jsonify({
                "message": "🤖 I can help you analyze our trading team! Try asking about specific traders, performance metrics, or market insights.",
                "command": None,
                "trader": None,
                "status": "fallback"
            })
            
    except requests.exceptions.Timeout:
        print("⏰ OpenAI API timeout")
        return jsonify({
            "message": "🤖 I can help you analyze our trading team! Try asking about specific traders, performance metrics, or market insights.",
            "command": None,
            "trader": None,
            "status": "fallback"
        })
    except Exception as e:
        print(f"💥 Error: {str(e)}")
        return jsonify({
            "message": "🤖 I can help you analyze our trading team! Try asking about specific traders, performance metrics, or market insights.",
            "command": None,
            "trader": None,
            "status": "fallback"
        })

@app.route('/test', methods=['GET'])
def test():
    """Test endpoint with sample query"""
    return chat_proxy({"query": "Who is the top performer?"})

if __name__ == '__main__':
    print("🚀 Starting OpenAI Proxy Server for 3forge...")
    print("📍 Endpoints:")
    print("   GET  /       - Health check")
    print("   POST /chat   - Main chat endpoint")
    print("   GET  /test   - Test with sample query")
    
    # Run on localhost:5000
    app.run(debug=True, host='127.0.0.1', port=5000) 