import markdown

# Read the markdown file
with open('3forge-tutorial.md', 'r', encoding='utf-8') as f:
    content = f.read()

# Convert to HTML
html = markdown.markdown(content, extensions=['codehilite', 'fenced_code'])

# Create styled HTML file
styled_html = f'''<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>3forge Trading Dashboard Tutorial</title>
    <style>
        body {{ 
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; 
            max-width: 900px; 
            margin: 0 auto; 
            padding: 20px; 
            line-height: 1.6; 
            color: #333;
        }}
        code {{ 
            background: #f4f4f4; 
            padding: 2px 4px; 
            border-radius: 3px; 
            font-family: "Consolas", "Monaco", monospace;
        }}
        pre {{ 
            background: #f4f4f4; 
            padding: 15px; 
            border-radius: 5px; 
            overflow-x: auto; 
            border: 1px solid #ddd;
        }}
        h1, h2, h3 {{ color: #2c3e50; }}
        h1 {{ border-bottom: 2px solid #3498db; padding-bottom: 10px; }}
        h2 {{ border-bottom: 1px solid #ecf0f1; padding-bottom: 5px; }}
        blockquote {{ 
            border-left: 4px solid #3498db; 
            margin-left: 0; 
            padding-left: 20px; 
            color: #666; 
            background: #f8f9fa;
            padding: 10px 20px;
            border-radius: 0 5px 5px 0;
        }}
        table {{
            border-collapse: collapse;
            width: 100%;
            margin: 20px 0;
        }}
        th, td {{
            border: 1px solid #ddd;
            padding: 8px 12px;
            text-align: left;
        }}
        th {{
            background-color: #f2f2f2;
            font-weight: bold;
        }}
        .highlight {{
            background: #fff3cd;
            padding: 10px;
            border-radius: 5px;
            border-left: 4px solid #ffc107;
            margin: 10px 0;
        }}
    </style>
</head>
<body>
{html}
</body>
</html>'''

# Write HTML file
with open('3forge-tutorial.html', 'w', encoding='utf-8') as f:
    f.write(styled_html)

print("HTML file created: 3forge-tutorial.html")
print("You can open this in any browser or share it directly!") 