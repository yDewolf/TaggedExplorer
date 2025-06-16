package com.github.ydewolf.classes.utils.config.abstract_classes;

import java.util.HashMap;

import com.github.ydewolf.enums.ManagerConfigKeys;

public abstract class BaseEnumConfiguration extends Configuration {
    protected HashMap<Enum<?>, Boolean> values = new HashMap<>();
    protected Class<?> base_enum;

    public BaseEnumConfiguration(ManagerConfigKeys tag, Object default_value) {
        super(tag, default_value);
    }
    
    @Override
    public void setValue(Object new_value) {
        this.values = (HashMap<Enum<?>, Boolean>) new_value;
    }

    public void setValue(Enum<?> key, Boolean value) {
        this.values.put(key, value);
    }

    public Boolean getValue(Enum<?> key) {
        return this.values.get(key);
    }

    public HashMap<Enum<?>, Boolean> getValues() {
        return this.values;
    }

    public Class<?> getEnumBase() {
        return this.base_enum;
    }
}
