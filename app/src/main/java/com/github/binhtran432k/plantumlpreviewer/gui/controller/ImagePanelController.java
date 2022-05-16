package com.github.binhtran432k.plantumlpreviewer.gui.controller;

import java.awt.*;

import javax.swing.JViewport;

import com.github.binhtran432k.plantumlpreviewer.gui.model.GuiModel;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ScrollSizeType;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ZoomAction;

public class ImagePanelController {
    GuiModel model = GuiModel.getInstance();

    public ImagePanelController() {
    }

    public void pressCursor(Point cursorPoint) {
        model.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        model.setHoldPoint(cursorPoint);
    }

    public void resetCursor() {
        model.setCursor(null);
        model.setHoldPoint(null);
    }

    public void dragCursor(Point cursorPoint) {
        Point holdPoint = model.getHoldPoint();
        JViewport viewport = model.getViewport();

        if (holdPoint == null || viewport == null || !model.isScrollable()) {
            return;
        }

        Point dragPoint = cursorPoint;
        Point viewPoint = viewport.getViewPosition();
        viewPoint.x -= dragPoint.x - holdPoint.x;
        viewPoint.y -= dragPoint.y - holdPoint.y;
        model.setHoldPoint(dragPoint);

        model.updateToVisiblePosition(viewPoint);
    }

    public void zoomInImage() {
        model.setZoomAction(ZoomAction.ZOOMABLE);
        model.zoomImage(true);
    }

    public void zoomOutImage() {
        model.setZoomAction(ZoomAction.ZOOMABLE);
        model.zoomImage(false);
    }

    public void zoomBestFitImage() {
        model.setZoomAction(ZoomAction.BEST_FIT);
        refreshImageZoom();
    }

    public void zoomWidthFitImage() {
        model.setZoomAction(ZoomAction.WIDTH_FIT);
        refreshImageZoom();
    }

    public void zoomFitImage() {
        model.setZoomAction(ZoomAction.FIT);
        refreshImageZoom();
    }

    public void scrollLeftHorizontal() {
        if (model.isScrollableHorizontal()) {
            model.scrollImage(true, true);
        }
    }

    public void scrollRightHorizontal() {
        if (model.isScrollableHorizontal()) {
            model.scrollImage(true, false);
        }
    }

    public void scrollUpVertical() {
        if (model.isScrollableVertical()) {
            model.scrollImage(false, true);
        }
    }

    public void scrollDownVertical() {
        if (model.isScrollableVertical()) {
            model.scrollImage(false, false);
        }
    }

    public void scrollToTop() {
        if (model.isScrollableVertical()) {
            model.scrollToSize(ScrollSizeType.TOP);
        }
    }

    public void scrollToRight() {
        if (model.isScrollableHorizontal()) {
            model.scrollToSize(ScrollSizeType.RIGHT);
        }
    }

    public void scrollToBottom() {
        if (model.isScrollableVertical()) {
            model.scrollToSize(ScrollSizeType.BOTTOM);
        }
    }

    public void scrollToLeft() {
        if (model.isScrollableHorizontal()) {
            model.scrollToSize(ScrollSizeType.LEFT);
        }
    }

    public void toggleScrollBar() {
        model.setViewScroll(!model.isViewScroll());
        model.refreshImage();
    }

    public void goNextPage() {
        model.updateAdditionIndex(1);
    }

    public void goPrevPage() {
        model.updateAdditionIndex(-1);
    }

    public void refreshImageZoom() {
        model.refreshZoom();
        model.refreshImage();
    }

}
