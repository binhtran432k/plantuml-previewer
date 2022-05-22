package com.github.binhtran432k.plantumlpreviewer.gui.controller;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.gui.listener.ApplicationAction;

import lombok.RequiredArgsConstructor;

/**
 * Controller for gui application
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@RequiredArgsConstructor
public class ApplicationController {

    private final WindowController windowController;
    private final ImageBoardController imageBoardController;
    private final MenuBarController menuBarController;

    public void doAction(ApplicationAction action) {
        if (action == null) {
            return;
        }

        int fastSpeed = Option.FAST_SPEED_FACTOR;

        switch (action) {
            case QUIT:
                windowController.closeWindow();
                break;
            case ZOOM_IMAGE_SIZE:
                imageBoardController.zoomImageImageSize();
                break;
            case ZOOM_WIDTH_FIT:
                imageBoardController.zoomImageWidthFit();
                break;
            case ZOOM_BEST_FIT:
                imageBoardController.zoomImageBestFit();
                break;
            case ZOOM_IN:
                imageBoardController.zoomImageIn();
                break;
            case ZOOM_OUT:
                imageBoardController.zoomImageOut();
                break;
            case TOGGLE_SCROLL_BAR:
                imageBoardController.toggleScrollBar();
                break;
            case TOGGLE_MENU_BAR:
                menuBarController.toggleMenuBar();
                break;
            case GO_NEXT_IMAGE:
                imageBoardController.goNextImage();
                break;
            case GO_PREV_IMAGE:
                imageBoardController.goPrevImage();
                break;
            case MOVE_UP:
                imageBoardController.scrollImageUp(1);
                break;
            case MOVE_RIGHT:
                imageBoardController.scrollImageRight(1);
                break;
            case MOVE_DOWN:
                imageBoardController.scrollImageDown(1);
                break;
            case MOVE_LEFT:
                imageBoardController.scrollImageLeft(1);
                break;
            case MOVE_UP_PLUS:
                imageBoardController.scrollImageUp(fastSpeed);
                break;
            case MOVE_RIGHT_PLUS:
                imageBoardController.scrollImageRight(fastSpeed);
                break;
            case MOVE_DOWN_PLUS:
                imageBoardController.scrollImageDown(fastSpeed);
                break;
            case MOVE_LEFT_PLUS:
                imageBoardController.scrollImageLeft(fastSpeed);
                break;
            case MOVE_TOP:
                imageBoardController.scrollImageTop();
                break;
            case MOVE_END:
                imageBoardController.scrollImageEnd();
                break;
            case MOVE_BOTTOM:
                imageBoardController.scrollImageBottom();
                break;
            case MOVE_BEGIN:
                imageBoardController.scrollImageBegin();
                break;
            case REFRESH_ZOOM:
                imageBoardController.refreshImageZoom();
                break;
            case RELOAD_IMAGE:
                imageBoardController.reloadImageFromFile();
                break;
            default:
                break;
        }
    }

}
