package com.github.ydewolf.swing.ui.FileInfoPanel.elements;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.ydewolf.swing.ui.FileInfoPanel.FileInfoPanel;

public class ImageControls extends JPanel {
    protected double zoom_level = 1;

    protected final double ZOOM_INCREMENT = 0.1;

    protected final double MAX_ZOOM = 3;
    protected final double MIN_ZOOM = 0.1;

    protected JLabel zoom_label;
    protected FileInfoPanel info_panel;

    public ImageControls(FileInfoPanel info_panel, int width, int height) {
        this.info_panel = info_panel;

        // this.setOpaque(false);
        this.setPreferredSize(new Dimension(width, height));
        
        // Controls
        {   
            this.zoom_label = new JLabel("" + this.zoom_level);
            this.add(zoom_label);

            JButton zoom_in = new JButton("+");
            zoom_in.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateZoomLevel(Math.min(zoom_level + ZOOM_INCREMENT, MAX_ZOOM));
                }
            });
            this.add(zoom_in);

            JButton zoom_out = new JButton("-");
            zoom_out.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateZoomLevel(Math.max(zoom_level - ZOOM_INCREMENT, MIN_ZOOM));
                }
            });
            this.add(zoom_out);
        }
    }

    public void updateZoomLevel(double new_zoom_level) {
        this.zoom_level = new_zoom_level;
        String text = String.format("%10.1f", new_zoom_level);
        zoom_label.setText(text);
        info_panel.updateImage();
    }

    public double getZoomLevel() {
        return this.zoom_level;
    }
}
