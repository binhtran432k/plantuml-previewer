package com.github.binhtran432k.plantumlpreviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Image Panel of main window
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class ImagePanel extends JPanel {

    private class StatusPanel extends JPanel {
        private final int STATUS_SIZE = 23;
        private final float FONT_SIZE = 14;

        private JLabel component;

        public StatusPanel(String componentStr) {
            component = new JLabel(componentStr);
            component.setFont(component.getFont().deriveFont(FONT_SIZE));

            initUI();
        }

        public void setComponent(String text) {
            component.setText(text);
        }

        private void initUI() {
            setBackground(new Color(0xc1c1c1));
            setPreferredSize(new Dimension(getWidth(), STATUS_SIZE));
            setLayout(new BorderLayout());

            add(component);
        }
    }

    private int currentPage = 1;
    private int maxPage = 1;
    private int zoom = 100;

    private StatusPanel statusPanel;

    public ImagePanel() {
        statusPanel = new StatusPanel(generateStatusImageLabel());

        initUI();
    }

    public void setStatusImageLabel(String text) {
        statusPanel.setComponent(text);
    }

    private String generateStatusImageLabel() {
        String pageNumber = String.format("%d/%d", currentPage, maxPage);
        String imageName = "example.png";
        String zoomRate = String.format("[%d%%]", zoom);
        String padding = "   ";

        String label = String.format("  %s%s%s%s%s  ", pageNumber, padding, imageName, padding, zoomRate);

        return label;
    }

    private void initUI() {
        setBackground(new Color(0xe5e5e5));
        setLayout(new BorderLayout());

        add(statusPanel, BorderLayout.SOUTH);
    }

}
