package com.github.ydewolf.classes.utils.config.configs;

import java.io.File;

import com.github.ydewolf.classes.utils.config.abstract_classes.Configuration;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigRootFolder extends Configuration {
    public ConfigRootFolder() {
        super(ManagerConfigKeys.RootFolder, new File(System.getProperty("user.home") + "\\Pictures"));
    }

    public ConfigRootFolder(File value) {
        super(ManagerConfigKeys.RootFolder, value);
    }
}
