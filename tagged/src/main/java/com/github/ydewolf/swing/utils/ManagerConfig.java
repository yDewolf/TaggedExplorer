package com.github.ydewolf.swing.utils;

import java.io.File;


public class ManagerConfig {
    protected File root_folder = new File(System.getProperty("user.home") + "\\Pictures");
    public boolean recursive = true;
    public boolean debug_mode = false;
    public boolean debug_elapsed = true;
    protected boolean changed_root = false;

    public String[] VALID_EXTENSIONS = {".jpeg", ".jpg", ".png", ".gif"};
    public String[] EXCLUDED_EXTENSIONS = {};
    public String[] EXCLUDED_FOLDERS = {".git", ".vscode"};

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
