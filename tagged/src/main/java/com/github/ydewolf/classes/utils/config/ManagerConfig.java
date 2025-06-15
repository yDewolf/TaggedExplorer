package com.github.ydewolf.classes.utils.config;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseManagerConfig;
import com.github.ydewolf.classes.utils.config.configs.ConfigDebugSettings;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ManagerConfig extends BaseManagerConfig {
    public ManagerConfig() {
        super();
        
        this.setDebug(DebugTypes.DEBUG_MENU, true);
        this.setDebug(DebugTypes.DEBUG_PANELS, false);
        this.setDebug(DebugTypes.DEBUG_FILE_MANAGER, true);
    }

    public boolean getDebug(DebugTypes type) {
        ConfigDebugSettings debug_settings = (ConfigDebugSettings) this.getConfig(ManagerConfigKeys.DebugSettings);
        return debug_settings.getValue(type);
    }

    public void setDebug(DebugTypes type, boolean value) {
        ConfigDebugSettings debug_settings = (ConfigDebugSettings) this.getConfig(ManagerConfigKeys.DebugSettings);
        // this.changed = true;
        debug_settings.setValue(type, value);
    }
}
