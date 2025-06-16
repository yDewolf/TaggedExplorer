package com.github.ydewolf.swing.ui.FileInfoPanel;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.ydewolf.classes.Files.FileRef;
import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.ui.FileInfoPanel.elements.ImageView;
import com.github.ydewolf.swing.ui.FileInfoPanel.elements.ImageControls;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileInfoPanel extends JPanel {
    protected FileManagerFrame manager;
    protected int width;
    protected int height;
    protected int border_size;
    protected int field_height = 20;

    protected int max_characters;

    protected JTextField folder_root_field;
    protected JLabel image_label;

    protected JTextField filename_field;
    protected JTextField file_path_field;

    protected BufferedImage full_image;
    
    protected ImageControls image_controls;

    public FileInfoPanel(FileManagerFrame manager, int width, int height, int border_size) {
        this.manager = manager;
        this.width = width;
        this.height = height;
        this.border_size = border_size;

        JavaSwingUtils.setupJPanel(this, width, height, border_size);

        this.setupLabels();
    }

    public void setupLabels() {
        JPanel top_panel = new JPanel();
        JavaSwingUtils.setupJPanel(top_panel, width, field_height, border_size);
        this.add(top_panel);

        this.folder_root_field = JavaSwingUtils.createTextfieldRow(top_panel, "Root folder:", width, field_height);
        // Setup image view
        {
            JPanel image_view_holder = new JPanel();
            image_view_holder.setLayout(new BoxLayout(image_view_holder, BoxLayout.Y_AXIS));
            // image_view_holder.setLayout(new OverlayLayout(image_view_holder));
            this.add(image_view_holder);

            ImageView image_view = new ImageView(width, width);
            this.image_label = image_view.getImageLabel();

            image_view_holder.add(image_view, BorderLayout.CENTER);

            this.image_controls = new ImageControls(this, width, 40);
            image_view_holder.add(image_controls, BorderLayout.SOUTH);
        }

        JPanel panel = new JPanel();
        JavaSwingUtils.setupJPanel(panel, width, 80, border_size);
        this.add(panel);

        this.filename_field = JavaSwingUtils.createTextfieldRow(panel, "Filename: ", width, field_height);
        this.file_path_field = JavaSwingUtils.createTextfieldRow(panel, "File Path: ", width, field_height);

        updateFolderInfo();
        updateFileInfo();
    }

    public void updateFolderInfo() {
        this.folder_root_field.setText(((File) this.manager.getConfigs().getConfigValue(ManagerConfigKeys.RootFolder)).getAbsolutePath());
    }

    public void updateFileInfo() {
        if (this.filename_field == null || this.file_path_field == null) {
            return;
        }
        FileRef selected_file = this.manager.getSelectHandler().getSelectedFile();
        if (selected_file == null) {
            this.filename_field.setText("No file selected");
            this.file_path_field.setText("No file selected");
            return;
        }

        this.filename_field.setText(selected_file.getFilename());
        this.file_path_field.setText(selected_file.getPath());
    }

    public void updateSelectedImage() {
        updateFileInfo();
        
        this.full_image = this.manager.getSelectHandler().getSelectedFileAsImage(0, 0);
        updateImage();
    }
    
    public void updateImage() {
        if (this.full_image == null) {
            return;
        }

        int newImageWidth = (int) (full_image.getWidth() * this.image_controls.getZoomLevel());
        int newImageHeight = (int) (full_image.getHeight() * this.image_controls.getZoomLevel());

        BufferedImage resizedImage = new BufferedImage(newImageWidth , newImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(full_image, 0, 0, newImageWidth, newImageHeight, null);
        g.dispose();

        this.image_label.setIcon(new ImageIcon(resizedImage));
    }
}
