package com.github.binhtran432k.plantumlpreviewer.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.binhtran432k.plantumlpreviewer.gui.controller.ImageBoardController;
import com.github.binhtran432k.plantumlpreviewer.gui.controller.MenuBarController;
import com.github.binhtran432k.plantumlpreviewer.gui.controller.WindowController;

import lombok.RequiredArgsConstructor;

/**
 * Input listener for Menu bar
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@RequiredArgsConstructor
public class MenuBarListener implements ActionListener {
    private final MenuBarController menuBarController;
    private final ImageBoardController controller;
    private final WindowController windowController;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == MenuBarAction.QUIT.toString()) {
            windowController.closeWindow();
        } else if (e.getActionCommand() == MenuBarAction.ZOOM_IMAGE_SIZE.toString()) {
            controller.zoomImageImageSize();
        } else if (e.getActionCommand() == MenuBarAction.ZOOM_BEST_FIT.toString()) {
            controller.zoomImageBestFit();
        } else if (e.getActionCommand() == MenuBarAction.ZOOM_WIDTH_FIT.toString()) {
            controller.zoomImageWidthFit();
        } else if (e.getActionCommand() == MenuBarAction.ZOOM_IN.toString()) {
            controller.zoomImageIn();
        } else if (e.getActionCommand() == MenuBarAction.ZOOM_OUT.toString()) {
            controller.zoomImageOut();
        } else if (e.getActionCommand() == MenuBarAction.TOGGLE_MENU_BAR.toString()) {
            menuBarController.toggleMenuBar();
        } else if (e.getActionCommand() == MenuBarAction.TOGGLE_SCROLL_BAR.toString()) {
            controller.toggleScrollBar();
        }
    }

}
