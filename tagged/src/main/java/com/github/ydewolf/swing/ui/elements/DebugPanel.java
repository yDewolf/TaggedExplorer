package com.github.ydewolf.swing.ui.elements;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.ydewolf.swing.utils.JavaSwingUtils;

public class DebugPanel extends JPanel {
    protected JLabel time_elapsed_label;
    protected JLabel loaded_images_label;
    protected JLabel removed_images_label;

    protected JLabel version_label;

    public DebugPanel(int width, int height, int border_size) {
        JavaSwingUtils.setupJPanel(this, width, height, border_size);
        this.setMaximumSize(new Dimension(width * 10, height));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JPanel left_panel = new JPanel();
        left_panel.setLayout(new GridLayout(1, 3));
        this.add(left_panel);

        this.time_elapsed_label = new JLabel("Placeholder for elapsed time");
        left_panel.add(time_elapsed_label);

        this.loaded_images_label = new JLabel("Placeholder for loaded images");
        left_panel.add(loaded_images_label);
        
        this.removed_images_label = new JLabel("Placeholder for removed images");
        left_panel.add(removed_images_label);

        this.version_label = new JLabel("v0.0.0");
        this.add(version_label);
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

    public void setVersionText(String version) {
        this.version_label.setText(version);
    }
}
