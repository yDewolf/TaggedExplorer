package com.github.ydewolf.swing.ui.elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;


public class SelectFileButton extends JButton {
    public SelectFileButton(File file, int width, int height) {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(width, height));
        try {
            BufferedImage image = ImageIO.read(file);
            JLabel label = new JLabel(new ImageIcon(image));
            
            this.add(label, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
