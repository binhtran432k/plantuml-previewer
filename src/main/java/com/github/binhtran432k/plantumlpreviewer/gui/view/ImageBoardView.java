package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.gui.helper.ImageHelper;
import com.github.binhtran432k.plantumlpreviewer.gui.listener.ImageBoardListener;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ImageBoardModel;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ZoomAction;
import com.github.binhtran432k.plantumlpreviewer.gui.ui.ImagePanel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * View for Image Board
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class ImageBoardView implements IViewSubcriber {

    @Getter
    @AllArgsConstructor
    private class ImageSession {
        int x;
        int y;
        double scale;
    }

    private final Cursor MOVING_CURSOR = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    private final Map<Integer, ImageSession> cachedImageSessions = new HashMap<>();

    private @Getter JScrollPane imageBoard;
    private ImagePanel imagePanel;
    private ImageBoardModel imageBoardModel;

    public ImageBoardView(ImageBoardModel imageBoardModel, ImageBoardListener listener) {
        this.imagePanel = new ImagePanel();
        this.imageBoard = initImageBoard(this.imagePanel, listener);
        this.imageBoardModel = imageBoardModel;

        imageBoardModel.subcribe(SubcribeAction.CURSOR, this);
        imageBoardModel.subcribe(SubcribeAction.SCROLL_BAR, this);
        imageBoardModel.subcribe(SubcribeAction.ZOOM, this);
        imageBoardModel.subcribe(SubcribeAction.IMAGE, this);
        imageBoardModel.subcribe(SubcribeAction.DIFF_COORDINATE, this);
        imageBoardModel.subcribe(SubcribeAction.CLEAR_IMAGE_SESSION_CACHE, this);
    }

    @Override
    public void update(SubcribeAction action) {
        if (action == SubcribeAction.CURSOR) {
            if (imageBoardModel.isImageMoving()) {
                imageBoard.setCursor(MOVING_CURSOR);
            } else {
                imageBoard.setCursor(null);
            }
        } else if (action == SubcribeAction.SCROLL_BAR) {
            updateScrollBarVisible();
        } else if (action == SubcribeAction.DIFF_COORDINATE) {
            moveScrollBarByDiff();
        } else if (action == SubcribeAction.CLEAR_IMAGE_SESSION_CACHE) {
            cachedImageSessions.clear();
        } else if (action == SubcribeAction.IMAGE) {
            loadImage();
        } else if (action == SubcribeAction.ZOOM) {
            loadView(null);
        }
    }

    private JScrollPane initImageBoard(ImagePanel imageWrapper,
            ImageBoardListener listener) {
        JScrollPane panel = new JScrollPane();
        panel.setAutoscrolls(true);
        panel.setBorder(null);

        imageWrapper.setBackground(new Color(Option.PRIMARY_COLOR));
        panel.setViewportView(imageWrapper);

        // Remove all defalt mouse wheel
        Arrays.stream(panel.getMouseWheelListeners()).forEach(l -> panel.removeMouseWheelListener(l));

        panel.addMouseListener(listener);
        panel.addMouseMotionListener(listener);
        panel.addMouseWheelListener(listener);

        return panel;
    }

    private void reloadScrollBarInstance(JScrollBar scrollBar, boolean isViewScrollBar) {
        if (isViewScrollBar) {
            scrollBar.setPreferredSize(null);
        } else {
            scrollBar.setPreferredSize(new Dimension(0, 0));
        }
    }

    private void loadImage() {
        final ImageSession imageSession = cachedImageSessions.get(imageBoardModel.getIndex());
        double scale = 1;
        if (imageSession != null) {
            scale = imageSession.getScale();
        }

        imagePanel.setImageAndScale(imageBoardModel.getImage(), scale);

        SwingUtilities.invokeLater(() -> {
            loadView(imageSession);
        });
    }

    private void loadView(ImageSession imageSession) {
        BufferedImage image = imagePanel.getImage();
        if (image == null) {
            return;
        }

        final double scale = imagePanel.getScale();
        double foldScale = imageBoardModel.getFoldScale();
        double newScale = scale * foldScale;
        final ZoomAction action = imageBoardModel.getZoomAction();

        if (action == ZoomAction.BEST_FIT) {
            newScale = ImageHelper.getBestFitZoom(image.getWidth(), image.getHeight(),
                    imageBoard.getWidth(),
                    imageBoard.getHeight());
        } else if (action == ZoomAction.WIDTH_FIT) {
            newScale = ImageHelper.getWidthFitZoom(image.getWidth(), imageBoard.getWidth());
        } else if (action == ZoomAction.IMAGE_SIZE) {
            newScale = 1;
        }

        foldScale = newScale / scale;

        loadScaleAndCoordinate(imageSession, action, foldScale, newScale);
    }

    private void loadScaleAndCoordinate(final ImageSession imageSession, final ZoomAction action,
            final double foldScale, final double newScale) {
        imagePanel.setScale(newScale);
        imageBoardModel.setFoldScale(1);
        imageBoardModel.setScale(imagePanel.getScale()); // bridge scale

        if (action == ZoomAction.UNKOWN) {
            if (imageSession != null) {
                updateModelCoordinate(imageSession.getX(), imageSession.getY());
            } else {
                moveScrollBarCenter();
            }
        } else {
            moveScrollBarCenterOfZoom(foldScale);
        }
    }

    private void updateScrollBarVisible() {
        boolean isViewScrollBar = imageBoardModel.isViewScrollBar();
        reloadScrollBarInstance(imageBoard.getHorizontalScrollBar(), isViewScrollBar);
        reloadScrollBarInstance(imageBoard.getVerticalScrollBar(), isViewScrollBar);
        imageBoard.revalidate();
    }

    private void moveScrollBarByDiff() {
        JScrollBar hScrollBar = imageBoard.getHorizontalScrollBar();
        JScrollBar vScrollBar = imageBoard.getVerticalScrollBar();

        int x = addBound(hScrollBar.getValue(), imageBoardModel.getDiffX());
        int y = addBound(vScrollBar.getValue(), imageBoardModel.getDiffY());

        imageBoardModel.setDiffX(0);
        imageBoardModel.setDiffY(0);

        updateModelCoordinate(x, y);
    }

    private void moveScrollBarCenter() {
        JScrollBar hScrollBar = imageBoard.getHorizontalScrollBar();
        JScrollBar vScrollBar = imageBoard.getVerticalScrollBar();

        int maxHorizontal = Math.max(hScrollBar.getMaximum() - imageBoard.getWidth(), 0);
        int maxVertical = Math.max(vScrollBar.getMaximum() - imageBoard.getHeight(), 0);

        updateModelCoordinate(maxHorizontal / 2, maxVertical / 2);
    }

    private void moveScrollBarCenterOfZoom(double foldZoom) {
        JScrollBar hScrollBar = imageBoard.getHorizontalScrollBar();
        JScrollBar vScrollBar = imageBoard.getVerticalScrollBar();

        Point newPoint = ImageHelper.getScaledPoint(hScrollBar.getValue(), vScrollBar.getValue(),
                imageBoard.getWidth(), imageBoard.getHeight(), foldZoom);

        updateModelCoordinate(newPoint.x, newPoint.y);
    }

    private void updateModelCoordinate(int x, int y) {
        JScrollBar hScrollBar = imageBoard.getHorizontalScrollBar();
        JScrollBar vScrollBar = imageBoard.getVerticalScrollBar();

        hScrollBar.setValue(x);
        vScrollBar.setValue(y);
        int newX = hScrollBar.getValue();
        int newY = vScrollBar.getValue();

        int maxHorizontal = Math.max(hScrollBar.getMaximum() - imageBoard.getWidth(), 0);
        int maxVertical = Math.max(vScrollBar.getMaximum() - imageBoard.getHeight(), 0);

        int percentX = 0;
        int percentY = 0;

        if (maxHorizontal > 0) {
            percentX = newX * 100 / maxHorizontal;
        }
        if (maxVertical > 0) {
            percentY = newY * 100 / maxVertical;
        }

        imageBoardModel.setCoordinateAndNotifyStatus(percentX, percentY);

        cachedImageSessions.put(imageBoardModel.getIndex(),
                new ImageSession(newX, newY, imagePanel.getScale()));
    }

    private int addBound(int value, int diff) {
        if (diff == Integer.MAX_VALUE || diff == Integer.MIN_VALUE) {
            return diff;
        }

        return value + diff;
    }

}
