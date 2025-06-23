package com.github.ydewolf.swing.ui.FileExplorerPanel.elements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.ydewolf.classes.Files.utils.FileUtils;
import com.github.ydewolf.static_classes.FileFormats;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class SelectFileButton extends JButton {
    protected File related_file;

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

        JPanel extension_holder = new JPanel(new FlowLayout());
        extension_holder.setOpaque(false);
        this.add(extension_holder, BorderLayout.NORTH);

        JLabel extension_label = new JLabel(FileUtils.getFileExtension(file));
        extension_label.setForeground(Color.darkGray);
        extension_label.setFont(getFont().deriveFont(Font.BOLD));
        extension_label.setFont(getFont().deriveFont(16f));
        extension_holder.add(extension_label);

        this.loadFile(file);
    }

    public void loadFile(File file) {
        this.related_file = file;
        name_label.setText(JavaSwingUtils.truncateString(name_label, file.getName(), max_characters));

        if (FileUtils.checkFileExtension(file.getName(), FileFormats.IMAGE_LIKE_EXTENSIONS)) {
            try {
                BufferedImage img = FileUtils.readImageFileCropped(file, this.width, this.height);
                if (img != null) {
                    this.img_label.setIcon(new ImageIcon(img));
                }
                return;

            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.err.println("Yep. I can't access this image, sorry | IOException");
                return;
                // e.printStackTrace();
            }
        }

        // Remove the icon if it is a text file
        if (this.img_label.getIcon() != null) {
            this.img_label.setIcon(null);
        }
    }
}
