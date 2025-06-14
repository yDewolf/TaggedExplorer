package com.github.ydewolf.swing.ui.SettingsMenu;


import javax.swing.JDialog;
import javax.swing.JPanel;

import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class SettingsMenu extends JDialog {
    public SettingsMenu(FileManagerFrame manager_frame, int width, int height, int border_size) {
        super(manager_frame, "Settings", true);
        this.setSize(width, height);
        // this.setLocationRelativeTo(manager_frame);
        
        JPanel main_panel = new JPanel();
        JavaSwingUtils.setupJComponentDim(main_panel, width, height);
        this.add(main_panel);
    }
}
