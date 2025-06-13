package com.github.ydewolf.swing.ui.FileExplorerPanel.elements;

import javax.swing.JLabel;
import javax.swing.JMenu;

import com.github.ydewolf.classes.utils.config.ManagerConfig;
import com.github.ydewolf.classes.utils.config.abstract_classes.BaseManagerConfig;
import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.swing.FileManagerFrame;

public class SettingsMenu extends JMenu {
    protected FileManagerFrame manager_frame;
    public SettingsMenu(FileManagerFrame manager_frame) {
        this.manager_frame = manager_frame;
        this.setText("Settings");

        this.addConfigOptions(this.manager_frame.getConfigs());
    }

    protected void addConfigOptions(BaseManagerConfig configs) {
        for (ManagerConfigKeys key: configs.getConfigs()) {
            JLabel config = new JLabel(key.toString());
            this.add(config);
        }
    }
}
