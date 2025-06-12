package com.github.ydewolf.classes;

import com.github.ydewolf.classes.utils.config.BaseManagerConfig;
import com.github.ydewolf.enums.ChildFilesMethods;
import com.github.ydewolf.enums.DebugTypes;

public class FileManager extends BaseFileManager {
    protected boolean debug_mode = true;
    protected boolean debug_paths = false;
    protected ChildFilesMethods child_search_method = ChildFilesMethods.RECURSIVE;

    public FileManager(String root_folder_path)  {
        super(root_folder_path);

        this.defaultUpdateChildren();
    }

    public FileManager() {
        super();
    }

    public void loadConfig(BaseManagerConfig config) {
        super.loadConfig(config);

        this.child_search_method = config.child_search_method;
        this.debug_mode = config.getDebug(DebugTypes.DEBUG_FILE_MANAGER);
        this.debug_paths = config.getDebug(DebugTypes.DEBUG_FILE_MANAGER_PATHS);
    }

    public void defaultUpdateChildren() {
        long start_time = System.currentTimeMillis();

        if (debug_mode) {
            System.out.println("Started searching files | Root folder: " + this.root_folder.getAbsolutePath());
        }

        switch (this.child_search_method) {
            case ChildFilesMethods.RECURSIVE:
                this.updateChildFilesRecursive();
                break;
        
            default:
                this.updateChildFiles();
                break;
        }

        if (debug_paths) {
            String paths = "";
            for (String path : this.getFilePaths()) {
                paths += path + "\n";
            }
            System.out.println(paths);
        }

        if (debug_mode) {
            System.out.println(
                "Found " + this.getFiles().size() + " files | Subfolders: " + this.child_folders.size() + " | Method: " + this.child_search_method +  " | Elapsed time: " + (System.currentTimeMillis() - start_time) + "ms"
            );
        }
    }
}
