package com.github.ydewolf.classes.utils.config.configs;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseArrayConfiguration;
import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.static_classes.FileFormats;

public class ConfigValidExtensions extends BaseArrayConfiguration {
    public ConfigValidExtensions() {
        super(ManagerConfigKeys.ValidExtensions, FileFormats.IMAGE_LIKE_EXTENSIONS);
        this.capitalized_name = "Valid Extensions";
    }

    public ConfigValidExtensions(String[] valid_extensions) {
        super(ManagerConfigKeys.ValidExtensions, valid_extensions);
        this.capitalized_name = "Valid Extensions";
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