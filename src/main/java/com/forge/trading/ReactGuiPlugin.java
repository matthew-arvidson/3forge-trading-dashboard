package com.forge.trading;

import com.f1.ami.web.AmiWebGuiServiceAdapter;
import com.f1.ami.web.AmiWebGuiServicePlugin;
import com.f1.ami.web.AmiWebService;
import com.f1.container.ContainerTools;
import com.f1.utils.PropertyController;

/**
 * React GUI Service Plugin for 3forge
 * 
 * Phase 1: Basic alert/callback pattern (Hello World)
 * Phase 2: Load React framework
 * Phase 3: AG-Grid component integration
 */
public class ReactGuiPlugin implements AmiWebGuiServicePlugin {

    @Override
    public void init(ContainerTools tools, PropertyController props) {
        System.out.println("ReactGuiPlugin initialized!");
    }

    @Override
    public String getPluginId() {
        return "REACTGUI";
    }

    @Override
    public AmiWebGuiServiceAdapter createGuiIntegrationAdapter(AmiWebService service) {
        System.out.println("Creating ReactGuiAdapter for user session");
        return new ReactGuiAdapter();
    }
}

