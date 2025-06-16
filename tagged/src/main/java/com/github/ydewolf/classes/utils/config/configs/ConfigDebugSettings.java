package com.github.ydewolf.classes.utils.config.configs;
import java.util.HashMap;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseEnumConfiguration;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigDebugSettings extends BaseEnumConfiguration {
    // protected HashMap<DebugTypes, Boolean> value = new HashMap<>();

    public ConfigDebugSettings() {
        super(ManagerConfigKeys.DebugSettings, new HashMap<>());
        this.base_enum = DebugTypes.class;
        this.capitalized_name = "Debug Settings";
        
        for (DebugTypes type : DebugTypes.values()) {
            this.values.put(type, false);
        }

        this.values.put(DebugTypes.DEBUG_MENU, true);
        this.values.put(DebugTypes.DEBUG_PANELS, false);
        this.values.put(DebugTypes.DEBUG_FILE_MANAGER, true);
    }
}
