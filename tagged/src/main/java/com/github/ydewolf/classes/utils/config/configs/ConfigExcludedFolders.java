package com.github.ydewolf.classes.utils.config.configs;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseArrayConfiguration;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigExcludedFolders extends BaseArrayConfiguration {
    public ConfigExcludedFolders() {
        super(ManagerConfigKeys.ExcludedFolders, new String[0]);
        this.capitalized_name = "Excluded Folders";
    }

    public ConfigExcludedFolders(String[] excluded_folders) {
        super(ManagerConfigKeys.ExcludedFolders, excluded_folders);
        this.capitalized_name = "Excluded Folders";
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