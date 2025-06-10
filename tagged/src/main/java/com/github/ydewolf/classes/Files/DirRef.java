package com.github.ydewolf.classes.Files;

import java.io.File;

public class DirRef extends FileRef {
    protected DirRef(String file_path, File instance) {
        super(file_path, instance);
    }

    public static DirRef fromPath(String path) {
        File file_instance = new File(path);
        if (!file_instance.exists() || !file_instance.isDirectory()) {
            return null;
        }

        return new DirRef(path, file_instance);
    }

    public static FileRef fromFile(File file) {
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        
        return new DirRef(file.getAbsolutePath(), file);    
    }

    public File[] getFiles() {
        return this.file_instance.listFiles();
    }
}
