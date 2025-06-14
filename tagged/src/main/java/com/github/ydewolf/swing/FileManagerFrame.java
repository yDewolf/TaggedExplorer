package com.github.ydewolf.swing;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.ydewolf.classes.FileManager;
import com.github.ydewolf.classes.utils.config.ManagerConfig;
import com.github.ydewolf.classes.utils.config.abstract_classes.BaseManagerConfig;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.enums.ExplorerStatus;
import com.github.ydewolf.swing.ui.FileExplorerPanel.FileExplorerPanel;
import com.github.ydewolf.swing.ui.FileExplorerPanel.parts.FileExplorerTopPanel;
import com.github.ydewolf.swing.ui.FileInfoPanel.FileInfoPanel;
import com.github.ydewolf.swing.ui.SettingsMenu.SettingsMenu;
import com.github.ydewolf.swing.ui.elements.DebugPanel;
import com.github.ydewolf.swing.ui.elements.menu_items.OpenFolderMenuItem;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileManagerFrame extends JFrame {
    protected FileManager file_manager;
    protected BaseManagerConfig config;

    protected final String WINDOW_TITLE = "TaggedExplorer-0.0.1";
    protected final String VERSION_TAG = "v0.0.1-ALPHA";

    // Scales only the window size
    // Also updates SIZE_X and SIZE_Y
    protected final double SCALE = 0.75;

    protected final int SIZE_X = (int) Math.floor(1080 * SCALE);
    protected final int SIZE_Y = (int) Math.floor(720 * SCALE);

    protected final int SETTINGS_SIZE_X = (int) Math.floor(600);
    protected final int SETTINGS_SIZE_Y = (int) Math.floor(720);

    protected final int SIDE_PANEL_SIZE = 350;
    
    protected final int DEFAULT_BORDER_SIZE = 5;
    
    public final int DEFAULT_BUTTON_HEIGHT = 20;
    protected final int NAVBAR_HEIGHT = 25;
    
    protected final int HORIZONTAL_PANEL_HEIGHT = SIZE_Y - (NAVBAR_HEIGHT * 2) - (DEFAULT_BORDER_SIZE * 2);

    protected Thread file_loading_thread;

    protected JFileChooser file_dialog;
    protected SettingsMenu settings_menu;

    protected FileExplorerPanel file_explorer_panel;
    protected FileExplorerTopPanel file_explorer_top_panel;
    protected FileInfoPanel file_info_panel;

    protected JPanel horizontal_panel;
    protected DebugPanel debug_panel;

    public FileManagerFrame() {
        this.setup();
    }

    // UI Creation + initialization

    public void init() {
        this.file_info_panel = createFileInfoPanel();
        this.horizontal_panel.add(this.file_info_panel);
        this.horizontal_panel.add(createFileExplorerPanel());
        
        this.setVisible(true);
        this.updateFileExplorer();
    }

    @SuppressWarnings("unused")
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

    private JComponent createFileExplorerPanel() {
        int width = SIZE_X - SIDE_PANEL_SIZE;
        JPanel panel = JavaSwingUtils.createPanel(width, HORIZONTAL_PANEL_HEIGHT, DEFAULT_BORDER_SIZE);
        panel.setBorder(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.file_explorer_top_panel = new FileExplorerTopPanel(this, width, NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE);
        panel.add(this.file_explorer_top_panel);
        
        // Setup FileExplorerPanel view

        FileExplorerPanel explorer_panel = new FileExplorerPanel(this, width, HORIZONTAL_PANEL_HEIGHT, DEFAULT_BORDER_SIZE);
        this.file_explorer_panel = explorer_panel;

        JScrollPane explorer_view = new JScrollPane(this.file_explorer_panel);
        explorer_view.setPreferredSize(new Dimension(width, HORIZONTAL_PANEL_HEIGHT - NAVBAR_HEIGHT));
        explorer_view.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        explorer_view.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(explorer_view);


        return panel;
    }

    private FileInfoPanel createFileInfoPanel() {
        // Panel Creation
        FileInfoPanel panel = new FileInfoPanel(this, SIDE_PANEL_SIZE, HORIZONTAL_PANEL_HEIGHT, DEFAULT_BORDER_SIZE);

        return panel;
    }

    // Frame setup

    protected void setup() {
        this.setupFileManager();

        this.setResizable(false);
        this.setTitle(this.WINDOW_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Sizing:
        this.setSize(SIZE_X, SIZE_Y);
        BoxLayout layout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        this.setLayout(layout);

        this.settings_menu = new SettingsMenu(this, SETTINGS_SIZE_X, SETTINGS_SIZE_Y, DEFAULT_BORDER_SIZE);

        this.file_dialog = new JFileChooser();

        // Create navbar
        // this.add(createNavbar());
        
        // Create the main Horizontal Panel
        this.horizontal_panel = JavaSwingUtils.createPanel(SIZE_X, HORIZONTAL_PANEL_HEIGHT, DEFAULT_BORDER_SIZE);
        this.horizontal_panel.setLayout(new BoxLayout(horizontal_panel, BoxLayout.X_AXIS));

        this.add(this.horizontal_panel);

        // Create Debug Panel
        this.debug_panel = new DebugPanel(SIZE_X, NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE);
        if (this.config.getDebug(DebugTypes.DEBUG_MENU)) {
            this.add(this.debug_panel);
            this.debug_panel.setVersionText(VERSION_TAG);
        }
    }

    private void setupFileManager() {
        this.config = new ManagerConfig();

        this.file_manager = new FileManager();
        this.updateFileManagerConfigs();
    }

    // Updates

    public void updateFileExplorer() {
        this.file_explorer_top_panel.setStatus(ExplorerStatus.LOOKING_THROUGH_FOLDERS);
        
        this.file_manager.defaultUpdateChildren();
        this.file_explorer_top_panel.setStatus(ExplorerStatus.FINISHED_LOOKING_THROUGH_FOLDERS);
        
        this.file_explorer_top_panel.setStatus(ExplorerStatus.LOADING_FILES);
        this.file_explorer_panel.updateContents();
        this.file_explorer_top_panel.setStatus(ExplorerStatus.FINISHED_LOADING);
    }

    public void updateFileManagerConfigs(BaseManagerConfig new_config) {
        this.file_manager.loadConfig(new_config);
        this.config = new_config;

        if (debug_panel != null) {
            if (this.config.getDebug(DebugTypes.DEBUG_MENU) && !this.debug_panel.isDisplayable()) {
                this.add(this.debug_panel);
            } else {
                if (!this.config.getDebug(DebugTypes.DEBUG_MENU)) {
                    this.remove(this.debug_panel);
                }
            }
        }

        if (this.file_info_panel != null) {
            this.file_info_panel.updateFolderInfo();
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

    public void openSettingsMenu() {
        this.settings_menu.setVisible(!this.settings_menu.isVisible());
    }

    // Utils

    public void startUpdateThread() {
        if (file_loading_thread == null) {
            createNewUpdateThread();
            file_loading_thread.start();
            return;
        }

        if (file_loading_thread.isAlive()) {
            return;
        }

        if (file_loading_thread.getState() != Thread.State.NEW) {
            createNewUpdateThread();
        }

        file_loading_thread.start();
    }

    protected Thread createNewUpdateThread() {
        this.file_loading_thread = new Thread(() -> {
            this.updateFileExplorer();
        });
        
        return this.file_loading_thread;
    }

    public FileManager getFileManager() {
        return this.file_manager;
    }

    public BaseManagerConfig getConfigs() {
        return this.config;
    }

    public JFileChooser getFileChooser() {
        return this.file_dialog;
    }
}
