package com.github.ydewolf.classes.utils.config;

import com.github.ydewolf.enums.DebugTypes;

public class ManagerConfig extends BaseManagerConfig {
    public ManagerConfig() {
        super();
        
        this.debug_options.put(DebugTypes.DEBUG_MENU, true);
        this.debug_options.put(DebugTypes.DEBUG_PANELS, false);
    }
}
