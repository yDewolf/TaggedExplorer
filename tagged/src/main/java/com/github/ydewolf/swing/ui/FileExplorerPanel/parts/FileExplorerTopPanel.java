package com.github.ydewolf.swing.ui.FileExplorerPanel.parts;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JPanel;

import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileExplorerTopPanel extends JPanel {
    public FileExplorerTopPanel(FileManagerFrame manager_frame, int width, int height, int border_size) {
        JavaSwingUtils.setupJPanel(this, width, height, border_size);
        this.setBorder(null);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        // this.add(new JLabel("Placeholder"));Button refresh_button = new Button("Refresh");
        
        Button refresh_button = new Button("Refresh");
        refresh_button.setMaximumSize(new Dimension(0, manager_frame.DEFAULT_BUTTON_HEIGHT));

        refresh_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager_frame.startUpdateThread();
            }
            
        });

        this.add(refresh_button);
    }
}
