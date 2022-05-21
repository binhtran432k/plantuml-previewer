package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;

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

import lombok.Getter;

/**
 * View for Image Board
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class ImageBoardView implements IViewSubcriber {

    private final Cursor MOVING_CURSOR = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

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
        } else if (action == SubcribeAction.IMAGE || action == SubcribeAction.ZOOM) {
            reloadImage();
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

    private void reloadImage() {
        BufferedImage image = imageBoardModel.getImage();
        if (image == null) {
            return;
        }

        double zoom = imageBoardModel.getZoom();
        double foldZoom = imageBoardModel.getFoldZoom();
        ZoomAction action = imageBoardModel.getZoomAction();
        boolean isZoomIn = foldZoom > 1;
        boolean moveCenter = true;

        if (action == ZoomAction.BEST_FIT) {
            foldZoom = ImageHelperPlus.getBestFitZoom(image.getWidth(), image.getHeight(), imageBoard.getWidth(),
                    imageBoard.getHeight());
        } else if (action == ZoomAction.WIDTH_FIT) {
            foldZoom = ImageHelperPlus.getWidthFitZoom(image.getWidth(), imageBoard.getWidth());
        } else if (action == ZoomAction.IMAGE_SIZE) {
            foldZoom = 1;
        } else if (action == ZoomAction.ZOOMABLE) {
            if (zoom < 2.5) {
                foldZoom = zoom * foldZoom;
                if (isZoomIn) {
                    int width = imageBoardModel.getZoomedImage().getWidth();
                    int newWidth = (int) (foldZoom * image.getWidth());
                    while (newWidth <= width) {
                        foldZoom += 0.01;
                        newWidth = (int) (foldZoom * image.getWidth());
                    }
                }
            } else {
                image = imageBoardModel.getZoomedImage();
            }

            moveCenter = foldZoom * image.getWidth() <= imageBoard.getWidth();
        }
        if (image != null) {
            foldZoom = Math.max(0.01, foldZoom);

            image = ImageHelperPlus.getScaledImage(image, foldZoom);
            imageBoardModel.setZoomedImage(image);

            boolean isIconExist = imageWrapper.getIcon() != null;

            final ImageIcon icon = new ImageIcon(image);
            imageWrapper.setIcon(icon);
            imageBoard.revalidate();
            imageBoard.repaint();

            if (!isIconExist) {
                SwingUtilities.invokeLater(() -> {
                    moveScrollBarCenter();
                });
            } else if (moveCenter) {
                moveScrollBarCenter();
            } else {
                moveScrollBarCenterOfZoom(imageBoardModel.getFoldZoom());
            }

            imageBoardModel.setFoldZoom(1);
        }
    }

    private void updateScrollBarVisible() {
        boolean isViewScrollBar = imageBoardModel.isViewScrollBar();
        reloadScrollBarInstance(imageBoard.getHorizontalScrollBar(), isViewScrollBar);
        reloadScrollBarInstance(imageBoard.getVerticalScrollBar(), isViewScrollBar);
        imageBoard.revalidate();
        imageBoard.repaint();
    }

    private void moveScrollBarByDiff() {
        JScrollBar hScrollBar = imageBoard.getHorizontalScrollBar();
        int diffX = imageBoardModel.getDiffX();
        if (diffX == Integer.MIN_VALUE || diffX == Integer.MAX_VALUE) {
            hScrollBar.setValue(diffX);
        } else {
            hScrollBar.setValue(hScrollBar.getValue() - diffX);
        }
        JScrollBar vScrollBar = imageBoard.getVerticalScrollBar();
        int diffY = imageBoardModel.getDiffY();
        if (diffY == Integer.MIN_VALUE || diffY == Integer.MAX_VALUE) {
            vScrollBar.setValue(diffY);
        } else {
            vScrollBar.setValue(vScrollBar.getValue() - diffY);
        }

        imageBoardModel.setDiffX(0);
        imageBoardModel.setDiffY(0);

        updateModelCoordinate();
    }

    private void moveScrollBarCenter() {
        JScrollBar hScrollBar = imageBoard.getHorizontalScrollBar();
        JScrollBar vScrollBar = imageBoard.getVerticalScrollBar();

        int maxHorizontal = Math.max(hScrollBar.getMaximum() - imageBoard.getWidth(), 0);
        int maxVertical = Math.max(vScrollBar.getMaximum() - imageBoard.getHeight(), 0);

        hScrollBar.setValue(maxHorizontal / 2);
        vScrollBar.setValue(maxVertical / 2);

        updateModelCoordinate();
    }

    private void moveScrollBarCenterOfZoom(double foldZoom) {
        if (foldZoom == 1) {
            return;
        }

        JScrollBar hScrollBar = imageBoard.getHorizontalScrollBar();
        JScrollBar vScrollBar = imageBoard.getVerticalScrollBar();

        Point newPoint = ImageHelperPlus.getScaledPoint(hScrollBar.getValue(), vScrollBar.getValue(),
                imageBoard.getWidth(), imageBoard.getHeight(), foldZoom);

        hScrollBar.setValue((int) newPoint.getX());
        vScrollBar.setValue((int) newPoint.getY());

        updateModelCoordinate();
    }

    private void updateModelCoordinate() {
        JScrollBar hScrollBar = imageBoard.getHorizontalScrollBar();
        JScrollBar vScrollBar = imageBoard.getVerticalScrollBar();

        int maxHorizontal = Math.max(hScrollBar.getMaximum() - imageBoard.getWidth(), 0);
        int maxVertical = Math.max(vScrollBar.getMaximum() - imageBoard.getHeight(), 0);

        int x = 0;
        int y = 0;

        if (maxHorizontal > 0) {
            x = hScrollBar.getValue() * 100 / maxHorizontal;
        }
        if (maxVertical > 0) {
            y = vScrollBar.getValue() * 100 / maxVertical;
        }

        imageBoardModel.setCoordinateAndNotifyStatus(x, y);
    }

}
