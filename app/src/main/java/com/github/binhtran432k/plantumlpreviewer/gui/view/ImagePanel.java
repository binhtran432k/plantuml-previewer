package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.github.binhtran432k.plantumlpreviewer.gui.model.GuiModel;

class ImagePanel extends JScrollPane {

    ImagePanelListener listener = new ImagePanelListener();
    GuiModel model = GuiModel.getInstance();

    public ImagePanel() {
        initUI();
    }

    public void initUI() {
        setAutoscrolls(true);
        setOpaque(false);
        setBorder(null);

        setViewport(model.getViewport());
        setViewportView(model.getImageView());

        // Remove all defalt mouse wheel
        Arrays.stream(getMouseWheelListeners()).forEach(l -> removeMouseWheelListener(l));

        addMouseListener(listener);
        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
        addComponentListener(listener);
        setFocusable(true);
        addKeyListener(listener);
        updateScrollBar();

        model.setCursorConsumer((cursor) -> {
            setCursor(cursor);
            updateScrollBar();
        });

    }

    public void updateScrollBar() {
        reloadScrollBarInstance(getHorizontalScrollBar());
        reloadScrollBarInstance(getVerticalScrollBar());
    }

    private void reloadScrollBarInstance(JScrollBar scrollBar) {
        if (model.isViewScroll()) {
            scrollBar.setPreferredSize(null);
        } else {
            scrollBar.setPreferredSize(new Dimension());
        }
    }
}
