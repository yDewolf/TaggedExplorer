package com.github.ydewolf.classes.utils.config.abstract_classes;

import com.github.ydewolf.enums.ManagerConfigKeys;

public class BaseArrayConfiguration extends Configuration {
    public BaseArrayConfiguration(ManagerConfigKeys tag, Object default_value) {
        super(tag, default_value);
    }

    public void setValue(Object[] new_value) {
        this.value = new_value;
    }

    @Override
    public Object[] getValue() {
        return (Object[]) this.value;
    }
}