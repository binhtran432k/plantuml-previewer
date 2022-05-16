package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.github.binhtran432k.plantumlpreviewer.gui.model.GuiModel;

/**
 * Image Panel of main window
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class NormalPanel extends JPanel {

    private StatusPanel statusPanel;
    private ImagePanel imagePanel;
    GuiModel model = GuiModel.getInstance();

    public NormalPanel() {
        imagePanel = new ImagePanel();
        statusPanel = new StatusPanel("");

        initUI();
    }

    public void setStatusImageLabel(String text) {
        statusPanel.setComponent(text);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        add(statusPanel, BorderLayout.SOUTH);
        add(imagePanel, BorderLayout.CENTER);
        model.setStatusConsumer((str) -> {
            statusPanel.setComponent(str);
        });

        model.updateImage();
    }

}
