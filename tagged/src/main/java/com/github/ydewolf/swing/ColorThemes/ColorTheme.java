package com.github.ydewolf.swing.ColorThemes;

import java.awt.Color;
import java.awt.Component;

public abstract class ColorTheme {
    public final Color FOREGROUND_COLOR = null;
    public final Color BACKGROUND_COLOR = null;

    public final Color CONTAINER_BACKGROUND_COLOR = Color.MAGENTA;

    public void applyToComponent(Component component) {
        component.setBackground(BACKGROUND_COLOR);
        component.setForeground(FOREGROUND_COLOR);
    }
}
