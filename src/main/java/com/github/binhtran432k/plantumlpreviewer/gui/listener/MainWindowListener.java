package com.github.binhtran432k.plantumlpreviewer.gui.listener;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.github.binhtran432k.plantumlpreviewer.gui.controller.ApplicationController;

import lombok.RequiredArgsConstructor;

/**
 * Input listener for Main window
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@RequiredArgsConstructor
public class MainWindowListener implements KeyListener, ComponentListener {

    private final ApplicationController applicationController;

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        applicationController.doAction(ApplicationAction.REFRESH_ZOOM);
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
            if (e.isShiftDown()) {
                applicationController.doAction(ApplicationAction.ZOOM_IN);
            } else {
                applicationController.doAction(ApplicationAction.ZOOM_IMAGE_SIZE);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_PLUS) {
            applicationController.doAction(ApplicationAction.ZOOM_IN);
        } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            applicationController.doAction(ApplicationAction.ZOOM_OUT);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            applicationController.doAction(ApplicationAction.ZOOM_BEST_FIT);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            applicationController.doAction(ApplicationAction.ZOOM_WIDTH_FIT);
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            applicationController.doAction(ApplicationAction.QUIT);
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            applicationController.doAction(ApplicationAction.RELOAD_IMAGE);
        } else if (e.getKeyCode() == KeyEvent.VK_H || e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (e.isShiftDown()) {
                applicationController.doAction(ApplicationAction.MOVE_LEFT_PLUS);
            } else {
                applicationController.doAction(ApplicationAction.MOVE_LEFT);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_J || e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (e.isShiftDown()) {
                applicationController.doAction(ApplicationAction.MOVE_DOWN_PLUS);
            } else {
                applicationController.doAction(ApplicationAction.MOVE_DOWN);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_K || e.getKeyCode() == KeyEvent.VK_UP) {
            if (e.isShiftDown()) {
                applicationController.doAction(ApplicationAction.MOVE_UP_PLUS);
            } else {
                applicationController.doAction(ApplicationAction.MOVE_UP);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_L || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (e.isShiftDown()) {
                applicationController.doAction(ApplicationAction.MOVE_RIGHT_PLUS);
            } else {
                applicationController.doAction(ApplicationAction.MOVE_RIGHT);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_G) {
            if (e.isShiftDown()) {
                applicationController.doAction(ApplicationAction.MOVE_BOTTOM);
            } else {
                applicationController.doAction(ApplicationAction.MOVE_TOP);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_4 && e.isShiftDown()) {
            applicationController.doAction(ApplicationAction.MOVE_END);
        } else if (e.getKeyCode() == KeyEvent.VK_0) {
            applicationController.doAction(ApplicationAction.MOVE_BEGIN);
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            applicationController.doAction(ApplicationAction.TOGGLE_SCROLL_BAR);
        } else if (e.getKeyCode() == KeyEvent.VK_M) {
            applicationController.doAction(ApplicationAction.TOGGLE_MENU_BAR);
        } else if (e.getKeyCode() == KeyEvent.VK_N) {
            applicationController.doAction(ApplicationAction.GO_NEXT_IMAGE);
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            applicationController.doAction(ApplicationAction.GO_PREV_IMAGE);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
