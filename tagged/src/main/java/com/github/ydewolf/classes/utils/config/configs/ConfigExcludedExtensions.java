package com.github.ydewolf.classes.utils.config.configs;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseArrayConfiguration;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigExcludedExtensions extends BaseArrayConfiguration {
    public ConfigExcludedExtensions() {
        super(ManagerConfigKeys.ExcludedExtensions, new String[0]);
        this.capitalized_name = "Excluded Extensions";
    }

    public ConfigExcludedExtensions(String[] excluded_extensions) {
        super(ManagerConfigKeys.ExcludedExtensions, excluded_extensions);
        this.capitalized_name = "Excluded Extensions";
    }

    @Override
    public void setValue(Object new_value) {
        this.value = (String[]) new_value;
    }

    @Override
    public String[] getValue() {
        return (String[]) this.value;
    }
}