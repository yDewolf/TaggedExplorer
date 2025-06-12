package com.github.ydewolf.swing;

import java.awt.BorderLayout;
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
import com.github.ydewolf.classes.utils.config.BaseManagerConfig;
import com.github.ydewolf.classes.utils.config.ManagerConfig;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.swing.ui.FileExplorerPanel.FileExplorerPanel;
import com.github.ydewolf.swing.ui.FileExplorerPanel.parts.FileExplorerTopPanel;
import com.github.ydewolf.swing.ui.FileInfoPanel.FileInfoPanel;
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


    protected final int SIDE_PANEL_SIZE = 350;
    protected final int DEFAULT_BORDER_SIZE = 5;

    public final int DEFAULT_BUTTON_HEIGHT = 20;
    protected final int NAVBAR_HEIGHT = 25;

    protected Thread file_loading_thread;

    protected JFileChooser file_dialog;

    protected FileExplorerPanel file_explorer_panel;
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
        JPanel panel = JavaSwingUtils.createPanel(SIZE_X - SIDE_PANEL_SIZE, SIZE_Y - NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE);
        panel.setBorder(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new FileExplorerTopPanel(this, SIZE_X - SIDE_PANEL_SIZE, NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE));
        
        // Setup FileExplorerPanel view

        // JPanel explorer_holder = JavaSwingUtils.createPanel(SIZE_X - SIDE_PANEL_SIZE, SIZE_Y - NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE);
        // explorer_holder.setBorder(null);
        // explorer_holder.setLayout(new BorderLayout());
        
        // panel.add(explorer_holder);
    
        FileExplorerPanel explorer_panel = new FileExplorerPanel(this, SIZE_X - SIDE_PANEL_SIZE, SIZE_Y - NAVBAR_HEIGHT * 2, DEFAULT_BORDER_SIZE);
        this.file_explorer_panel = explorer_panel;

        JScrollPane explorer_view = new JScrollPane(this.file_explorer_panel);
        explorer_view.setPreferredSize(new Dimension(SIZE_X - SIDE_PANEL_SIZE, SIZE_Y - NAVBAR_HEIGHT * 2));
        explorer_view.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        explorer_view.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(explorer_view, BorderLayout.CENTER);


        return panel;
    }

    private FileInfoPanel createFileInfoPanel() {
        // Panel Creation
        FileInfoPanel panel = new FileInfoPanel(this, SIDE_PANEL_SIZE, SIZE_Y - NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE);
        // JPanel panel = JavaSwingUtils.createPanel(SIDE_PANEL_SIZE, SIZE_Y - NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE);


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
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JFileChooser file_dialog = new JFileChooser();
        this.file_dialog = file_dialog;

        this.add(createNavbar());
        
        // Create the main Horizontal Panel
        this.horizontal_panel = JavaSwingUtils.createPanel(SIZE_X, SIZE_Y - NAVBAR_HEIGHT, DEFAULT_BORDER_SIZE);
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

        // this.file_manager.defaultUpdateChildren();
    }

    // Updates

    public void updateFileExplorer() {
        this.file_manager.defaultUpdateChildren();
        this.file_explorer_panel.updateContents();
    }

    public void updateFileManagerConfigs(BaseManagerConfig new_config) {
        this.file_manager.loadConfig(new_config);
        this.config = new_config;

        if (debug_panel != null) {
            if (this.config.getDebug(DebugTypes.DEBUG_MENU) && !this.debug_panel.isDisplayable()) {
                this.add(this.debug_panel);
            } else {
                this.remove(this.debug_panel);
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
        this.file_loading_thread = new Thread(() -> {this.updateFileExplorer();});
        
        return this.file_loading_thread;
    }

    public FileManager getFileManager() {
        return this.file_manager;
    }

    public BaseManagerConfig getConfigs() {
        return this.config;
    }
}
