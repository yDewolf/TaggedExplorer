package com.github.ydewolf.swing;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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

    protected Thread file_loading_thread;

    protected FileExplorerPanel file_explorer_panel;

    public FileManagerFrame() {
        createNewUpdateThread();
        this.setup();
    }

    // UI Creation + initialization

    public void init() {
        // createLeftPanel();
        this.add(createLeftPanel());
        this.add(createRightPanel());
        
        
        this.setVisible(true);
        this.updateFileExplorer();
    }

    private JComponent createLeftPanel() {
        JPanel left_panel = JavaSwingUtils.createPanel(SIZE_X - SIDE_PANEL_SIZE, SIZE_Y, DEFAULT_BORDER_SIZE);
        left_panel.setLayout(new BorderLayout());

        FileExplorerPanel explorer_panel = new FileExplorerPanel(this, SIZE_X - SIDE_PANEL_SIZE, SIZE_Y, DEFAULT_BORDER_SIZE);
        this.file_explorer_panel = explorer_panel;

        JScrollPane explorer_view = new JScrollPane(this.file_explorer_panel);
        explorer_view.setMaximumSize(new Dimension(SIZE_X - SIDE_PANEL_SIZE, 200));
        explorer_view.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        left_panel.add(explorer_view);

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
                if (file_loading_thread.isAlive()) {
                    return;
                }

                if (file_loading_thread.getState() != Thread.State.NEW) {
                    createNewUpdateThread();
                }

                file_loading_thread.start();
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

        // this.file_manager.defaultUpdateChildren();
    }

    // Updates

    public void updateFileExplorer() {
        this.file_manager.defaultUpdateChildren();
        this.file_explorer_panel.updateContents();
    }

    // Utils

    public Thread createNewUpdateThread() {
        this.file_loading_thread = new Thread(() -> {this.updateFileExplorer();});
        
        return this.file_loading_thread;
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
