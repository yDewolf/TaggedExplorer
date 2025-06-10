package com.github.ydewolf;

import com.github.ydewolf.classes.FileManager;

public class Main {
    public static void main(String[] args) {
        FileManager manager = new FileManager();
        
        System.out.println(manager.getFiles().toString());
    }   
}