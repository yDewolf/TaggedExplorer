package com.github.ydewolf.classes.Files.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileUtils {
    public static boolean checkFileExtension(String filename, String[] valid_extensions, String[] excluded_extensions) {
        if (valid_extensions.length == 0 && excluded_extensions.length == 0) {
            return true;
        }

        for (String extension : excluded_extensions) {
            if (filename.endsWith(extension)) {
                return false;
            }
        }

        if (valid_extensions.length == 0) {
            return true;
        }

        for (String extension : valid_extensions) {
            if (filename.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkFileExtension(String filename, String[] valid_extensions) {
        return FileUtils.checkFileExtension(filename, valid_extensions, new String[0]);
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

