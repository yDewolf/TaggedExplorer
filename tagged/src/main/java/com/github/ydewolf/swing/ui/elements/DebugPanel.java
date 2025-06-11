package com.github.ydewolf.swing.ui.elements;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class DebugPanel extends JPanel {
    protected JLabel time_elapsed_label;
    protected JLabel loaded_images_label;
    protected JLabel removed_images_label;


    public DebugPanel(int width, int height, int border_size) {
        JavaSwingUtils.setupJPanel(this, width, height, border_size);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.time_elapsed_label = new JLabel("Placeholder for elapsed time");
        this.add(time_elapsed_label);

        this.loaded_images_label = new JLabel("Placeholder for loaded images");
        this.add(loaded_images_label);
        
        this.removed_images_label = new JLabel("Placeholder for removed images");
        this.add(removed_images_label);
    }

    public void setElapsedTimeText(long elapsed_ms) {
        this.time_elapsed_label.setText("Elapsed time: " + elapsed_ms + "ms");
    }

    public void setLoadedImagesText(int img_count) {
        this.loaded_images_label.setText("Loaded images: " + img_count);
    }
    
    public void setRemovedImagesText(int img_count) {
        this.removed_images_label.setText("Removed images: " + img_count);
    }
}
