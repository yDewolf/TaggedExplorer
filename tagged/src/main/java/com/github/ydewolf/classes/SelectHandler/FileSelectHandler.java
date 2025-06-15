package com.github.ydewolf.classes.SelectHandler;

import java.awt.image.BufferedImage;

import com.github.ydewolf.classes.FileManager;

public class FileSelectHandler extends BaseFileSelectHandler {
    protected int default_width = 64;
    protected int default_height = 64;

    public FileSelectHandler(FileManager manager) {
        super(manager);
    }

    public FileSelectHandler(FileManager manager, int target_height, int target_width) {
        super(manager);
        this.default_height = target_height;
        this.default_width = target_width;
    }
    
    public Object getFileContents() {
        return super.getFileContents(default_width, default_height);
    }

    public BufferedImage getSelectedFileAsImage() {
        return super.getSelectedFileAsImage(default_width, default_height);
    }
}
