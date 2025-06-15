package com.github.ydewolf.classes.utils.config.abstract_classes;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

import com.github.ydewolf.enums.ManagerConfigKeys;

public abstract class BaseManagerConfig {
    protected HashMap<ManagerConfigKeys, Object> configurations = new HashMap<>();
    protected HashMap<ManagerConfigKeys, Boolean> changed_configs = new HashMap<>();
    // protected boolean changed = false;

    public BaseManagerConfig() {
        for (ManagerConfigKeys config_name: ManagerConfigKeys.values()) {
            try {
                Class<?> clazz = Class.forName("com.github.ydewolf.classes.utils.config.configs." + "Config" + config_name);
                Object instance = clazz.getDeclaredConstructor().newInstance();

                this.configurations.put(config_name, instance);
                this.changed_configs.put(config_name, false);

            } catch (ClassNotFoundException e) {
                System.err.println("No class found for config " + config_name + " | Try creating it");

            } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
                System.err.println("Erro ao instanciar a classe: " + e.getMessage());
            }
        }
    }

    public Set<ManagerConfigKeys> getConfigs() {
        return this.configurations.keySet();
    }

    public Configuration getConfig(ManagerConfigKeys config_key) {
        return (Configuration) this.configurations.get(config_key);
    }

    public Object getConfigValue(ManagerConfigKeys config_key) {
        return ((Configuration) this.configurations.get(config_key)).getValue();
    }

    public void setConfig(ManagerConfigKeys config_key, Object value) {
        this.getConfig(config_key).setValue(value);
    }

    public boolean getChanged(ManagerConfigKeys key) {
        return ((Configuration) this.configurations.get(key)).getChanged();
    }
}
