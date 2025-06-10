package com.github.ydewolf.classes;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.github.ydewolf.classes.Files.DirRef;
import com.github.ydewolf.classes.Files.FileRef;

public class FileManager {
    private static String[] VALID_EXTENSIONS = {};

    protected HashMap<String, FileRef> child_files = new HashMap<>();
    protected File root_folder;

    public FileManager(String root_folder_path)  {
        this.root_folder = new File(root_folder_path);
        if (!this.root_folder.exists() || !this.root_folder.isDirectory()) {
            System.err.println("The root folder path should point to a directory");
            return;
        }

        this.updateChildFiles();
    }

    public FileManager() {
        this(".");
    }

    public void updateChildFiles() {
        // I think it is optimized now
        File[] filtered_files = this.root_folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (VALID_EXTENSIONS.length == 0) {
                    return true;
                }

                // Skip files that are already in the child_files map
                if (child_files.containsKey(pathname.getAbsolutePath())) {
                    return false;
                }

                for (String extension : VALID_EXTENSIONS) {
                    if (pathname.getAbsolutePath().endsWith(extension)) {

                        return true;
                    }
                }

                return false;
            }
        });

        // Files that should be added to the child files
        for (File file : filtered_files) {
            try {
                String file_path = file.getCanonicalPath();
                this.child_files.put(file_path, FileManager.createRefFromFile(file));
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        // Remove files that shouldn't be there anymore
        ArrayList<String> paths_to_remove = new ArrayList<>();
        for (FileRef file : this.child_files.values()) {
            if (!file.getInstance().exists()) {
                paths_to_remove.add(file.getPath());
            }
        }

        for (String path : paths_to_remove) {
            this.child_files.remove(path);
        }
    }
    
    public HashMap<String, FileRef> getFiles() {
        return this.child_files;
    }

    public Set<String> getFilePaths() {
        return this.child_files.keySet();
    }

    public static FileRef createRefFromFile(File file) {
        if (file.isDirectory()) {
            return DirRef.fromFile(file);
        }
        // If not a directory
        return FileRef.fromFile(file);
    }
}
