package com.github.ydewolf.classes;

import java.io.File;
import java.io.FileFilter;

import com.github.ydewolf.classes.Files.DirRef;
import com.github.ydewolf.classes.Files.FileRef;

public class FileManager {
    private static String[] VALID_EXTENSIONS = {".jpeg", ".jpg", ".png", ".gif"};

    protected FileRef[] child_files;
    protected File root_folder;

    public FileManager(String root_folder_path)  {
        this.root_folder = new File(root_folder_path);
        if (!this.root_folder.exists() ||!this.root_folder.isDirectory()) {
            System.err.println("The root folder path should point to a directory");
            return;
        }

        this.updateChildFiles();
    }

    public FileManager() {
        this(".");
    }

    public void updateChildFiles() {
        // TODO: Optimize this using cache and linear search to not instantiate files that shouldn't be removed from the list at all
        File[] filtered_files = this.root_folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (VALID_EXTENSIONS.length == 0) {
                    return true;
                }

                for (String extension : VALID_EXTENSIONS) {
                    if (pathname.getAbsolutePath().endsWith(extension)) {
                        return true;
                    }
                }

                return false;
            }
        });

        // Re generate the child files variable from the filtered files
        FileRef[] new_buffer = new FileRef[filtered_files.length];
        for (int idx = 0; idx < new_buffer.length; idx++) {
            File file = filtered_files[idx];
            if (file.isDirectory()) {
                new_buffer[idx] = DirRef.fromFile(file);
                continue;
            }
            // If not a directory
            new_buffer[idx] = FileRef.fromFile(file);
        }

        this.child_files = new_buffer;
    }
    
    public FileRef[] getFiles() {
        return this.child_files;
    }

    public String[] getFilePaths() {
        String[] file_paths = new String[this.child_files.length];
        for (int idx = 0; idx < file_paths.length; idx++) {
            file_paths[idx] = this.child_files[idx].getPath();
        }

        return file_paths;
    }
}
