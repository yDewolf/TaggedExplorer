package com.github.ydewolf.classes.utils.config.abstract_classes;

import java.util.HashMap;

import com.github.ydewolf.enums.ManagerConfigKeys;

public abstract class BaseEnumConfiguration extends Configuration {
    protected HashMap<Enum<?>, Boolean> value = new HashMap<>();
    protected Class<?> base_enum;

    public BaseEnumConfiguration(ManagerConfigKeys tag, Object default_value) {
        super(tag, default_value);
    }
    
    public void setValue(Enum<?> key, Boolean value) {
        this.value.put(key, value);
    }

    public Boolean getValue(Enum<?> key) {
        return this.value.get(key);
    }

    public HashMap<Enum<?>, Boolean> getValues() {
        return this.value;
    }

    public Class<?> getEnumBase() {
        return this.base_enum;
    }
}
