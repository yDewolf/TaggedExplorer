package com.github.ydewolf.classes.utils.config.abstract_classes;

import com.github.ydewolf.enums.ManagerConfigKeys;

public abstract class BaseSelectEnumConfiguration extends Configuration {

    public BaseSelectEnumConfiguration(ManagerConfigKeys tag, Object default_value) {
        super(tag, default_value);
    }
    
    public void setValue(Enum<?> key) {
        this.value = key;
    }

    public Enum<?> getValue() {
        return (Enum<?>) this.value;
    }
}
