package com.github.ydewolf.swing.ui.FileExplorerPanel.elements;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.github.ydewolf.classes.utils.config.abstract_classes.BaseManagerConfig;
import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.swing.FileManagerFrame;

public class SettingsMenuItem extends JMenuItem {
    protected FileManagerFrame manager_frame;
    public SettingsMenuItem(FileManagerFrame manager_frame) {
        this.manager_frame = manager_frame;
        this.setText("Settings");
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager_frame.openSettingsMenu();
            }
        });

        // this.addConfigOptions(this.manager_frame.getConfigs());
    }

    protected void addConfigOptions(BaseManagerConfig configs) {
        for (ManagerConfigKeys key: configs.getConfigs()) {
            JMenuItem config = new JMenuItem(configs.getConfig(key).getName());
            this.add(config);
        }
    }
}
