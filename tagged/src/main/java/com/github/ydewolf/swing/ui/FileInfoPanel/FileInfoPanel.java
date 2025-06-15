package com.github.ydewolf.swing.ui.FileInfoPanel;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileInfoPanel extends JPanel {
    protected FileManagerFrame manager;
    protected int width;
    protected int height;

    protected int max_characters;

    protected JLabel folder_root_label;

    public FileInfoPanel(FileManagerFrame manager, int width, int height, int border_size) {
        this.manager = manager;
        this.width = width;
        this. height = height;

        JavaSwingUtils.setupJPanel(this, width, height, border_size);
        this.setupLabels();
    }

    public void setupLabels() {
        this.folder_root_label = new JLabel("Placeholder folder path");
        this.add(folder_root_label);

        this.max_characters = JavaSwingUtils.getMaxCharacters(folder_root_label);

        updateFolderInfo();
    }

    public void updateFolderInfo() {
        this.folder_root_label.setText(JavaSwingUtils.truncateString(
                this.folder_root_label,
                ((File) this.manager.getConfigs().getConfigValue(ManagerConfigKeys.RootFolder)).getAbsolutePath(),
                this.max_characters
        ));
    }

    public void updateSelectedImage() {

    }
}
