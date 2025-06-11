package com.github.ydewolf.swing.ui.elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.github.ydewolf.swing.utils.JavaSwingUtils;


public class SelectFileButton extends JButton {
    public SelectFileButton(File file, int width, int height) {
        this.setLayout(new BorderLayout());
        JavaSwingUtils.setupJComponentDim(this, width, height);

        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        JLabel label = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_FAST)));
        this.add(label, BorderLayout.CENTER);

        // JLabel name_label = new JLabel(file.getName());
        // this.add(name_label, BorderLayout.SOUTH);
    }
}
