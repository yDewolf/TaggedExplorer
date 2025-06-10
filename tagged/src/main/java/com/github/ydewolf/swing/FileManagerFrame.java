package com.github.ydewolf.swing;

import javax.swing.JFrame;

import com.github.ydewolf.classes.FileManager;
import com.github.ydewolf.swing.utils.ManagerConfig;

public class FileManagerFrame extends JFrame {
    protected FileManager file_manager;
    protected ManagerConfig config;

    protected int SIZE_X = 800;
    protected int SIZE_Y = 600;

    public FileManagerFrame() {
        this.setup();
    }

    protected void setup() {
        this.setupFileManager();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Sizing:
        this.setSize(SIZE_X, SIZE_Y);
    }

    public void init() {
        this.setVisible(true);
    }

    private void setupFileManager() {
        this.config = new ManagerConfig();

        this.file_manager = new FileManager();
        this.file_manager.loadConfig(config);
    }

    public void updateFileManagerConfigs() {
        this.file_manager.loadConfig(config);
    }

    public FileManager getFileManager() {
        return this.file_manager;
    }

    public ManagerConfig getConfigs() {
        return this.config;
    }
}
