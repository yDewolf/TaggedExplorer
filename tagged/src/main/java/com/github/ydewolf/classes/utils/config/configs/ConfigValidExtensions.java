package com.github.ydewolf.classes.utils.config.configs;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseArrayConfiguration;
import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.static_classes.FileFormats;

public class ConfigValidExtensions extends BaseArrayConfiguration {
    public ConfigValidExtensions() {
        super(ManagerConfigKeys.ValidExtensions, FileFormats.IMAGE_LIKE_EXTENSIONS);
    }

    public ConfigValidExtensions(String[] valid_extensions) {
        super(ManagerConfigKeys.ValidExtensions, valid_extensions);
    }

    public void fromString(String string, String separator) {
        this.setValue(string.split(separator));
    }
}