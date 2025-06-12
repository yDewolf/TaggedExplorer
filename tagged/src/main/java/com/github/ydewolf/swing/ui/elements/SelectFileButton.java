package com.github.ydewolf.swing.ui.elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class SelectFileButton extends JButton {
    protected JLabel img_label;
    protected JLabel name_label;

    protected int width;
    protected int height;
    protected int max_characters = 0;

    public SelectFileButton(File file, int target_width, int target_height, int max_characters) {
        this.setLayout(new BorderLayout());
        this.setToolTipText(file.getAbsolutePath());

        // Setup variables
        this.width = target_width;
        this.height = target_height;
        this.max_characters = max_characters;

        JavaSwingUtils.setupJComponentDim(this, target_width, target_height);

        // Setup labels
        this.img_label = new JLabel();
        this.add(img_label, BorderLayout.CENTER);

        this.name_label = new JLabel();
        name_label.setPreferredSize(new Dimension(target_width, 20));
        this.add(name_label, BorderLayout.SOUTH);

        this.loadFile(file);
    }

    public void loadFile(File file) {
        try {
            BufferedImage img = JavaSwingUtils.cropImage(file, this.width, this.height);
            this.img_label.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        name_label.setText(JavaSwingUtils.truncateString(name_label, file.getName(), max_characters));
    }
}
