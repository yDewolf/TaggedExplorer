package com.github.ydewolf.classes.utils.config.configs;

import java.io.File;

import com.github.ydewolf.classes.utils.config.abstract_classes.Configuration;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigRootFolder extends Configuration {
    public ConfigRootFolder() {
        super(ManagerConfigKeys.RootFolder, new File(System.getProperty("user.home") + "\\Pictures"));
        this.capitalized_name = "Root Folder";
    }

    public ConfigRootFolder(File value) {
        super(ManagerConfigKeys.RootFolder, value);
        this.capitalized_name = "Root Folder";
    }

    @Override
    public void setValue(Object new_value) {
        this.value = (File) new_value;
    }

    @Override
    public File getValue() {
        return (File) this.value;
    }
}
