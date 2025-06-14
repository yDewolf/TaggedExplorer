package com.github.ydewolf.classes.utils.config;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseManagerConfig;
import com.github.ydewolf.enums.DebugTypes;

public class ManagerConfig extends BaseManagerConfig {
    public ManagerConfig() {
        super();
        
        this.setDebug(DebugTypes.DEBUG_MENU, true);
        this.setDebug(DebugTypes.DEBUG_PANELS, false);
        this.setDebug(DebugTypes.DEBUG_FILE_MANAGER, true);
    }
}
