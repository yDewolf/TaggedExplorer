package com.github.ydewolf.swing.ui.FileInfoPanel.elements;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class ImageView extends JScrollPane {
    protected JLabel image_label;

    public ImageView(int width, int height) {
        // image_view.setOpaque(false);
        this.getVerticalScrollBar().setUnitIncrement(16);
        this.getHorizontalScrollBar().setUnitIncrement(16);

        this.image_label = new JLabel();
        
        this.setViewportView(image_label);
        this.setPreferredSize(new Dimension(width, height));
    }

    public JLabel getImageLabel() {
        return this.image_label;
    }
}
