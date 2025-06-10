package com.github.ydewolf.swing.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.ui.elements.SelectFileButton;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileExplorerPanel extends JPanel {
    protected FileManagerFrame manager_frame;
    final protected int IMAGE_SIZE_X = 30;
    final protected int IMAGE_SIZE_Y = 30;

    public FileExplorerPanel(FileManagerFrame manager, int width, int height, int border_size) {
        this.manager_frame = manager;
        JavaSwingUtils.setupJPanel(this, width, height, border_size);

        this.setLayout(new GridLayout(0, 5));
        this.setPreferredSize(new Dimension(IMAGE_SIZE_X, IMAGE_SIZE_Y));
        updateContents();
    }

    public void updateContents() {
        this.removeAll();
        Set<String> paths = manager_frame.getFileManager().getFilePaths();
        Dimension buttonSize = new JButton().getPreferredSize();
        this.setPreferredSize(buttonSize);

        for (String path : paths) {
            SelectFileButton select_button = new SelectFileButton(manager_frame.getFileManager().getFileInstance(path), IMAGE_SIZE_X, IMAGE_SIZE_Y);
            this.add(select_button);
        }
    }
}
