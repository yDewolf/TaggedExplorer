package com.github.ydewolf.classes.utils.config.abstract_classes;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import com.github.ydewolf.enums.ChildFilesMethods;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.static_classes.FileFormats;

public abstract class BaseManagerConfig {
    protected HashMap<ManagerConfigKeys, Object> configurations = new HashMap<>();

    protected File root_folder = new File(System.getProperty("user.home") + "\\Pictures");
    public ChildFilesMethods child_search_method = ChildFilesMethods.RECURSIVE;

    public HashMap<DebugTypes, Boolean> debug_options = new HashMap<>();
    
    protected boolean changed_root = false;

    public String[] VALID_EXTENSIONS = FileFormats.IMAGE_LIKE_EXTENSIONS;
    public String[] EXCLUDED_EXTENSIONS = {};
    public String[] EXCLUDED_FOLDERS = {".git", ".vscode"};

    public BaseManagerConfig() {
        for (ManagerConfigKeys config_name: ManagerConfigKeys.values()) {
            try {
                this.configurations.put(config_name, Class.forName("com.github.ydewolf.classes.utils.config.configs." + "Config" + config_name));
            } catch (ClassNotFoundException e) {
                System.err.println("No class found for config " + config_name + " | Try creating it");;
            }
        }
        
        for (DebugTypes type : DebugTypes.values()) {
            debug_options.put(type, false);
        }
    }

    public Set<ManagerConfigKeys> getConfigs() {
        return this.configurations.keySet();
    }

    public Object getConfig(ManagerConfigKeys config_key) {
        return this.configurations.get(config_key);
    }

    public void setConfig(ManagerConfigKeys config_key, Object value) {
        this.configurations.put(config_key, value);
    }




    public boolean getDebug(DebugTypes type) {
        return this.debug_options.get(type);
    }

    public void setDebug(DebugTypes type, boolean value) {
        this.debug_options.put(type, value);
    }

    public void setRoot(File new_root) {
        this.changed_root = root_folder.getAbsolutePath() != new_root.getAbsolutePath();
        this.root_folder = new_root;
    }

    public File getRoot() {
        return this.root_folder;
    }

    public boolean getChangedRoot() {
        return this.changed_root;
    }
}
