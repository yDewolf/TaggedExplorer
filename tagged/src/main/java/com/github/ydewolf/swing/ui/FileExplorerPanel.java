package com.github.ydewolf.swing.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Set;
import java.util.concurrent.Flow;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.ui.elements.SelectFileButton;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileExplorerPanel extends JPanel {
    protected FileManagerFrame manager_frame;
    final protected int IMAGE_SIZE_X = 128;
    final protected int IMAGE_SIZE_Y = 128;

    public FileExplorerPanel(FileManagerFrame manager, int width, int height, int border_size) {
        this.manager_frame = manager;
        JavaSwingUtils.setupJPanel(this, width, height, border_size);
        
        int column_count = Math.floorDiv(width, IMAGE_SIZE_X + 10);
        this.setLayout(new GridLayout(0, column_count));

        updateContents();
    }

    public void updateContents() {
        this.removeAll();
        Set<String> paths = manager_frame.getFileManager().getFilePaths();
        // Dimension buttonSize = new JButton().getPreferredSize();
        // this.setPreferredSize(buttonSize);

        for (String path : paths) {
            JPanel button_holder = new JPanel();
            JavaSwingUtils.setupJComponentDim(button_holder, IMAGE_SIZE_X, IMAGE_SIZE_Y);
            button_holder.setLayout(new FlowLayout());

            SelectFileButton select_button = new SelectFileButton(manager_frame.getFileManager().getFileInstance(path), IMAGE_SIZE_X, IMAGE_SIZE_Y);
            button_holder.add(select_button);
            
            // JPanel panel = new JPanel(new FlowLayout());
            // panel.add(button_holder);

            this.add(button_holder);
        }
    }
}
