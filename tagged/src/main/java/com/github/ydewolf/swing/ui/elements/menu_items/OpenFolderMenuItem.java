package com.github.ydewolf.swing.ui.elements.menu_items;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.utils.ManagerConfig;

public class OpenFolderMenuItem extends JMenuItem {
    public OpenFolderMenuItem(FileManagerFrame main_panel, JFileChooser file_dialog) {
        super("Open directory");

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file_dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                int option = file_dialog.showOpenDialog(main_panel);
                if (option == JFileChooser.APPROVE_OPTION) {
                    ManagerConfig config = main_panel.getConfigs();
                    config.setRoot(file_dialog.getSelectedFile());
                    
                    main_panel.updateFileManagerConfigs(config);
                }
            }
        });
    }
}
