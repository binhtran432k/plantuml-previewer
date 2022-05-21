package com.github.binhtran432k.plantumlpreviewer.gui.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

/**
 * Anti alias version of {@link javax.swing.JLabel}
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class SmoothLabel extends JLabel {

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        super.paintComponent(g2d);
    }

}
