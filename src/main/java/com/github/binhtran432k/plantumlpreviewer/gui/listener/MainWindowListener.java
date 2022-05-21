package com.github.binhtran432k.plantumlpreviewer.gui.listener;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.gui.controller.ImageBoardController;
import com.github.binhtran432k.plantumlpreviewer.gui.controller.MenuBarController;
import com.github.binhtran432k.plantumlpreviewer.gui.controller.WindowController;

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

    private final WindowController windowController;
    private final ImageBoardController imageBoardController;
    private final MenuBarController menuBarController;

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        imageBoardController.refreshImageZoom();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int speedFactor = 1;
        if (e.isShiftDown()) {
            speedFactor = Option.FAST_SPEED_FACTOR;
        }

        if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
            if (e.isShiftDown()) {
                imageBoardController.zoomImageIn();
            } else {
                imageBoardController.zoomImageImageSize();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_PLUS) {
            imageBoardController.zoomImageIn();
        } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            imageBoardController.zoomImageOut();
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            imageBoardController.zoomImageBestFit();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            imageBoardController.zoomImageWidthFit();
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            windowController.closeWindow();
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            imageBoardController.reloadImageFromFile();
        } else if (e.getKeyCode() == KeyEvent.VK_H || e.getKeyCode() == KeyEvent.VK_LEFT) {
            imageBoardController.scrollImageLeft(speedFactor);
        } else if (e.getKeyCode() == KeyEvent.VK_J || e.getKeyCode() == KeyEvent.VK_DOWN) {
            imageBoardController.scrollImageDown(speedFactor);
        } else if (e.getKeyCode() == KeyEvent.VK_K || e.getKeyCode() == KeyEvent.VK_UP) {
            imageBoardController.scrollImageUp(speedFactor);
        } else if (e.getKeyCode() == KeyEvent.VK_L || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            imageBoardController.scrollImageRight(speedFactor);
        } else if (e.getKeyCode() == KeyEvent.VK_G) {
            if (e.isShiftDown()) {
                imageBoardController.scrollImageBottom();
            } else {
                imageBoardController.scrollImageTop();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_4 && e.isShiftDown()) {
            imageBoardController.scrollImageEnd();
        } else if (e.getKeyCode() == KeyEvent.VK_0) {
            imageBoardController.scrollImageBegin();
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            imageBoardController.toggleScrollBar();
        } else if (e.getKeyCode() == KeyEvent.VK_M) {
            menuBarController.toggleMenuBar();
        } else if (e.getKeyCode() == KeyEvent.VK_N) {
            imageBoardController.goNextPage();
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            imageBoardController.goPrevPage();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
