package com.github.ydewolf.classes.utils.config.abstract_classes;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

import com.github.ydewolf.classes.utils.config.configs.ConfigDebugSettings;
import com.github.ydewolf.classes.utils.config.configs.ConfigRootFolder;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.enums.ManagerConfigKeys;

public abstract class BaseManagerConfig {
    protected HashMap<ManagerConfigKeys, Object> configurations = new HashMap<>();

    // protected File root_folder = new File(System.getProperty("user.home") + "\\Pictures");
    // public ChildFilesMethods child_search_method = ChildFilesMethods.RECURSIVE;

    // public HashMap<DebugTypes, Boolean> debug_options = new HashMap<>();
    
    protected boolean changed_root = false;

    // public String[] VALID_EXTENSIONS = FileFormats.IMAGE_LIKE_EXTENSIONS;
    // public String[] EXCLUDED_EXTENSIONS = {};
    // public String[] EXCLUDED_FOLDERS = {".git", ".vscode"};

    public BaseManagerConfig() {
        for (ManagerConfigKeys config_name: ManagerConfigKeys.values()) {
            try {
                Class<?> clazz = Class.forName("com.github.ydewolf.classes.utils.config.configs." + "Config" + config_name);
                Object instance = clazz.getDeclaredConstructor().newInstance();

                this.configurations.put(config_name, instance);
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




    public boolean getDebug(DebugTypes type) {
        ConfigDebugSettings debug_settings = (ConfigDebugSettings) this.getConfig(ManagerConfigKeys.DebugSettings);
        return debug_settings.getValue(type);
    }

    public void setDebug(DebugTypes type, boolean value) {
        ConfigDebugSettings debug_settings = (ConfigDebugSettings) this.getConfig(ManagerConfigKeys.DebugSettings);
        debug_settings.setValue(type, value);
    }

    public void setRoot(File new_root) {
        ConfigRootFolder root_config = (ConfigRootFolder) this.getConfig(ManagerConfigKeys.RootFolder);
        this.changed_root = !root_config.getValue().getAbsolutePath().equals(new_root.getAbsolutePath());
        root_config.setValue(new_root);
    }

    public File getRoot() {
        ConfigRootFolder root_config = (ConfigRootFolder) this.getConfig(ManagerConfigKeys.RootFolder);
        return root_config.getValue();
    }

    public boolean getChangedRoot() {
        return this.changed_root;
    }
}
