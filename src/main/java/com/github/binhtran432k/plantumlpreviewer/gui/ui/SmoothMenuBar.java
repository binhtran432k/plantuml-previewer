package com.github.binhtran432k.plantumlpreviewer.gui.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Anti alias version of {@link javax.swing.JMenuBar}
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class SmoothMenuBar extends JMenuBar {

    public static class SmoothMenu extends JMenu {
        public SmoothMenu(String name) {
            super(name);
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            super.paintComponent(g2d);
        }
    }

    public static class SmoothMenuItem extends JMenuItem {
        public SmoothMenuItem(String name) {
            super(name);
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            super.paintComponent(g2d);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        super.paintComponent(g2d);
    }

}
