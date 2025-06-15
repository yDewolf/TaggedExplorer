package com.github.ydewolf.classes.Files;

import java.io.File;

public class FileRef {
    protected String path;
    protected File file_instance;

    // Protect the main constructor so it can check for errors previously
    protected FileRef(String file_path, File instance) {
        this.path = file_path;
        this.file_instance = instance;
    }

    public static FileRef fromPath(String path) {
        File file_instance = new File(path);
        if (!file_instance.exists() || file_instance.isDirectory()) {
            return null;
        }
        
        return new FileRef(path, file_instance);
    }

    public static FileRef fromFile(File file) {
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        
        return new FileRef(file.getAbsolutePath(), file);    
    }

    public String getFilename() {
        return this.getInstance().getName();
    }

    public String getPath() {
        return this.path;
    }

    public File getInstance() {
        return this.file_instance;
    }
}
