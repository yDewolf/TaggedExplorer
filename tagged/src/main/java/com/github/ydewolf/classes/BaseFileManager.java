package com.github.ydewolf.classes;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.github.ydewolf.classes.Files.DirRef;
import com.github.ydewolf.classes.Files.FileRef;
import com.github.ydewolf.swing.utils.ManagerConfig;

public abstract class BaseFileManager {
    protected String[] VALID_EXTENSIONS = {};
    protected String[] EXCLUDED_EXTENSIONS = {};
    protected String[] EXCLUDED_FOLDERS = {".git", ".vscode"};

    protected HashMap<String, FileRef> child_files = new HashMap<>();
    protected File root_folder;

    public BaseFileManager(String root_folder_path)  {
        this.root_folder = new File(root_folder_path);
        if (!this.root_folder.exists() || !this.root_folder.isDirectory()) {
            System.err.println("The root folder path should point to a directory");
            return;
        }
    }

    public BaseFileManager() {
        this(".");
    }

    public void loadConfig(ManagerConfig config) {
        this.VALID_EXTENSIONS = config.VALID_EXTENSIONS;
        this.EXCLUDED_EXTENSIONS = config.EXCLUDED_EXTENSIONS;
        this.EXCLUDED_FOLDERS = config.EXCLUDED_FOLDERS;

        this.root_folder = config.root_folder;
    }


    protected void updateChildFiles() {
        // I think it is optimized now
        File[] filtered_files = this.root_folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return validateFile(pathname);
            }
        });

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

        // Files that should be added to the child files
        for (File file : filtered_files) {
            try {
                String file_path = file.getCanonicalPath();
                this.child_files.put(file_path, BaseFileManager.createRefFromFile(file));
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    

    protected void updateChildFilesRecursive() {
        ArrayList<FileRef> all_files = this.getChildrenRecursive(root_folder);

        // Remove files that shouldn't be there anymore
        ArrayList<String> paths_to_remove = new ArrayList<>();
        for (FileRef file : this.child_files.values()) {
            if (!file.getInstance().exists()) {
                paths_to_remove.add(file.getPath());
            }
        }

        // Files that should be added to the child files
        for (FileRef file : all_files) {
            try {
                String file_path = file.getInstance().getCanonicalPath();
                this.child_files.put(file_path, file);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        for (String path : paths_to_remove) {
            this.child_files.remove(path);
        }
    }

    protected ArrayList<FileRef> getChildrenRecursive(File parent_folder) {
        ArrayList<File> folders_to_look = new ArrayList<>();
        File[] folder_files = parent_folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                // Add directories so they can be also checked
                if (pathname.isDirectory()) {
                    // Skip this folder if it is not valid
                    if (!validateFolder(pathname)) {
                        return false;
                    }

                    folders_to_look.add(pathname);
                    return false;
                }
                
                return validateFile(pathname);
            }
        });

        // Recursion
        ArrayList<FileRef> files = new ArrayList<>();
        for (File directory : folders_to_look) {
            ArrayList<FileRef> files_to_add = this.getChildrenRecursive(directory);
            for (FileRef fileRef : files_to_add) {
                files.add(fileRef);
            }
        }

        for (File file : folder_files) {
            FileRef ref = BaseFileManager.createRefFromFile(file);
            files.add(ref);
        }

        return files;
    }

    protected boolean validateFile(File file) {
        if (VALID_EXTENSIONS.length == 0 && EXCLUDED_EXTENSIONS.length == 0) {
            return true;
        }

        // Skip files that are already in the child_files map
        if (this.child_files.containsKey(file.getAbsolutePath())) {
            return false;
        }

        for (String extension : EXCLUDED_EXTENSIONS) {
            if (file.getAbsolutePath().endsWith(extension)) {
                return false;
            }
        }

        if (VALID_EXTENSIONS.length == 0) {
            return true;
        }

        for (String extension : VALID_EXTENSIONS) {
            if (file.getAbsolutePath().endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    protected boolean validateFolder(File folder) {
        for (String excluded_folder : EXCLUDED_FOLDERS) {
            if (folder.getPath().endsWith(excluded_folder)) {
                return false;
            }
        }

        return true;
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
