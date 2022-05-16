package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import com.github.binhtran432k.plantumlpreviewer.gui.controller.ImagePanelController;

public class ImagePanelListener implements MouseInputListener, MouseWheelListener, ComponentListener, KeyListener {

    ImagePanelController controller = new ImagePanelController();

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
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) { // Wheel UP
                controller.zoomInImage();
            } else {
                controller.zoomOutImage();
            }
        } else if (e.isShiftDown()) {
            if (e.getWheelRotation() < 0) { // Wheel UP
                controller.scrollLeftHorizontal();
            } else {
                controller.scrollRightHorizontal();
            }
        } else {
            if (e.getWheelRotation() < 0) { // Wheel UP
                controller.scrollUpVertical();
            } else {
                controller.scrollDownVertical();
            }
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        controller.refreshImageZoom();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_EQUALS && e.isShiftDown()) {
            controller.zoomInImage();
        } else if (e.getKeyCode() == KeyEvent.VK_PLUS) {
            controller.zoomInImage();
        } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            controller.zoomOutImage();
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            controller.zoomBestFitImage();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            controller.zoomWidthFitImage();
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_H) {
            controller.scrollLeftHorizontal();
        } else if (e.getKeyCode() == KeyEvent.VK_J) {
            controller.scrollDownVertical();
        } else if (e.getKeyCode() == KeyEvent.VK_K) {
            controller.scrollUpVertical();
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            controller.scrollRightHorizontal();
        } else if (e.getKeyCode() == KeyEvent.VK_G) {
            if (e.isShiftDown()) {
                controller.scrollToBottom();
            } else {
                controller.scrollToTop();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_4 && e.isShiftDown()) {
            controller.scrollToRight();
        } else if (e.getKeyCode() == KeyEvent.VK_0) {
            controller.scrollToLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            controller.toggleScrollBar();
        } else if (e.getKeyCode() == KeyEvent.VK_N) {
            controller.goNextPage();
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            controller.goPrevPage();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
