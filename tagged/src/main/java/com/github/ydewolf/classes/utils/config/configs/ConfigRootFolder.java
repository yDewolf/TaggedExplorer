package com.github.ydewolf.classes.utils.config.configs;

import java.io.File;

import com.github.ydewolf.classes.utils.config.abstract_classes.Configuration;
import com.github.ydewolf.enums.ManagerConfigKeys;

public class ConfigRootFolder extends Configuration {
    public ConfigRootFolder(ManagerConfigKeys tag, File default_value) {
        super(tag, default_value);
    }   
}
