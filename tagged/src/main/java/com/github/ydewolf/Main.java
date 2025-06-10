package com.github.ydewolf;

import com.github.ydewolf.classes.FileManager;

public class Main {
    public static void main(String[] args) {
        FileManager manager = new FileManager("C:\\dev\\Andre2DS\\Tagged\\tagged\\src\\main\\java\\com\\github\\ydewolf\\classes\\Files");
        
        for (String path : manager.getFilePaths()) {
            System.out.println(path);
        }
    }   
}