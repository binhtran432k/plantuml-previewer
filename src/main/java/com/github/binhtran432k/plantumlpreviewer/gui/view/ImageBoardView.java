package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.gui.helper.ImageHelperPlus;
import com.github.binhtran432k.plantumlpreviewer.gui.listener.ImageBoardListener;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ImageBoardModel;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ZoomAction;
import com.github.binhtran432k.plantumlpreviewer.gui.ui.SmoothLabel;

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
        double zoom;
    }

    private final Cursor MOVING_CURSOR = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    private final Map<Integer, ImageSession> cachedImageSessions = new HashMap<>();

    private @Getter JScrollPane imageBoard;
    private SmoothLabel imageWrapper;
    private ImageBoardModel imageBoardModel;

    public ImageBoardView(ImageBoardModel imageBoardModel, ImageBoardListener listener) {
        this.imageWrapper = initImageWrapper();
        this.imageBoard = initImageBoard(this.imageWrapper, listener);
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
        } else if (action == SubcribeAction.IMAGE || action == SubcribeAction.ZOOM) {
            loadView();
        }
    }

    private JScrollPane initImageBoard(JLabel imageWrapper,
            ImageBoardListener listener) {
        JScrollPane panel = new JScrollPane();
        panel.setAutoscrolls(true);
        panel.setBorder(null);

        JPanel imageView = new JPanel(new GridBagLayout());
        imageView.setBackground(new Color(Option.PRIMARY_COLOR));

        imageView.add(imageWrapper);

        panel.setViewportView(imageView);

        // Remove all defalt mouse wheel
        Arrays.stream(panel.getMouseWheelListeners()).forEach(l -> panel.removeMouseWheelListener(l));

        panel.addMouseListener(listener);
        panel.addMouseMotionListener(listener);
        panel.addMouseWheelListener(listener);

        return panel;
    }

    private SmoothLabel initImageWrapper() {
        SmoothLabel imageWrapper = new SmoothLabel();
        imageWrapper.setBorder(new EmptyBorder(Option.BORDER_SIZE, Option.BORDER_SIZE,
                Option.BORDER_SIZE, Option.BORDER_SIZE));

        return imageWrapper;
    }

    private void reloadScrollBarInstance(JScrollBar scrollBar, boolean isViewScrollBar) {
        if (isViewScrollBar) {
            scrollBar.setPreferredSize(null);
        } else {
            scrollBar.setPreferredSize(new Dimension(0, 0));
        }
    }

    private void loadView() {
        BufferedImage image = imageBoardModel.getImage();
        if (image == null) {
            imageWrapper.setIcon(null);
            return;
        }

        double zoom = imageBoardModel.getZoom();
        double foldZoom = imageBoardModel.getFoldZoom();
        double newZoom = zoom * foldZoom;
        final ZoomAction action = imageBoardModel.getZoomAction();
        boolean isZoomIn = foldZoom > 1;

        int width = image.getWidth();
        int height = image.getHeight();
        final double minZoom = Math.min(1, Option.MIN_PIXEL / (double) Math.min(width, height));
        final double maxZoom = Math.max(1, Option.MAX_PIXEL / (double) Math.max(width, height));

        if (action == ZoomAction.BEST_FIT) {
            newZoom = ImageHelperPlus.getBestFitZoom(image.getWidth(), image.getHeight(),
                    imageBoard.getWidth(),
                    imageBoard.getHeight());
        } else if (action == ZoomAction.WIDTH_FIT) {
            newZoom = ImageHelperPlus.getWidthFitZoom(image.getWidth(), imageBoard.getWidth());
        } else if (action == ZoomAction.IMAGE_SIZE) {
            newZoom = 1;
        } else if (action == ZoomAction.ZOOMABLE) {
            if (isZoomIn) {
                int zoomedWidth = imageBoardModel.getZoomedImage().getWidth();
                int newWidth = (int) (newZoom * width);
                if (newWidth <= zoomedWidth) {
                    newZoom = (zoomedWidth + 1.1) / (double) width;
                }
            }
        }

        final ImageSession imageSession = cachedImageSessions.get(imageBoardModel.getIndex());

        if (action == ZoomAction.UNKOWN && imageSession != null) {
            newZoom = imageSession.getZoom();
        }

        newZoom = Math.max(minZoom, Math.min(maxZoom, newZoom));
        foldZoom = Math.round(newZoom * 100 / zoom) / (double) 100;

        image = ImageHelperPlus.getScaledImage(image, newZoom);
        loadImage(image, imageSession, action, foldZoom, newZoom);
    }

    private void loadImage(final BufferedImage image, final ImageSession imageSession, final ZoomAction action,
            final double foldZoom, final double newZoom) {
        imageBoardModel.setZoomedImage(image);
        final ImageIcon icon = new ImageIcon(image);

        imageWrapper.setIcon(icon);
        imageBoard.repaint();

        if (action == ZoomAction.UNKOWN) {
            SwingUtilities.invokeLater(() -> {
                if (imageSession != null) {
                    updateModelCoordinate(imageSession.getX(), imageSession.getY());
                } else {
                    moveScrollBarCenter();
                }
            });
        } else {
            moveScrollBarCenterOfZoom(foldZoom);
        }

        imageBoardModel.setFoldZoom(1);
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

        Point newPoint = ImageHelperPlus.getScaledPoint(hScrollBar.getValue(), vScrollBar.getValue(),
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
                new ImageSession(newX, newY, imageBoardModel.getZoom()));
    }

    private int addBound(int value, int diff) {
        if (diff == Integer.MAX_VALUE || diff == Integer.MIN_VALUE) {
            return diff;
        }

        return value + diff;
    }

}
