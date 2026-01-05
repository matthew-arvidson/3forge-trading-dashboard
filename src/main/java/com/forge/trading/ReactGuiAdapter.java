package com.forge.trading;

import java.util.List;

import com.f1.ami.web.AmiWebGuiServiceAdapter;
import com.f1.ami.web.AmiWebGuiServiceAdapterPeer;
import com.f1.utils.CH;
import com.f1.utils.structs.table.derived.ParamsDefinition;

/**
 * React GUI Service Adapter
 * 
 * Defines the AmiScript API and JavaScript callbacks for React component integration
 * 
 * Phase 1: Simple alert + date callback to verify bidirectional communication
 * Phase 2: Add React mounting methods
 * Phase 3: Add AG-Grid data binding methods
 */
public class ReactGuiAdapter implements AmiWebGuiServiceAdapter {

    private AmiWebGuiServiceAdapterPeer peer;

    @Override
    public void init(AmiWebGuiServiceAdapterPeer peer) {
        this.peer = peer;
        System.out.println("ReactGuiAdapter initialized with peer");
    }

    @Override
    public String getGuiServiceId() {
        return "REACTGUI";
    }

    @Override
    public String getDescription() {
        return "React GUI Integration Service";
    }

    @Override
    public String getAmiscriptClassname() {
        return "ReactGui";
    }

    /**
     * Define methods callable from AmiScript
     * 
     * Phase 1: showAlert(message) - displays browser alert and returns timestamp
     * Phase 2: mountComponent(elementId, component, props) - mount React component
     */
    @Override
    public List<ParamsDefinition> getAmiscriptMethods() {
        return CH.l(
            new ParamsDefinition("showAlert", Object.class, "String message"),
            new ParamsDefinition("mountComponent", Object.class, "String elementId,String component,Object props")
        );
    }

    /**
     * Handle AmiScript method calls - forward to JavaScript
     */
    @Override
    public Object onAmiScriptMethod(String name, Object[] args) {
        System.out.println("AmiScript called method: " + name + " with " + args.length + " args");
        
        // Forward to JavaScript in browser
        this.peer.executeJavascriptCallback(name, args);
        
        return null;
    }

    /**
     * Define callbacks that AmiScript developers can override
     * 
     * Phase 1: onAlertClosed(timestamp) - called when user dismisses alert
     * Phase 2: onComponentEvent(elementId, eventName, eventData) - called when React component triggers an event
     */
    @Override
    public List<ParamsDefinition> getAmiScriptCallbacks() {
        return CH.l(
            new ParamsDefinition("onAlertClosed", Object.class, "String timestamp"),
            new ParamsDefinition("onComponentEvent", Object.class, "String elementId,String eventName,Object eventData")
        );
    }

    /**
     * Handle JavaScript-to-AmiScript callbacks
     */
    @Override
    public void onCallFromJavascript(String name, Object args[]) {
        System.out.println("JavaScript called: " + name + " with " + args.length + " args");
        
        // Execute the AmiScript callback
        this.peer.executeAmiScriptCallback(name, args);
    }

    /**
     * JavaScript libraries to load in browser
     * Phase 1: Load ReactGuiService.js (bridge)
     * Phase 2: Load forge-react.js (React bundle with components)
     */
    @Override
    public List<String> getJavascriptLibraries() {
        return CH.l(
            "http://localhost:33332/resources/forge-react.js",      // React components bundle
            "http://localhost:33332/resources/ReactGuiService.js"   // 3forge bridge
        );
    }

    @Override
    public String getJavascriptInitialization() {
        // Runs before external library loads
        return "";
    }

    /**
     * Create the JavaScript singleton instance
     */
    @Override
    public String getJavascriptNewInstance() {
        return "new ReactGuiService()";
    }

    @Override
    public void onLayoutStartup() {
        System.out.println("ReactGuiAdapter: Layout started");
    }

    @Override
    public void onPageLoading() {
        System.out.println("ReactGuiAdapter: Page loading");
    }

    @Override
    public String getJavascriptCloseInstance() {
        return "";
    }
}

