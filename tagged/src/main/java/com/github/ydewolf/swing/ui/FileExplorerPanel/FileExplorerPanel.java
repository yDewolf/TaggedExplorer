package com.github.ydewolf.swing.ui.FileExplorerPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.ydewolf.classes.utils.config.configs.ConfigDebugSettings;
import com.github.ydewolf.enums.DebugTypes;
import com.github.ydewolf.enums.ManagerConfigKeys;
import com.github.ydewolf.swing.FileManagerFrame;
import com.github.ydewolf.swing.ui.FileExplorerPanel.elements.SelectFileButton;
import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class FileExplorerPanel extends JPanel {
    protected FileManagerFrame manager_frame;
    protected HashMap<String, JPanel> img_panels;

    final protected int IMAGE_SIZE_X = 128;
    final protected int IMAGE_SIZE_Y = 96;

    protected int border_size;
    protected GridLayout layout;
    public int target_width;

    public FileExplorerPanel(FileManagerFrame manager, int width, int height, int border_size) {
        this.manager_frame = manager;
        this.border_size = border_size;
        this.img_panels = new HashMap<>();
        target_width = width;

        JavaSwingUtils.setupJComponentDim(this, width, height);
        this.layout = new GridLayout(0, 1);
        this.setLayout(this.layout);

        this.updateLayout(width);
    }

    public void updateLayout(int new_width) {
        int column_count = Math.max(1, (int) Math.floor(new_width / (IMAGE_SIZE_X)) - 1);

        this.layout.setColumns(column_count);
    }

    public void updateContents() {
        Set<String> paths = manager_frame.getFileManager().getFilePaths();

        // Remove panels of images that doesn't exist anymore
        ArrayList<String> paths_to_remove = new ArrayList<>();
        for (String path_key : img_panels.keySet()) {
            if (!paths.contains(path_key)) {
                paths_to_remove.add(path_key);
                continue;
            }
        }

        // If somehow the img_panels desynchronize with the JComponent
        if (img_panels.keySet().size() < this.getComponentCount()) {
            System.err.println("WARNING: Image panels doesn't represent the JComponent buttons | Rebuilding all the Image panels");
            this.removeAll();
        }
        
        int removed_imgs = 0;
        for (String path : paths_to_remove) {
            removed_imgs += 1;

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

        ConfigDebugSettings debug_settings = (ConfigDebugSettings) this.manager_frame.getConfigs().getConfig(ManagerConfigKeys.DebugSettings);
        try {
            for (String path : paths) {
                // Exclude images that were already added
                if (img_panels.containsKey(path)) {
                    continue;
                }

                Thread.sleep(0, 1);
    
                // Generate image label
                long start_time = System.currentTimeMillis();
    
                JPanel button_holder = new JPanel();
                button_holder.setMaximumSize(new Dimension(IMAGE_SIZE_X, IMAGE_SIZE_Y));
                
                // Debug Panels
                if (debug_settings.getKeyValue(DebugTypes.DEBUG_PANELS)) {
                    button_holder.setBackground(Color.black);
                }
    
                JavaSwingUtils.setupJComponentDim(button_holder, IMAGE_SIZE_X, IMAGE_SIZE_Y);
                button_holder.setLayout(new FlowLayout());
                
                SelectFileButton select_button = new SelectFileButton(manager_frame.getFileManager().getFileInstance(path), IMAGE_SIZE_X, IMAGE_SIZE_Y, max_characters);
                select_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        manager_frame.setSelectedFile(path);
                    }
                    
                });
                button_holder.add(select_button);
                
                this.add(button_holder);
                img_panels.put(path, button_holder);
                SwingUtilities.updateComponentTreeUI(button_holder);
                
                // Debugging info
                if (debug_settings.getKeyValue(DebugTypes.DEBUG_ELAPSED_TIME)) {
                    System.out.println("IMG load elapsed time: " + (System.currentTimeMillis() - start_time) + "ms");
                }
    
                total_time += (System.currentTimeMillis() - start_time);
                added_imgs += 1;
    
                updateDebugInfo(total_time, added_imgs, removed_imgs);
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting update files thread");
            Thread.currentThread().interrupt();
        }
    
        updateDebugInfo(total_time, added_imgs, removed_imgs);

        if (debug_settings.getKeyValue(DebugTypes.DEBUG_ELAPSED_TIME)) {
            System.out.println("Total time to load images: " + total_time + "ms | Images loaded: " + added_imgs + " | Removed images: " + removed_imgs);
        }
            
    }

    protected void updateDebugInfo(long total_time, int added_imgs, int removed_imgs) {
        ConfigDebugSettings debug_settings = (ConfigDebugSettings) this.manager_frame.getConfigs().getConfig(ManagerConfigKeys.DebugSettings);
        if (!debug_settings.getKeyValue(DebugTypes.DEBUG_MENU)) {
            return;
        }
        
        this.manager_frame.getDebugPanel().setElapsedTimeText(total_time);
        this.manager_frame.getDebugPanel().setLoadedImagesText(added_imgs);
        this.manager_frame.getDebugPanel().setRemovedImagesText(removed_imgs);
    }
}
