# 🐛 Debugging 3forge in Cursor (VS Code-like)

## ✅ Setup Complete!

Your Cursor IDE is now configured for Java debugging with:
- **Remote debugging configuration** in `.vscode/launch.json`
- **Java project settings** in `.vscode/settings.json`
- **Debug method** in `TradingDashboardManager.java`

## 🚀 Step-by-Step Debugging Process

### Step 1: Start 3forge in Debug Mode
```bash
.\debug-3forge.ps1
```

This will:
- Build and deploy your Java classes
- Start 3forge with remote debugging enabled on port 5005
- Show connection instructions

### Step 2: Set Breakpoints in Cursor

1. **Open** `src/main/java/com/forge/trading/TradingDashboardManager.java`
2. **Find** the `debugBreakpoint` method (around line 102)
3. **Click** in the gutter (left margin) next to these lines to set breakpoints:
   ```java
   String traderName = trader.toString();        // ← Click here
   ```
   ```java
   Object panel = getPanelMethod.invoke(...);    // ← Click here  
   ```
   ```java
   Class<?> panelClass = panel.getClass();       // ← Click here
   ```

### Step 3: Connect Cursor Debugger

1. **Open** the Debug panel: `Ctrl+Shift+D` (or click the bug icon in sidebar)
2. **Select** "🐛 Debug 3forge Remote" from the dropdown
3. **Click** the green play button (▶️) or press `F5`

You should see:
- ✅ "Debugger attached" message
- 🔗 Connection indicator in status bar

### Step 4: Trigger the Breakpoint

1. **Open** your dashboard: http://localhost:33332/3forge
2. **Click** any trader name in the trades table
3. **Watch** Cursor pause at your breakpoint! 🎯

### Step 5: Inspect Objects (The Magic Part!)

When the breakpoint hits, use Cursor's debug panels:

**Variables Panel** (left side):
- Expand `layout` object
  - See class name: `layout.getClass().getName()`
  - See available methods: `layout.getClass().getMethods()`
- Expand `panel` object  
  - See class name: `panel.getClass().getName()`
  - See available methods: `panel.getClass().getMethods()`

**Debug Console** (bottom):
Type commands to explore:
```java
panel.getClass().getSimpleName()
panel.getClass().getMethods()
```

Look for methods like:
- `setHtmlTemplate2()`
- `setValue()`
- `setContent()`
- `refresh()`

## 🎯 What We're Looking For

**Panel Type**: What exact class is the HTML panel?
- Might be: `AmiFormPanel`, `HtmlPanel`, `WebPanel`, etc.

**Update Methods**: What methods can update the HTML?
- Look for: `setHtml*`, `set*Template*`, `setValue*`, `setContent*`

**Method Signatures**: What parameters do they expect?
- String only? `setValue(String html)`
- Key-value pair? `setValue(String key, String value)`  
- Template name? `setTemplate(String templateName, String html)`

## 🔧 Debugging Tips

**Continue Execution**: Press `F5` to continue after inspecting
**Step Through**: Use `F10` (step over) or `F11` (step into)
**Hot Reload**: Make code changes and they'll apply on next breakpoint

**Debug Console Commands**:
```java
// Check panel methods
Arrays.stream(panel.getClass().getMethods()).forEach(m -> System.out.println(m.getName()))

// Try method calls
panel.getClass().getMethod("setValue", String.class)
```

## 🎉 Expected Outcome

After 1-2 debugging sessions, you'll discover:
1. **Exact panel class name**
2. **Correct method to call** for HTML updates  
3. **Proper parameters** to pass
4. **Why our reflection attempts failed**

Then we can implement the **real HTML panel update** method! 

## 🐛 Troubleshooting

**Debugger won't connect?**
- Check 3forge is running with debug flags
- Verify port 5005 is open: `netstat -an | findstr 5005`

**Breakpoints not hitting?**
- Ensure 3forge loaded your latest Java classes
- Check AMI script is calling `debugBreakpoint()` method

**No variables showing?**  
- Make sure you're using the Java debugging configuration (not Node.js)
- Verify Java extension is active 