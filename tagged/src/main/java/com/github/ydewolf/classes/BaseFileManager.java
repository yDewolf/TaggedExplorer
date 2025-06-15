package com.github.ydewolf.classes;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.github.ydewolf.classes.Files.DirRef;
import com.github.ydewolf.classes.Files.FileRef;
import com.github.ydewolf.classes.Files.utils.FileUtils;
import com.github.ydewolf.classes.utils.config.abstract_classes.BaseManagerConfig;
import com.github.ydewolf.enums.ManagerConfigKeys;

public abstract class BaseFileManager {
    protected String[] VALID_EXTENSIONS = {};
    protected String[] EXCLUDED_EXTENSIONS = {};
    protected String[] EXCLUDED_FOLDERS = {".git", ".vscode"};

    protected long last_updated = 0;
    protected boolean never_looked_through = true;

    protected HashMap<String, FileRef> child_files = new HashMap<>();
    protected HashMap<String, DirRef> child_folders = new HashMap<>();
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

    public void loadConfig(BaseManagerConfig config) {
        this.VALID_EXTENSIONS = (String[]) config.getConfigValue(ManagerConfigKeys.ValidExtensions);
        this.EXCLUDED_EXTENSIONS = (String[]) config.getConfigValue(ManagerConfigKeys.ExcludedExtensions);
        this.EXCLUDED_FOLDERS = (String[]) config.getConfigValue(ManagerConfigKeys.ExcludedFolders);

        this.root_folder = (File) config.getConfigValue(ManagerConfigKeys.RootFolder);
        this.last_updated = System.currentTimeMillis();
        this.never_looked_through = true;
    }


    protected void updateChildFiles() {
        this.child_folders.clear();

        // I think it is optimized now
        File[] filtered_files = this.root_folder.listFiles(new FileFilter() {
            int file_count = 0;
            @Override
            public boolean accept(File pathname) {
                try {
                    if (file_count >= 10) {
                        Thread.sleep(0, 1);
                    }

                    file_count++;

                    return validateFile(pathname);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        });

        if (filtered_files == null) {
            System.err.println("Folder files is null | Caused by an I/O Error (probably) | Path: " + this.root_folder.getAbsolutePath());
            last_updated = System.currentTimeMillis();
            this.never_looked_through = false;
            return;
        }

        if (filtered_files.length == 0) {
            this.child_files.clear();
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

        // Files that should be added to the child files
        try {
            int file_count = 0;
            for (File file : filtered_files) {
                if (file_count >= 10) {
                    file_count = 0;
                    Thread.sleep(0, 1);
                }
                try {
                    String file_path = file.getCanonicalPath();
                    this.child_files.put(file_path, BaseFileManager.createRefFromFile(file));
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }

                file_count++;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        last_updated = System.currentTimeMillis();
        this.never_looked_through = false;
    }
    
    protected void updateChildFilesRecursive() {
        this.child_folders.clear();
        try {
            Thread.sleep(0, 5);

            ArrayList<FileRef> all_files = this.getChildrenRecursive(root_folder);
            
            Thread.sleep(0, 5);
            // Remove files that shouldn't be there anymore
            ArrayList<String> paths_to_remove = new ArrayList<>();
            for (FileRef file : this.child_files.values()) {
                if (!file.getPath().contains(this.root_folder.getAbsolutePath()) || !file.getInstance().exists()) {
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

            last_updated = System.currentTimeMillis();
            this.never_looked_through = false;
        } catch (InterruptedException e) {
            System.out.println("Exiting update files thread");
            Thread.currentThread().interrupt();

        }
    }

    protected ArrayList<FileRef> getChildrenRecursive(File parent_folder) {
        ArrayList<File> folders_to_look = new ArrayList<>();
        File[] folder_files = parent_folder.listFiles(new FileFilter() {
            int folder_count = 0;
            @Override
            public boolean accept(File pathname) {
                try {
                    // Add directories so they can be also checked
                    if (pathname.isDirectory()) {
                        if (folder_count >= 20) {
                            Thread.sleep(0, 1);
                        }
                        folder_count++;
                        // Skip this folder if it is not valid
                        if (!FileUtils.checkFileExtension(pathname.getPath(), new String[0], EXCLUDED_FOLDERS)) {
                            System.out.println("Skipped folder " + pathname.getAbsolutePath() + " | Reason: Excluded Folder");
                            return false;
                        }
                        long last_modified = pathname.lastModified();
                        if (last_modified > last_updated || never_looked_through) {
                            folders_to_look.add(pathname);
                            return false;
                        }
    
                        System.out.println("Skipped folder " + pathname.getAbsolutePath() + " | Reason: Last Modified is greater than last updated");
    
                        return false;
                    }
                    
                    return validateFile(pathname);
                    
                } catch (InterruptedException e) {
                    // System.out.println("WARNING: Overriding update file thread!!");
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        });

        // Recursion
        ArrayList<FileRef> files = new ArrayList<>();
        try {
            int folder_count = 0;
            for (File directory : folders_to_look) {
                if (folder_count >= 10) {
                    folder_count = 0;
                    Thread.sleep(0, 1);
                }
                folder_count++;
                this.child_folders.put(directory.getAbsolutePath(), (DirRef) BaseFileManager.createRefFromFile(directory));
    
                ArrayList<FileRef> files_to_add = this.getChildrenRecursive(directory);
                for (FileRef fileRef : files_to_add) {
                    files.add(fileRef);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (folder_files == null) {
            System.err.println("Folder files is null | Caused by an I/O Error (probably) | Path: " + parent_folder.getAbsolutePath());
            return files;
        }

        for (File file : folder_files) {
            FileRef ref = BaseFileManager.createRefFromFile(file);
            files.add(ref);
        }

        return files;
    }

    protected boolean validateFile(File file) {
        // Skip files that are already in the child_files map
        if (this.child_files.containsKey(file.getAbsolutePath())) {
            return false;
        }
        
        return FileUtils.checkFileExtension(file.getName(), this.VALID_EXTENSIONS, this.EXCLUDED_EXTENSIONS);

    }

    public HashMap<String, FileRef> getFiles() {
        return this.child_files;
    }

    public File getFileInstance(String file_path) {
        return this.child_files.get(file_path).getInstance();
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
