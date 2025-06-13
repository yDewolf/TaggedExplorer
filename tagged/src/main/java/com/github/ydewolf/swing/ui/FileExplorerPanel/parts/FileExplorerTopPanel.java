package com.github.ydewolf.swing.ui.FileExplorerPanel.parts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import com.github.ydewolf.enums.ExplorerStatus;
import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.ui.elements.menu_items.OpenFolderMenuItem;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileExplorerTopPanel extends JMenuBar {
    protected JTextField status_label;
    public FileExplorerTopPanel(FileManagerFrame manager_frame, int width, int height, int border_size) {
        JavaSwingUtils.setupJComponentDim(this, width, height);
        this.setLayout(new BorderLayout());

        JMenuBar left_div = new JMenuBar();
        left_div.setLayout(new FlowLayout());
        this.add(left_div, BorderLayout.WEST);

        left_div.add(new OpenFolderMenuItem(manager_frame, manager_frame.getFileChooser()));
        
        JMenuItem refresh_button = new JMenuItem("Refresh");
        refresh_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager_frame.startUpdateThread();
            }
        });

        left_div.add(refresh_button);

        JMenuBar right_div = new JMenuBar();
        this.add(right_div, BorderLayout.EAST);

        this.status_label = new JTextField();
        this.status_label.setBorder(null);
        this.status_label.setEnabled(false);
        this.status_label.setDisabledTextColor(Color.BLACK);

        this.setStatus(ExplorerStatus.IDLE);
        right_div.add(status_label);

        // TODO: Create a class for this menu
        JMenu options_menu = new JMenu("Settings");
        right_div.add(options_menu);
    }

    public void setStatus(ExplorerStatus status) {
        this.status_label.setText("" + status);
    }
}
