package com.github.ydewolf.swing.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JavaSwingUtils {
    static final boolean DEBUG_MODE = false;
    static final Color DEBUG_COLOR = new Color(225, 52, 235);

    public static JPanel createPanel(int width, int height, int border_size) {
        JPanel panel = new JPanel();
        JavaSwingUtils.setupJPanel(panel, width, height, border_size);

        return panel;
    }

    public static void setupJPanel(JPanel panel, int width, int height, int border_size) {
        JavaSwingUtils.setupJComponentDim(panel, width, height);
        
        panel.setBorder(BorderFactory.createEmptyBorder(border_size, border_size, border_size, border_size));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        if (JavaSwingUtils.DEBUG_MODE) {
            panel.setBackground(JavaSwingUtils.DEBUG_COLOR);
        }
    }

    public static void setupJComponentDim(JComponent component, int width, int height) {
        Dimension dim = new Dimension(width, height);
        component.setSize(dim);
        component.setBounds(0, 0, width, height);
        component.setMaximumSize(dim);
    }

    public static String truncateString(JLabel label, String text) {
        int max_characters = JavaSwingUtils.getMaxCharacters(label);
        
        return JavaSwingUtils.truncateString(label, text, max_characters) + "...";
    }

    public static String truncateString(JLabel label, String text, int max_characters) {
        if (text.length() < max_characters) {
            return text;
        }

        return text.substring(0, max_characters);
    }

    // Returns the maximum amount of characters a JLabel can support
    public static int getMaxCharacters(JLabel label, String ellipsis) {
        FontMetrics fm = label.getFontMetrics(label.getFont());
        
        int ellipsis_width = fm.stringWidth(ellipsis);
        int usable_width = label.getPreferredSize().width - ellipsis_width;

        int avg_width = fm.stringWidth(" ");
        int max_characters = (int) Math.ceil(usable_width / avg_width) + 1;
        
        return max_characters;
    }

    public static int getMaxCharacters(JLabel label) {
        return JavaSwingUtils.getMaxCharacters(label, "...");
    }

    public static BufferedImage cropImage(File file, int target_width, int target_height) throws IOException {
        BufferedImage original = ImageIO.read(file);

        double scale = Math.max(
            (double) target_width / original.getWidth(),
            (double) target_height / original.getHeight()
        );

        int scaled_width = (int) Math.ceil(original.getWidth() * scale);
        int scaled_height = (int) Math.ceil(original.getHeight() * scale);

        BufferedImage scaled = new BufferedImage(scaled_width, scaled_height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dScale = scaled.createGraphics();
        g2dScale.drawImage(original.getScaledInstance(scaled_width, scaled_height, Image.SCALE_FAST), 0, 0, null);
        g2dScale.dispose();

        int x = Math.max(((scaled.getWidth() - target_width) / 2), 0);
        int y = Math.max(((scaled.getHeight() - target_height) / 2), 0);
        
        return scaled.getSubimage(x, y, target_width, target_height);
    }
}
