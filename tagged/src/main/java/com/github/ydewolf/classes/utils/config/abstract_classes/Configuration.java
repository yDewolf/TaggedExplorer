package com.github.ydewolf.classes.utils.config.abstract_classes;

import com.github.ydewolf.classes.utils.config.interfaces.Configurable;
import com.github.ydewolf.enums.ManagerConfigKeys;

public abstract class Configuration implements Configurable {
    protected ManagerConfigKeys tag;
    protected Object value;

    public Configuration(ManagerConfigKeys tag, Object default_value) {
        this.tag = tag;
        this.setValue(default_value);
    }

    @Override
    public void setValue(Object new_value) {
        this.value = new_value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }
    
}
