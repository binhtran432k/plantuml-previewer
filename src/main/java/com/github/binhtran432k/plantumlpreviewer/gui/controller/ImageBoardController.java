package com.github.binhtran432k.plantumlpreviewer.gui.controller;

import java.awt.Point;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.core.PlantUmlImage;
import com.github.binhtran432k.plantumlpreviewer.core.PlantUmlLoader;
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

        final int diffX = dragPoint.x - cachedHoldPoint.x;
        final int diffY = dragPoint.y - cachedHoldPoint.y;

        cachedHoldPoint = dragPoint; // update hold point

        imageBoardModel.setDiffCoordinateWithNotify(diffX, diffY);
    }

    public void zoomImageIn() {
        imageBoardModel.setFoldZoom(Option.DEFAULT_ZOOM_IN_FOLD);
        imageBoardModel.setZoomActionAndNotify(ZoomAction.ZOOMABLE);
    }

    public void zoomImageOut() {
        imageBoardModel.setFoldZoom(Option.DEFAULT_ZOOM_OUT_FOLD);
        imageBoardModel.setZoomActionAndNotify(ZoomAction.ZOOMABLE);
    }

    public void zoomImageBestFit() {
        imageBoardModel.setZoomActionAndNotify(ZoomAction.BEST_FIT);
    }

    public void zoomImageWidthFit() {
        imageBoardModel.setZoomActionAndNotify(ZoomAction.WIDTH_FIT);
    }

    public void zoomImageImageSize() {
        imageBoardModel.setZoomActionAndNotify(ZoomAction.IMAGE_SIZE);
    }

    public void scrollImageLeft(int speedUpFactor) {
        imageBoardModel.setDiffCoordinateWithNotify(speedUpFactor * Option.INCREMENT_UNIT, 0);
    }

    public void scrollImageRight(int speedUpFactor) {
        imageBoardModel.setDiffCoordinateWithNotify(-speedUpFactor * Option.INCREMENT_UNIT, 0);
    }

    public void scrollImageUp(int speedUpFactor) {
        imageBoardModel.setDiffCoordinateWithNotify(0, speedUpFactor * Option.INCREMENT_UNIT);
    }

    public void scrollImageDown(int speedUpFactor) {
        imageBoardModel.setDiffCoordinateWithNotify(0, -speedUpFactor * Option.INCREMENT_UNIT);
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
        int index = imageBoardModel.getIndex() + 1;
        if (index >= imageBoardModel.getMaxImage()) {
            return;
        }
        imageBoardModel.setIndex(index);
        updateImage(true);
    }

    public void goPrevImage() {
        int index = imageBoardModel.getIndex() - 1;
        if (index < 0) {
            return;
        }
        imageBoardModel.setIndex(index);
        updateImage(true);
    }

    public void refreshImageZoom() {
        imageBoardModel.setZoomActionAndNotify(imageBoardModel.getZoomAction());
    }

    private void updateImage(boolean isBuffer) {
        statusBarController.setPendingStatus();
        new Thread(() -> {
            PlantUmlImage plantUmlImage = getPlantUmlImage(isBuffer);

            if (plantUmlImage == null) {
                statusBarController.setNoFileStatus();
                return;
            }

            imageBoardModel.setIndex(plantUmlImage.getIndex());
            imageBoardModel.setMaxImage(plantUmlImage.getMaxImage());
            imageBoardModel.setDescription(plantUmlImage.getDescription());
            imageBoardModel.setImagesAndNotify(plantUmlImage.getImage());
            statusBarController.setFileInfoStatus();
        }).start();
    }

    private PlantUmlImage getPlantUmlImage(boolean isBuffer) {
        if (isBuffer) {
            return plantUmlLoader.getImageFromBuffer(imageBoardModel.getIndex(), imageBoardModel.getPlantUmlFormat());
        }

        return plantUmlLoader.getImageFromFile(imageBoardModel.getFile(),
                imageBoardModel.getIndex(), imageBoardModel.getPlantUmlFormat());
    }

}
