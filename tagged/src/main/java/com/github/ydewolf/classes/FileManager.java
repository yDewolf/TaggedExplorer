package com.github.ydewolf.classes;

import com.github.ydewolf.classes.utils.config.BaseManagerConfig;
import com.github.ydewolf.enums.DebugTypes;

public class FileManager extends BaseFileManager {
    protected boolean debug_mode = true;
    protected boolean recursive = false;

    public FileManager(String root_folder_path)  {
        super(root_folder_path);

        this.defaultUpdateChildren();
    }

    public FileManager() {
        super();
    }

    public void loadConfig(BaseManagerConfig config) {
        super.loadConfig(config);

        this.recursive = config.recursive;
        this.debug_mode = config.getDebug(DebugTypes.DEBUG_FILES);
    }

    public void defaultUpdateChildren() {
        if (recursive) {
            this.updateChildFilesRecursive();

        } else {
            this.updateChildFiles();
        }

        if (debug_mode) {
            for (String path : this.getFilePaths()) {
                System.out.println(path);
            }
        }
    }
}
