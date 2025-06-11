package com.github.ydewolf.swing;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.ydewolf.classes.FileManager;
import com.github.ydewolf.swing.ui.FileExplorerPanel;
import com.github.ydewolf.swing.ui.elements.DebugPanel;
import com.github.ydewolf.swing.ui.elements.menu_items.OpenFolderMenuItem;
import com.github.ydewolf.swing.utils.JavaSwingUtils;
import com.github.ydewolf.swing.utils.ManagerConfig;

public class FileManagerFrame extends JFrame {
    protected FileManager file_manager;
    protected ManagerConfig config;

    protected final int SIZE_X = 800;
    protected final int SIZE_Y = 650;

    protected final int SIDE_PANEL_SIZE = 250;
    protected final int DEFAULT_BORDER_SIZE = 5;

    protected final int DEFAULT_BUTTON_HEIGHT = 20;
    protected final int NAVBAR_HEIGHT = 25;

    protected Thread file_loading_thread;

    protected JFileChooser file_dialog;

    protected FileExplorerPanel file_explorer_panel;
    protected JPanel horizontal_panel;
    protected DebugPanel debug_panel;

    public FileManagerFrame() {
        this.setup();
    }

    // UI Creation + initialization

    public void init() {
        this.horizontal_panel.add(createLeftPanel());
        this.horizontal_panel.add(createRightPanel());
        
        
        this.setVisible(true);
        this.updateFileExplorer();
    }

    private JComponent createNavbar() {
        JMenuBar navbar = new JMenuBar();
        JavaSwingUtils.setupJComponentDim(navbar, SIZE_X, NAVBAR_HEIGHT);

        JMenu file_menu = new JMenu("Directory");
        navbar.add(file_menu);
        // Add MenuItems to file_menu
        {
            OpenFolderMenuItem open_folder_item = new OpenFolderMenuItem(this, this.file_dialog);
            file_menu.add(open_folder_item);
        }

        return navbar;
    }

    private JComponent createLeftPanel() {
        JPanel left_panel = JavaSwingUtils.createPanel(SIZE_X - SIDE_PANEL_SIZE, SIZE_Y, DEFAULT_BORDER_SIZE);
        left_panel.setLayout(new BorderLayout());
        left_panel.setBorder(null);

        FileExplorerPanel explorer_panel = new FileExplorerPanel(this, SIZE_X - SIDE_PANEL_SIZE, SIZE_Y, DEFAULT_BORDER_SIZE);
        this.file_explorer_panel = explorer_panel;

        JScrollPane explorer_view = new JScrollPane(this.file_explorer_panel);
        explorer_view.setMaximumSize(new Dimension(SIZE_X - SIDE_PANEL_SIZE, SIZE_Y));
        explorer_view.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        explorer_view.getVerticalScrollBar().setUnitIncrement(16);

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
                startUpdateThread();
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
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JFileChooser file_dialog = new JFileChooser();
        this.file_dialog = file_dialog;

        this.add(createNavbar());
        
        this.horizontal_panel = JavaSwingUtils.createPanel(SIZE_X, SIZE_Y - NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE);
        this.horizontal_panel.setLayout(new BoxLayout(horizontal_panel, BoxLayout.X_AXIS));

        this.add(this.horizontal_panel);

        this.debug_panel = new DebugPanel(SIZE_X, NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE);
        if (this.config.debug_menu) {
            this.add(this.debug_panel);
        }
        // this.add(file_dialog);
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

    public void updateFileManagerConfigs(ManagerConfig new_config) {
        this.file_manager.loadConfig(new_config);
        this.config = new_config;

        if (debug_panel != null) {
            if (this.config.debug_menu && !this.debug_panel.isDisplayable()) {
                this.add(this.debug_panel);
            } else {
                this.remove(this.debug_panel);
            }
        }

        if (this.config.getChangedRoot()) {
            startUpdateThread();
        }
    }

    public void updateFileManagerConfigs() {
        this.updateFileManagerConfigs(this.config);
    }

    // Other

    public DebugPanel getDebugPanel() {
        return this.debug_panel;
    }

    // Utils

    public void startUpdateThread() {
        if (file_loading_thread.isAlive()) {
            return;
        }

        if (file_loading_thread.getState() != Thread.State.NEW) {
            createNewUpdateThread();
        }

        file_loading_thread.start();
    }

    protected Thread createNewUpdateThread() {
        this.file_loading_thread = new Thread(() -> {this.updateFileExplorer();});
        
        return this.file_loading_thread;
    }

    public FileManager getFileManager() {
        return this.file_manager;
    }

    public ManagerConfig getConfigs() {
        return this.config;
    }
}
