package com.github.ydewolf.classes.utils.config.abstract_classes;

import com.github.ydewolf.enums.ManagerConfigKeys;

public abstract class BaseArrayConfiguration extends Configuration {
    public BaseArrayConfiguration(ManagerConfigKeys tag, Object default_value) {
        super(tag, default_value);
    }

    public void setValue(Object[] new_value) {
        this.value = new_value;
    }

    public void fromString(String string, String separator) {
        this.setValue(string.split(separator));
    }

    @Override
    public Object[] getValue() {
        return (Object[]) this.value;
    }
}