package com.github.ydewolf.swing;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.ydewolf.classes.FileManager;
import com.github.ydewolf.swing.ui.FileExplorerPanel;
import com.github.ydewolf.swing.utils.JavaSwingUtils;
import com.github.ydewolf.swing.utils.ManagerConfig;

public class FileManagerFrame extends JFrame {
    protected FileManager file_manager;
    protected ManagerConfig config;

    protected final int SIZE_X = 800;
    protected final int SIZE_Y = 600;

    protected final int SIDE_PANEL_SIZE = 250;
    protected final int DEFAULT_BORDER_SIZE = 5;

    protected final int DEFAULT_BUTTON_HEIGHT = 20;

    protected FileExplorerPanel file_explorer_panel;

    public FileManagerFrame() {
        this.setup();
    }

    // UI Creation + initialization

    public void init() {
        this.add(createLeftPanel());
        this.add(createRightPanel());
        
        
        this.setVisible(true);
    }

    private FileExplorerPanel createLeftPanel() {
        // Panel Creation
        FileExplorerPanel left_panel = new FileExplorerPanel(this, SIZE_X - SIDE_PANEL_SIZE, SIZE_Y, DEFAULT_BORDER_SIZE);
        this.file_explorer_panel = left_panel;

        return left_panel;
    }

    private JPanel createRightPanel() {
        // Panel Creation
        JPanel right_panel = JavaSwingUtils.createPanel(SIDE_PANEL_SIZE, SIZE_Y, DEFAULT_BORDER_SIZE);
        Button refresh_button = new Button("Refresh");
        refresh_button.setMaximumSize(new Dimension(SIDE_PANEL_SIZE, this.DEFAULT_BUTTON_HEIGHT));
        right_panel.add(refresh_button);

        refresh_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFileExplorer();
            }
            
        });

        return right_panel;
    }

    // Frame setup

    protected void setup() {
        this.setupFileManager();

        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Sizing:
        this.setSize(SIZE_X, SIZE_Y);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
    }

    private void setupFileManager() {
        this.config = new ManagerConfig();

        this.file_manager = new FileManager();
        this.updateFileManagerConfigs();

        this.file_manager.defaultUpdateChildren();
    }

    // Updates

    public void updateFileExplorer() {
        this.file_manager.defaultUpdateChildren();
        

    }

    // Utils

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
