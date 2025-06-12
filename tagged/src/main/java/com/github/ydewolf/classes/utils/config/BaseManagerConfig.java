package com.github.ydewolf.classes.utils.config;

import java.io.File;
import java.util.HashMap;

import com.github.ydewolf.enums.ChildFilesMethods;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.static_classes.FileFormats;

public abstract class BaseManagerConfig {
    protected File root_folder = new File(System.getProperty("user.home") + "\\Pictures");
    public ChildFilesMethods child_search_method = ChildFilesMethods.RECURSIVE;

    public HashMap<DebugTypes, Boolean> debug_options = new HashMap<>();
    
    protected boolean changed_root = false;

    public String[] VALID_EXTENSIONS = FileFormats.IMAGE_LIKE_EXTENSIONS;
    public String[] EXCLUDED_EXTENSIONS = {};
    public String[] EXCLUDED_FOLDERS = {".git", ".vscode"};

    public BaseManagerConfig() {
        for (DebugTypes type : DebugTypes.values()) {
            debug_options.put(type, false);
        }
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
