package com.github.ydewolf.classes.utils.config.configs;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseArrayConfiguration;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigExcludedFolders extends BaseArrayConfiguration {
    public ConfigExcludedFolders() {
        super(ManagerConfigKeys.ExcludedFolders, new String[0]);
    }

    public ConfigExcludedFolders(String[] excluded_folders) {
        super(ManagerConfigKeys.ExcludedFolders, excluded_folders);
    }

    public void fromString(String string, String separator) {
        this.setValue(string.split(separator));
    }
}