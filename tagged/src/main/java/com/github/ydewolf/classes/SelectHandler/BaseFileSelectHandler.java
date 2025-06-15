package com.github.ydewolf.classes.SelectHandler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import com.github.ydewolf.classes.FileManager;
import com.github.ydewolf.classes.Files.FileRef;
import com.github.ydewolf.classes.Files.utils.FileUtils;
import com.github.ydewolf.static_classes.FileFormats;

public abstract class BaseFileSelectHandler {
    protected FileManager file_manager;
    
    protected FileRef selected_file = null;

    public BaseFileSelectHandler(FileManager manager) {
        this.file_manager = manager;
    }

    public void selectFile(String abs_path) {
        HashMap<String, FileRef> file_list = this.file_manager.getFiles();
        if (!file_list.containsKey(abs_path)) {
            System.err.println("Couldn't select file because manager doesn't contain the file | Path: " + abs_path);
            return;
        }

        FileRef ref = file_list.get(abs_path);
        this.selected_file = ref;
    }

    public FileRef getSelectedFile() {
        return this.selected_file;
    }

    public Object getFileContents(int target_width, int target_height) {
        if (this.selected_file.equals(null)) {
            System.err.println("Trying to get the contents from selected file but selected file is null");
            return null;
        }

        // Parse file as an Image
        if (FileUtils.checkFileExtension(this.selected_file.getPath(), FileFormats.IMAGE_LIKE_EXTENSIONS)) {
            return this.getSelectedFileAsImage(target_width, target_height);
        }

        // Do something else to the file
        System.err.println("File format not supported | Extension: " + FileUtils.getFileExtension(this.selected_file.getInstance()));
        return null;
    }

    public BufferedImage getSelectedFileAsImage(int target_width, int target_height) {
        if (!FileUtils.checkFileExtension(this.selected_file.getPath(), FileFormats.IMAGE_LIKE_EXTENSIONS)) {
            System.err.println("Wrong file format | Couldn't parse as Image like | File Path: " + this.selected_file.getPath());
            return null;
        }

        try {
            if (target_width == 0 && target_height == 0) {
                return FileUtils.readImageFile(this.selected_file.getInstance());
            }

            return FileUtils.readImageFileCropped(this.selected_file.getInstance(), target_width, target_height);

        } catch (IOException e) {
            System.err.println("Couldn't Parse image | Probably some IOException | Path: " + this.selected_file.getPath());
            e.printStackTrace();
            return null;
        }
    }
}
