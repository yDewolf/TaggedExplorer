package com.github.ydewolf.classes.utils.config.abstract_classes;

import com.github.ydewolf.enums.ManagerConfigKeys;

public abstract class BaseSelectEnumConfiguration extends Configuration {

    public BaseSelectEnumConfiguration(ManagerConfigKeys tag, Object default_value) {
        super(tag, default_value);
    }
    
    @Override
    public void setValue(Object key) {
        this.value = (Enum<?>) key;
    }

    public Enum<?> getValue() {
        return (Enum<?>) this.value;
    }
}
