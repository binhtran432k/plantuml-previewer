package com.github.binhtran432k.plantumlpreviewer.gui.controller;

import java.util.Arrays;

import javax.swing.JFrame;

/**
 * Controller for Window
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class WindowController {

    public void closeWindow() {
        Arrays.stream(JFrame.getFrames()).forEach(frame -> {
            frame.setVisible(false); // hide window
            frame.dispose(); // close frame
        });
        System.exit(0); // close completely
    }

}
