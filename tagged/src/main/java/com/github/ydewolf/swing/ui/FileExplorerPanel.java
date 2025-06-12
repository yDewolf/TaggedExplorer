package com.github.ydewolf.swing.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.ui.elements.SelectFileButton;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileExplorerPanel extends JPanel {
    protected FileManagerFrame manager_frame;
    protected HashMap<String, JPanel> img_panels;

    final protected int IMAGE_SIZE_X = 128;
    final protected int IMAGE_SIZE_Y = 96;

    public FileExplorerPanel(FileManagerFrame manager, int width, int height, int border_size) {
        this.manager_frame = manager;
        this.img_panels = new HashMap<>();

        JavaSwingUtils.setupJComponentDim(this, width, height);
        
        int column_count = Math.floorDiv(width, IMAGE_SIZE_X + 10);
        this.setLayout(new GridLayout(0, column_count));

        // updateContents();
    }

    public void updateContents() {
        // this.removeAll();
        Set<String> paths = manager_frame.getFileManager().getFilePaths();

        // Remove panels of images that doesn't exist anymore
        int removed_imgs = 0;
        ArrayList<String> paths_to_remove = new ArrayList<>();
        for (String path_key : img_panels.keySet()) {
            if (!paths.contains(path_key)) {
                paths_to_remove.add(path_key);
                removed_imgs += 1;
                continue;
            }
        }
        
        for (String path : paths_to_remove) {
            JPanel panel = this.img_panels.get(path);
            this.remove(panel);
            
            this.img_panels.remove(path);
        }
        SwingUtilities.updateComponentTreeUI(this);
        
        //  Add the new images
        long total_time = 0;
        int added_imgs = 0;

        // Label max characters
        JLabel sample_label = new JLabel();
        sample_label.setPreferredSize(new Dimension(IMAGE_SIZE_X, 20));
        int max_characters = JavaSwingUtils.getMaxCharacters(sample_label, "...");

        for (String path : paths) {
            // Exclude images that were already added
            if (img_panels.containsKey(path)) {
                continue;
            }

            // Generate image label
            long start_time = System.currentTimeMillis();

            JPanel button_holder = new JPanel();
            JavaSwingUtils.setupJComponentDim(button_holder, IMAGE_SIZE_X, IMAGE_SIZE_Y);
            button_holder.setLayout(new FlowLayout());
            
            SelectFileButton select_button = new SelectFileButton(manager_frame.getFileManager().getFileInstance(path), IMAGE_SIZE_X, IMAGE_SIZE_Y, max_characters);
            button_holder.add(select_button);
            
            this.add(button_holder);
            img_panels.put(path, button_holder);

            if (this.manager_frame.getConfigs().debug_elapsed) {
                System.out.println("IMG load elapsed time: " + (System.currentTimeMillis() - start_time) + "ms");
                total_time += (System.currentTimeMillis() - start_time);
                added_imgs += 1;
            }

            SwingUtilities.updateComponentTreeUI(button_holder);
        }

        if (this.manager_frame.getConfigs().debug_menu) {
            this.manager_frame.getDebugPanel().setElapsedTimeText(total_time);
            this.manager_frame.getDebugPanel().setLoadedImagesText(added_imgs);
            this.manager_frame.getDebugPanel().setRemovedImagesText(removed_imgs);
        }

        if (this.manager_frame.getConfigs().debug_elapsed) {
            System.out.println("Total time to load images: " + total_time + "ms | Images loaded: " + added_imgs + " | Removed images: " + removed_imgs);
        }
    }
}
