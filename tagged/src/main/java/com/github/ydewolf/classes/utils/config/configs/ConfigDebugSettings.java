package com.github.ydewolf.classes.utils.config.configs;
import java.util.HashMap;

import com.github.ydewolf.classes.utils.config.abstract_classes.Configuration;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigDebugSettings extends Configuration {
    protected HashMap<DebugTypes, Boolean> value = new HashMap<>();

    public ConfigDebugSettings() {
        super(ManagerConfigKeys.DebugSettings, new HashMap<>());
        this.capitalized_name = "Debug Settings";
        
        for (DebugTypes type : DebugTypes.values()) {
            this.value.put(type, false);
        }

        this.value.put(DebugTypes.DEBUG_MENU, true);
        this.value.put(DebugTypes.DEBUG_PANELS, false);
        this.value.put(DebugTypes.DEBUG_FILE_MANAGER, true);
    }

    public void setValue(DebugTypes key, Boolean value) {
        this.value.put(key, value);
    }

    public Boolean getValue(DebugTypes key) {
        return this.value.get(key);
    }
}
