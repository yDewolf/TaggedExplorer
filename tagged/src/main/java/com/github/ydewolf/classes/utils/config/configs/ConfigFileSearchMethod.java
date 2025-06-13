package com.github.ydewolf.classes.utils.config.configs;

import com.github.ydewolf.classes.utils.config.abstract_classes.Configuration;
import com.github.ydewolf.enums.ChildFilesMethods;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigFileSearchMethod extends Configuration {
    public ConfigFileSearchMethod(ManagerConfigKeys tag, ChildFilesMethods default_value) {
        super(tag, default_value);
    }
    
}
