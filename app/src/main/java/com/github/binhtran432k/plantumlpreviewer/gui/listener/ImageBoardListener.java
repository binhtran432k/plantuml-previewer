package com.github.binhtran432k.plantumlpreviewer.gui.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import com.github.binhtran432k.plantumlpreviewer.gui.controller.ImageBoardController;

import lombok.RequiredArgsConstructor;

/**
 * Input listener for image panel
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@RequiredArgsConstructor
public class ImageBoardListener implements MouseInputListener, MouseWheelListener {

    private final ImageBoardController controller;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.pressCursor(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.resetCursor();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.dragCursor(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        boolean isWheelUp = e.getWheelRotation() < 0;
        boolean isZoomingOrScrolling = e.isControlDown();

        if (isZoomingOrScrolling) {
            if (isWheelUp) {
                controller.zoomImageIn();
            } else {
                controller.zoomImageOut();
            }
            return;
        }

        boolean isScrollHorizontal = e.isShiftDown();

        if (isScrollHorizontal) {
            if (isWheelUp) {
                controller.scrollImageLeft(1);
            } else {
                controller.scrollImageRight(1);
            }
        } else {
            if (isWheelUp) {
                controller.scrollImageUp(1);
            } else {
                controller.scrollImageDown(1);
            }
        }
    }

}
