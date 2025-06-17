package com.github.ydewolf.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.github.ydewolf.classes.FileManager;
import com.github.ydewolf.classes.SelectHandler.FileSelectHandler;
import com.github.ydewolf.classes.utils.config.ManagerConfig;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.enums.ExplorerStatus;
import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.swing.ColorThemes.ColorTheme;
import com.github.ydewolf.swing.ui.FileExplorerPanel.FileExplorerPanel;
import com.github.ydewolf.swing.ui.FileExplorerPanel.parts.FileExplorerTopPanel;
import com.github.ydewolf.swing.ui.FileInfoPanel.FileInfoPanel;
import com.github.ydewolf.swing.ui.SettingsMenu.SettingsMenu;
import com.github.ydewolf.swing.ui.elements.DebugPanel;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileManagerFrame extends JFrame {
    protected FileManager file_manager;
    protected FileSelectHandler file_select_handler;

    static final String FONT_RESOURCE_PATH = "/fonts/Roboto-Medium.ttf";

    protected ManagerConfig config;

    protected final String WINDOW_TITLE = "TaggedExplorer";
    protected final String VERSION_TAG = "v0.5.5-beta";

    // Scales only the window size
    // Also updates SIZE_X and SIZE_Y
    protected final double SCALE = 0.75;

    protected final int SIZE_X = (int) Math.floor(1080 * SCALE);
    protected final int SIZE_Y = (int) Math.floor(720 * SCALE);

    protected final int SETTINGS_SIZE_X = (int) Math.floor(600);
    protected final int SETTINGS_SIZE_Y = (int) Math.floor(720);

    protected final int SIDE_PANEL_SIZE = (int) Math.floor(SIZE_X / 2.25);
    
    protected final int DEFAULT_BORDER_SIZE = 5;
    
    public final int DEFAULT_BUTTON_HEIGHT = 20;
    protected final int NAVBAR_HEIGHT = 25;
    
    protected final int HORIZONTAL_PANEL_HEIGHT = SIZE_Y - (NAVBAR_HEIGHT * 2) - (DEFAULT_BORDER_SIZE * 2);

    protected Thread file_loading_thread;

    protected JFileChooser file_dialog;
    protected SettingsMenu settings_menu;

    protected final ManagerConfigKeys[] EXCLUDED_SETTINGS = {ManagerConfigKeys.RootFolder};

    protected FileExplorerPanel file_explorer_panel;
    protected FileExplorerTopPanel file_explorer_top_panel;
    protected FileInfoPanel file_info_panel;

    protected JPanel horizontal_panel;
    protected DebugPanel debug_panel;

    protected Boolean ready = false;

    public FileManagerFrame() {
        this.setup();
    }

    // UI Creation + initialization

    public void init() {
        this.file_info_panel = createFileInfoPanel();
        this.horizontal_panel.add(this.file_info_panel);
        this.horizontal_panel.add(createFileExplorerPanel());
        
        this.setVisible(true);
        ready = true;
        this.updateFileExplorer();
        
        try {
            InputStream is = FileManagerFrame.class.getResourceAsStream(FONT_RESOURCE_PATH);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            
            JavaSwingUtils.setFontRecursive(this, font.deriveFont(12f));

        } catch (FontFormatException | IOException e) { }
        
        ColorTheme default_theme = new ColorTheme() {
            
        };
        JavaSwingUtils.applyThemeRecursive(rootPane, default_theme);
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

        // Responsivity
        explorer_view.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int target_width = (int) file_explorer_panel.getVisibleRect().getWidth();
                file_explorer_panel.updateLayout(target_width);
            }
        });

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
        this.file_select_handler = new FileSelectHandler(file_manager);

        this.setResizable(true);
        this.setTitle(this.WINDOW_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Sizing:
        this.setSize(SIZE_X, SIZE_Y);
        BoxLayout layout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        this.setLayout(layout);

        this.settings_menu = new SettingsMenu(this, EXCLUDED_SETTINGS, SETTINGS_SIZE_X, SETTINGS_SIZE_Y, DEFAULT_BORDER_SIZE);

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

    public void setSelectedFile(String abs_path) {
        this.file_select_handler.selectFile(abs_path);

        this.updateFileInfo();
    }

    public void updateFileInfo() {
        if (this.file_info_panel == null) {
            System.err.println("Trying to update file info but file_info_panel is null");
            return;
        }

        file_info_panel.updateSelectedImage();
    }

    public void updateFileExplorer() {
        if (ready) {
            this.file_explorer_top_panel.setStatus(ExplorerStatus.LOOKING_THROUGH_FOLDERS);
        }
        
        this.file_manager.defaultUpdateChildren();

        if (ready) {
             this.file_explorer_top_panel.setStatus(ExplorerStatus.FINISHED_LOOKING_THROUGH_FOLDERS);
            
            this.file_explorer_top_panel.setStatus(ExplorerStatus.LOADING_FILES);
        }
        
        if (ready) {
            this.file_explorer_panel.updateContents();
        }
        
        if (ready) {
            this.file_explorer_top_panel.setStatus(ExplorerStatus.FINISHED_LOADING);
        }
    }

    public void updateFileManagerConfigs(ManagerConfig new_config) {
        if (file_loading_thread != null) {
            if (this.file_loading_thread.isAlive()) {
                this.file_loading_thread.interrupt();
            }
        }

        this.file_manager.loadConfig(new_config);
        this.config = new_config;

        if (debug_panel != null && new_config.getChanged(ManagerConfigKeys.DebugSettings)) {
            if (new_config.getDebug(DebugTypes.DEBUG_MENU)) {
                this.add(this.debug_panel);
            } else {
                this.remove(this.debug_panel);
            }
            SwingUtilities.updateComponentTreeUI(this);
        }

        if (this.file_info_panel != null) {
            this.file_info_panel.updateFolderInfo();
        }

        if (new_config.getChanged(ManagerConfigKeys.RootFolder)) {
            this.startUpdateThread();
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
        this.settings_menu.setLocationRelativeTo(this);
        this.settings_menu.setVisible(!this.settings_menu.isVisible());
    }

    // Utils

    public void startUpdateThread() {
        if (file_loading_thread == null) {
            this.createNewUpdateThread();
            file_loading_thread.start();
            return;
        }

        if (file_loading_thread.isAlive()) {
            try {
                file_loading_thread.interrupt();
                file_loading_thread.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // return;
        }

        if (file_loading_thread.getState() != Thread.State.NEW) {
            this.createNewUpdateThread();
        }

        file_loading_thread.start();
    }

    protected Thread createNewUpdateThread() {
        this.file_loading_thread = new Thread(() -> {
            this.updateFileExplorer();

            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        });
        
        return this.file_loading_thread;
    }

    public FileManager getFileManager() {
        return this.file_manager;
    }

    public FileSelectHandler getSelectHandler() {
        return this.file_select_handler;
    }

    public ManagerConfig getConfigs() {
        return this.config;
    }

    public JFileChooser getFileChooser() {
        return this.file_dialog;
    }
}
