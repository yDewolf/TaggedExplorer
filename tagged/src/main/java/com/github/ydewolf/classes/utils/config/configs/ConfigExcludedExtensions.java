package com.github.ydewolf.classes.utils.config.configs;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseArrayConfiguration;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigExcludedExtensions extends BaseArrayConfiguration {
    public ConfigExcludedExtensions() {
        super(ManagerConfigKeys.ExcludedExtensions, new String[0]);
    }

    public ConfigExcludedExtensions(String[] excluded_extensions) {
        super(ManagerConfigKeys.ExcludedExtensions, excluded_extensions);
    }

    public void fromString(String string, String separator) {
        this.setValue(string.split(separator));
    }

    public void setValue(String[] new_value) {
        this.value = new_value;
    }

    @Override
    public String[] getValue() {
        return (String[]) this.value;
    }
}