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

    public SelectFileButton(File file, int target_width, int target_height, int max_characters) {
        this.setLayout(new BorderLayout());
        this.setToolTipText(file.getAbsolutePath());

        JavaSwingUtils.setupJComponentDim(this, target_width, target_height);

        try {
            BufferedImage img = JavaSwingUtils.cropImage(file, target_width, target_height);
            
            JLabel label = new JLabel(new ImageIcon(img));
            this.add(label, BorderLayout.CENTER);

            JLabel name_label = new JLabel();
            name_label.setPreferredSize(new Dimension(target_width, 20));
            name_label.setText(JavaSwingUtils.truncateString(name_label, file.getName(), max_characters));
            this.add(name_label, BorderLayout.SOUTH);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
