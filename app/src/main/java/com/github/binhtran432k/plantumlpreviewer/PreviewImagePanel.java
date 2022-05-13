package com.github.binhtran432k.plantumlpreviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;

import com.google.common.io.Files;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.DiagramDescription;

/**
 * Image Panel of main window
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class PreviewImagePanel extends JPanel {

    private class ImagePanel extends JScrollPane implements MouseInputListener, MouseWheelListener {
        private final int ZOOM_UNIT = 10;
        private final int INCREMENT_UNIT = 30;

        private int zoom = 100;
        private JPanel imageView = new JPanel(new GridBagLayout());
        private JLabel imageWrapper = new JLabel();
        private Image image;
        private Point holdPoint;

        public ImagePanel() {
            initUI();
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public int getZoom() {
            return zoom;
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
            SwingUtilities.invokeLater(() -> {
            });

            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
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

        public void refreshImage() {
            imageWrapper.setIcon(new ImageIcon(generateImage()));
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

            return image.getScaledInstance(image.getWidth(null) * zoom / 100, -1,
                    Image.SCALE_SMOOTH);
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

    private class StatusPanel extends JPanel {
        private JLabel component;

        public StatusPanel(String componentStr) {
            component = new JLabel(componentStr);
            component.setFont(component.getFont().deriveFont(14f));
            component.setBorder(new EmptyBorder(3, 6, 3, 6));

            initUI();
        }

        public void setComponent(String text) {
            component.setText(text);
        }

        private void initUI() {
            setBackground(new Color(0xc1c1c1));
            setLayout(new BorderLayout());

            add(component);
        }
    }

    private int currentPage = 1;
    private int maxPage = 1;
    private String currentImageDescription;
    private String statusStr;

    private SourceType sourceType = SourceType.BUILTIN;
    private File file;
    private BufferedImage image;
    private StatusPanel statusPanel;
    private ImagePanel imagePanel;

    public PreviewImagePanel(File file) {
        this.file = file;
        imagePanel = new ImagePanel();
        statusStr = generateImageStatusStr();
        statusPanel = new StatusPanel(generateHtmlLabel());

        initUI();
    }

    public void setStatusImageLabel(String text) {
        statusPanel.setComponent(text);
    }

    public int calNumOfImages(SourceStringReader reader) {
        return reader.getBlocks().stream().mapToInt(b -> b.getDiagram().getNbImages()).sum();
    }

    private String generateHtmlLabel() {
        String htmlFormat = "<html><body style=\"text-align: justify; text-justify: inter-word;\">%s</body></html>";

        return String.format(htmlFormat, StringUtils.unicodeForHtml(statusStr));
    }

    private String generateImageStatusStr() {
        String pageNumber = String.format("%d/%d", currentPage, maxPage);
        String name = String.format("%s %s", file.getName(), currentImageDescription);
        String source = String.format("- %s -", sourceType.toString());
        String zoom = String.format("[%d%%]", imagePanel.getZoom());
        String padding = "   ";

        return String.join(padding, pageNumber, source, name, zoom);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        add(statusPanel, BorderLayout.SOUTH);
        add(imagePanel, BorderLayout.CENTER);
        repaint();

        SwingUtilities.invokeLater(() -> {
            try {
                ByteArrayOutputStream png = new ByteArrayOutputStream();
                String source = Files.asCharSource(file, StandardCharsets.UTF_8).read();

                SourceStringReader reader = new SourceStringReader(source);
                DiagramDescription description = reader.outputImage(png, currentPage - 1, new FileFormatOption(FileFormat.PNG));
                currentImageDescription = description.toString();
                statusStr = generateImageStatusStr();
                setStatusImageLabel(generateHtmlLabel());

                image = ImageIO.read(new ByteArrayInputStream(png.toByteArray()));
                imagePanel.setImage(image);
            } catch (IOException e) {
                imagePanel.setImage(null);
                e.printStackTrace();
            }

            imagePanel.refreshImage();

            SwingUtilities.invokeLater(() -> {
                imagePanel.moveImageToCenter();
                repaint();
            });
        });
    }

}
