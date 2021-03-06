package com.github.binhtran432k.plantumlpreviewer.gui.model;

import java.awt.image.BufferedImage;
import java.io.File;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.core.PlantUmlFormat;
import com.github.binhtran432k.plantumlpreviewer.gui.FileWatcher;
import com.github.binhtran432k.plantumlpreviewer.gui.view.SubcribeAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for image panel
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@Getter
@Setter
public class ImageBoardModel extends ModelPublisher {

    private final FileWatcher fileWatcher = new FileWatcher();
    private boolean isImageMoving;
    private boolean isViewScrollBar;
    private ZoomAction zoomAction;
    private int index;
    private int maxImage = 0;
    private BufferedImage image;
    private String description = "";
    private PlantUmlFormat plantUmlFormat;
    private double scale = 1;
    private int x = 50;
    private int y = 50;
    private int diffX = 0;
    private int diffY = 0;
    private double foldScale = 1;

    public ImageBoardModel(Option option) {
        this.index = option.getIndex();
        this.isViewScrollBar = option.isViewScrollBar();
        this.zoomAction = option.getZoomAction();
    }

    public void setDiffCoordinateWithNotify(int diffX, int diffY) {
        if (diffX == 0 && diffY == 0) {
            setDiffX(0);
            setDiffY(0);
            return;
        }
        setDiffX(diffX);
        setDiffY(diffY);
        notifySubcribers(SubcribeAction.DIFF_COORDINATE);
    }

    public void setZoomActionAndNotify(ZoomAction zoomAction) {
        setZoomAction(zoomAction);
        notifySubcribers(SubcribeAction.ZOOM);
    }

    public void setImageAndNotify(BufferedImage image) {
        setImage(image);
        notifySubcribers(SubcribeAction.IMAGE);
    }

    public void setFileAndResetAndNotify(File file) {
        setIndex(0);
        this.fileWatcher.setFileAndReset(file);
        notifySubcribers(SubcribeAction.CLEAR_IMAGE_SESSION_CACHE);
        notifySubcribers(SubcribeAction.STATUS_BAR);
    }

    public void setImageMovingAndNotify(boolean isImageMoving) {
        setImageMoving(isImageMoving);
        notifySubcribers(SubcribeAction.CURSOR);
    }

    public void setViewScrollBarAndNotify(boolean isViewScrollBar) {
        setViewScrollBar(isViewScrollBar);
        notifySubcribers(SubcribeAction.SCROLL_BAR);
    }

    public void setCoordinateAndNotifyStatus(int x, int y) {
        setX(x);
        setY(y);
        notifySubcribers(SubcribeAction.STATUS_BAR);
    }

}
