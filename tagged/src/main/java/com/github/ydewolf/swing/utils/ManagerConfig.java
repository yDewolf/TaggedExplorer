package com.github.ydewolf.swing.utils;

import java.io.File;


public class ManagerConfig {
    public File root_folder = new File(".");
    public boolean recursive = false;
    public boolean debug_mode = false;

    public String[] VALID_EXTENSIONS = {".jpeg", ".jpg", ".png", ".gif"};
    public String[] EXCLUDED_EXTENSIONS = {};
    public String[] EXCLUDED_FOLDERS = {".git", ".vscode"};
}
