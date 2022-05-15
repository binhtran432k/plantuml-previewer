package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

class ImagePanel extends JScrollPane implements MouseInputListener, MouseWheelListener, ComponentListener {
    private final int ZOOM_UNIT = 10;
    private final int INCREMENT_UNIT = 30;

    private int maxPage = 1;
    private int zoom = 100;
    private boolean isBestFit = false;
    private boolean isWidthFit = false;
    private JPanel imageView = new JPanel(new GridBagLayout());
    private JLabel imageWrapper = new JLabel();
    private Image image;
    private Point holdPoint;

    public ImagePanel() {
        initUI();
    }

    public int getZoom() {
        return zoom;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void updateImage(File file, int index, Consumer<String> callback) {
        new Thread(() -> {
            try {
                ByteArrayOutputStream png = new ByteArrayOutputStream();
                String source = Files.readString(file.toPath());

                SourceStringReader reader = new SourceStringReader(source);

                DiagramDescription description = reader.outputImage(png, index,
                        new FileFormatOption(FileFormat.PNG));

                maxPage = calNumOfImages(reader);

                callback.accept(description.toString());

                image = ImageIO.read(new ByteArrayInputStream(png.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            refreshImage();
        }).start();
    }

    public int calNumOfImages(SourceStringReader reader) {
        return reader.getBlocks().stream().mapToInt(b -> b.getDiagram().getNbImages()).sum();
    }

    public void initUI() {
        setAutoscrolls(true);
        setOpaque(false);
        initScrollBar(getHorizontalScrollBar());

        initScrollBar(getVerticalScrollBar());
        setBorder(null);

        imageWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        imageView.setBackground(new Color(0xe5e5e5));
        imageView.add(imageWrapper);
        setViewportView(imageView);

        // Remove all defalt mouse wheel
        Arrays.stream(getMouseWheelListeners()).forEach(l -> removeMouseWheelListener(l));

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addComponentListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        holdPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setCursor(null);

        holdPoint = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (holdPoint == null || viewport == null || !isScrollable()) {
            return;
        }
        Point dragPoint = e.getPoint();
        Point viewPoint = viewport.getViewPosition();
        viewPoint.x -= dragPoint.x - holdPoint.x;
        viewPoint.y -= dragPoint.y - holdPoint.y;
        holdPoint = dragPoint;

        viewport.setViewPosition(getValidPoint(viewPoint));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) { // Wheel UP
                refreshZoom(false);
                zoom += ZOOM_UNIT;
            } else {
                refreshZoom(true);
                zoom -= ZOOM_UNIT;
            }

            if (zoom < 20) {
                zoom = 20;
            }

            refreshImage();
        } else if (e.isShiftDown()) {
            if (!isScrollable()) {
                return;
            }
            Point viewPoint = viewport.getViewPosition();
            if (e.getWheelRotation() < 0) { // Wheel UP
                viewPoint.x -= INCREMENT_UNIT;
            } else {
                viewPoint.x += INCREMENT_UNIT;
            }
            viewport.setViewPosition(getValidPoint(viewPoint));
        } else {
            if (!isScrollable()) {
                return;
            }
            Point viewPoint = viewport.getViewPosition();
            if (e.getWheelRotation() < 0) { // Wheel UP
                viewPoint.y -= INCREMENT_UNIT;
            } else {
                viewPoint.y += INCREMENT_UNIT;
            }
            viewport.setViewPosition(getValidPoint(viewPoint));
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        refreshImage();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    private void refreshImage() {
        Image scaledImage = generateImage();

        if (scaledImage != null) {
            imageWrapper.setText(null);
            imageWrapper.setIcon(new ImageIcon(scaledImage));
        } else {
            imageWrapper.setText("...(PENDING)...");
            imageWrapper.setIcon(null);
        }

        SwingUtilities.invokeLater(() -> {
            moveImageToCenter();
            repaint();
        });
    }

    private void refreshZoom(boolean isZoomOut) {
        if (!isScrollable()) {
            return;
        }
        Point viewPoint = viewport.getViewPosition();
        int zoomX = image.getWidth(null) * ZOOM_UNIT / 100 / 2;
        int zoomY = image.getHeight(null) * ZOOM_UNIT / 100 / 2;
        if (isZoomOut) {
            zoomX = -zoomX;
            zoomY = -zoomY;
        }
        viewPoint.x += zoomX;
        viewPoint.y += zoomY;
        viewport.setViewPosition(getValidPoint(viewPoint));
    }

    private Image generateImage() {
        if (image == null) {
            return null;
        }

        int width = -1;
        int height = -1;

        if (isBestFit) {
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);
            width = viewport.getWidth() - 20;
            height = viewport.getHeight() - 20;
            int scaleWidth = imageWidth * height / imageHeight;
            int scaleHeight = imageHeight * width / imageWidth;
            if (scaleHeight > height) {
                width = -1;
            } else if (scaleWidth > width) {
                height = -1;
            }
        } else if (isWidthFit) {
            width = viewport.getWidth() - 20;
        } else {
            width = image.getWidth(null) * zoom / 100;
        }

        return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    private boolean isScrollable() {
        int maxViewX = imageWrapper.getWidth() - viewport.getWidth();
        int maxViewY = imageWrapper.getHeight() - viewport.getHeight();

        return maxViewX > 0 || maxViewY > 0;
    }

    private Point getValidPoint(Point point) {
        int maxViewX = imageWrapper.getWidth() - viewport.getWidth();
        int maxViewY = imageWrapper.getHeight() - viewport.getHeight();

        if (point.x < 0) {
            point.x = 0;
        } else if (point.x > maxViewX) {
            point.x = maxViewX;
        }

        if (point.y < 0) {
            point.y = 0;
        } else if (point.y > maxViewY) {
            point.y = maxViewY;
        }
        return point;
    }

    private void moveImageToCenter() {
        if (viewport == null || !isScrollable()) {
            return;
        }
        int x = (imageWrapper.getWidth() - viewport.getWidth()) / 2;
        int y = (imageWrapper.getHeight() - viewport.getHeight()) / 2;

        viewport.setViewPosition(new Point(x, y));
    }

    private void initScrollBar(JScrollBar scrollBar) {
        scrollBar.setUnitIncrement(INCREMENT_UNIT);
        // scrollBar.setPreferredSize(new Dimension());
    }
}
