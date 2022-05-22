package com.github.binhtran432k.plantumlpreviewer.gui.controller;

import java.awt.Point;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.core.PlantUmlImage;
import com.github.binhtran432k.plantumlpreviewer.core.PlantUmlLoader;
import com.github.binhtran432k.plantumlpreviewer.gui.FileWatcher;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ImageBoardModel;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ZoomAction;

import lombok.RequiredArgsConstructor;

/**
 * Controller for Image Panel
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@RequiredArgsConstructor
public class ImageBoardController {
    private final PlantUmlLoader plantUmlLoader = new PlantUmlLoader();
    private final ImageBoardModel imageBoardModel;
    private final StatusBarController statusBarController;

    private Point cachedHoldPoint;

    public void reloadImageFromFileOnChanged() {
        FileWatcher fileWatcher = imageBoardModel.getFileWatcher();
        if (fileWatcher.hasChanged()) {
            updateImage(false);
            fileWatcher.refreshModified();
        }
    }

    public void reloadImageFromFile() {
        updateImage(false);
    }

    public void reloadImageFromBuffer() {
        updateImage(true);
    }

    public void pressCursor(Point cursorPoint) {
        imageBoardModel.setImageMovingAndNotify(true);
        cachedHoldPoint = cursorPoint;
    }

    public void resetCursor() {
        imageBoardModel.setImageMovingAndNotify(false);
        cachedHoldPoint = null;
    }

    public void dragCursor(Point dragPoint) {
        if (cachedHoldPoint == null) {
            return;
        }

        final int diffX = cachedHoldPoint.x - dragPoint.x;
        final int diffY = cachedHoldPoint.y - dragPoint.y;

        cachedHoldPoint = dragPoint; // update hold point

        imageBoardModel.setDiffCoordinateWithNotify(diffX, diffY);
    }

    public void zoomImageIn() {
        if (imageBoardModel.getZoom() >= Option.MAX_ZOOM) {
            return;
        }
        imageBoardModel.setFoldZoom(Option.FOLD_ZOOM_IN);
        updateZoom(ZoomAction.ZOOMABLE);
    }

    public void zoomImageOut() {
        if (imageBoardModel.getZoom() <= Option.MIN_ZOOM) {
            return;
        }
        imageBoardModel.setFoldZoom(1 / Option.FOLD_ZOOM_IN);
        updateZoom(ZoomAction.ZOOMABLE);
    }

    public void zoomImageBestFit() {
        updateZoom(ZoomAction.BEST_FIT);
    }

    public void zoomImageWidthFit() {
        updateZoom(ZoomAction.WIDTH_FIT);
    }

    public void zoomImageImageSize() {
        updateZoom(ZoomAction.IMAGE_SIZE);
    }

    public void scrollImageLeft(int speedUpFactor) {
        imageBoardModel.setDiffCoordinateWithNotify(speedUpFactor * -Option.INCREMENT_UNIT, 0);
    }

    public void scrollImageRight(int speedUpFactor) {
        imageBoardModel.setDiffCoordinateWithNotify(speedUpFactor * Option.INCREMENT_UNIT, 0);
    }

    public void scrollImageUp(int speedUpFactor) {
        imageBoardModel.setDiffCoordinateWithNotify(0, speedUpFactor * -Option.INCREMENT_UNIT);
    }

    public void scrollImageDown(int speedUpFactor) {
        imageBoardModel.setDiffCoordinateWithNotify(0, speedUpFactor * Option.INCREMENT_UNIT);
    }

    public void scrollImageTop() {
        imageBoardModel.setDiffCoordinateWithNotify(0, Integer.MIN_VALUE);
    }

    public void scrollImageBottom() {
        imageBoardModel.setDiffCoordinateWithNotify(0, Integer.MAX_VALUE);
    }

    public void scrollImageBegin() {
        imageBoardModel.setDiffCoordinateWithNotify(Integer.MIN_VALUE, 0);
    }

    public void scrollImageEnd() {
        imageBoardModel.setDiffCoordinateWithNotify(Integer.MAX_VALUE, 0);
    }

    public void toggleScrollBar() {
        imageBoardModel.setViewScrollBarAndNotify(!imageBoardModel.isViewScrollBar());
    }

    public void goNextImage() {
        int nextIndex = imageBoardModel.getIndex() + 1;
        if (nextIndex >= imageBoardModel.getMaxImage()) {
            return;
        }
        imageBoardModel.setIndex(nextIndex);
        updateImage(true);
    }

    public void goPrevImage() {
        int prevIndex = imageBoardModel.getIndex() - 1;
        if (prevIndex < 0) {
            return;
        }
        imageBoardModel.setIndex(prevIndex);
        updateImage(true);
    }

    public void refreshImageZoom() {
        imageBoardModel.setZoomActionAndNotify(imageBoardModel.getZoomAction());
    }

    private void updateImage(boolean isBuffer) {
        statusBarController.setPendingStatus();
        new Thread(() -> {
            if (imageBoardModel.getZoomAction() == ZoomAction.ZOOMABLE) {
                imageBoardModel.setZoomAction(ZoomAction.UNKOWN);
            }
            PlantUmlImage plantUmlImage = getPlantUmlImage(isBuffer);

            if (plantUmlImage != null) {
                imageBoardModel.setIndex(plantUmlImage.getIndex());
                imageBoardModel.setMaxImage(plantUmlImage.getMaxImage());
                imageBoardModel.setDescription(plantUmlImage.getDescription());
                imageBoardModel.setImagesAndNotify(plantUmlImage.getImage());
            } else {
                imageBoardModel.setIndex(0);
                imageBoardModel.setMaxImage(0);
                imageBoardModel.setDescription("");
                imageBoardModel.setImagesAndNotify(null);
            }

            statusBarController.setPreviewImageStatus();
        }).start();
    }

    private void updateZoom(ZoomAction zoomAction) {
        statusBarController.setZoomingStatus();
        imageBoardModel.setZoomActionAndNotify(zoomAction);
        statusBarController.setPreviewImageStatus();
    }

    private PlantUmlImage getPlantUmlImage(boolean isBuffer) {
        if (isBuffer) {
            return plantUmlLoader.getImageFromBuffer(imageBoardModel.getIndex(), imageBoardModel.getPlantUmlFormat());
        }

        return plantUmlLoader.getImageFromFile(imageBoardModel.getFileWatcher().getFile(),
                imageBoardModel.getIndex(), imageBoardModel.getPlantUmlFormat());
    }

}
