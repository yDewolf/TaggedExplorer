package com.github.ydewolf.classes.utils.config.configs;

import com.github.ydewolf.classes.utils.config.abstract_classes.Configuration;
import com.github.ydewolf.enums.ChildFilesMethods;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigFileSearchMethod extends Configuration {
    public ConfigFileSearchMethod() {
        super(ManagerConfigKeys.FileSearchMethod, ChildFilesMethods.RECURSIVE);
    }
    
    public ConfigFileSearchMethod(ChildFilesMethods value) {
        super(ManagerConfigKeys.FileSearchMethod, value);
    }

    public void setValue(ChildFilesMethods new_value) {
        this.value = new_value;
    }

    @Override
    public ChildFilesMethods getValue() {
        return (ChildFilesMethods) this.value;
    }
}
