package com.github.ydewolf.swing.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JavaSwingUtils {
    static final boolean DEBUG_MODE = false;
    static final Color DEBUG_COLOR = new Color(225, 52, 235);

    public static JPanel createPanel(int width, int height, int border_size) {
        JPanel panel = new JPanel();
        JavaSwingUtils.setupJPanel(panel, width, height, border_size);

        return panel;
    }

    public static JTextField createTextfieldRow(JPanel main_panel, String label_text, int width, int height, boolean editable) {
        JPanel row = new JPanel();
        main_panel.add(row);
        row.setLayout(new GridLayout(1, 2));
        JavaSwingUtils.setupJComponentDim(row, width, height);
        row.setPreferredSize(new Dimension(width, height));

        JLabel label = new JLabel(label_text);
        row.add(label);

        JTextField field = new JTextField();
        field.setEditable(false);
        
        row.add(field);
        return field;
    }

    public static JTextField createTextfieldRow(JPanel main_panel, String label_text, int width, int height) {
        return JavaSwingUtils.createTextfieldRow(main_panel, label_text, width, height, false);
    }

    public static void setupJPanel(JPanel panel, int width, int height, int border_size) {
        JavaSwingUtils.setupJComponentDim(panel, width, height);
        
        panel.setBorder(BorderFactory.createEmptyBorder(border_size, border_size, border_size, border_size));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        if (JavaSwingUtils.DEBUG_MODE) {
            Random rand = new Random();
            Color debug_color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
            // panel.setBackground(JavaSwingUtils.DEBUG_COLOR);
            panel.setBackground(debug_color);
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

        return text.substring(0, max_characters) + "...";
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

}
