package com.github.binhtran432k.plantumlpreviewer.gui.model;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.DiagramDescription;

public class GuiModel {
    public static final int ZOOM_UNIT = 10;
    public static final int INCREMENT_UNIT = 30;

    // Data
    private int index = 0;
    private int maxPage = 0;
    private int zoom = 100;
    private boolean isViewScroll = false;
    private ZoomAction zoomAction = ZoomAction.FIT;
    private Point holdPoint;
    private Point relativePoint;
    private String currentImageDescription;
    private String statusStr;
    private Option option;

    // View
    private Image image;
    private Cursor cursor;
    private JViewport viewport = new JViewport();
    private JPanel imageView = new JPanel(new GridBagLayout());
    private JLabel imageWrapper = new JLabel();
    private Consumer<Cursor> cursorConsumer;
    private Consumer<String> statusConsumer;

    private static class Loader {
        static final GuiModel INSTANCE = new GuiModel();
    }

    private GuiModel() {
        imageWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        imageView.setBackground(new Color(0xe5e5e5));
        imageView.add(imageWrapper);
    }

    public static GuiModel getInstance() {
        return Loader.INSTANCE;
    }

    /**
     * @return the zoomAction
     */
    public ZoomAction getZoomAction() {
        return zoomAction;
    }

    /**
     * @param zoomAction the zoomAction to set
     */
    public void setZoomAction(ZoomAction zoomAction) {
        this.zoomAction = zoomAction;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the statusConsumer
     */
    public Consumer<String> getStatusConsumer() {
        return statusConsumer;
    }

    /**
     * @param statusConsumer the statusConsumer to set
     */
    public void setStatusConsumer(Consumer<String> statusConsumer) {
        this.statusConsumer = statusConsumer;
    }

    /**
     * @return the cursorConsumer
     */
    public Consumer<Cursor> getCursorConsumer() {
        return cursorConsumer;
    }

    /**
     * @param runnable the runnable to set
     */
    public void setCursorConsumer(Consumer<Cursor> consumer) {
        this.cursorConsumer = consumer;
    }

    /**
     * @return the isViewScroll
     */
    public boolean isViewScroll() {
        return isViewScroll;
    }

    /**
     * @param isViewScroll the isViewScroll to set
     */
    public void setViewScroll(boolean isViewScroll) {
        this.isViewScroll = isViewScroll;
        cursorConsumer.accept(cursor);
    }

    /**
     * @return the statusStr
     */
    public String getStatusStr() {
        return statusStr;
    }

    /**
     * @param statusStr the statusStr to set
     */
    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    /**
     * @return the currentImageDescription
     */
    public String getCurrentImageDescription() {
        return currentImageDescription;
    }

    /**
     * @param currentImageDescription the currentImageDescription to set
     */
    public void setCurrentImageDescription(String currentImageDescription) {
        this.currentImageDescription = currentImageDescription;
    }

    /**
     * @return the option
     */
    public Option getOption() {
        return option;
    }

    /**
     * @param option the option to set
     */
    public void setOption(Option option) {
        this.option = option;
    }

    /**
     * @return the viewport
     */
    public JViewport getViewport() {
        return viewport;
    }

    /**
     * @param viewport the viewport to set
     */
    public void setViewport(JViewport viewport) {
        this.viewport = viewport;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the imageWrapper
     */
    public JLabel getImageWrapper() {
        return imageWrapper;
    }

    /**
     * @param imageWrapper the imageWrapper to set
     */
    public void setImageWrapper(JLabel imageWrapper) {
        this.imageWrapper = imageWrapper;
    }

    /**
     * @return the imageView
     */
    public JPanel getImageView() {
        return imageView;
    }

    /**
     * @param imageView the imageView to set
     */
    public void setImageView(JPanel imageView) {
        this.imageView = imageView;
    }

    /**
     * @return the zoom
     */
    public int getZoom() {
        return zoom;
    }

    /**
     * @param zoom the zoom to set
     */
    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    /**
     * @return the maxPage
     */
    public int getMaxPage() {
        return maxPage;
    }

    /**
     * @param maxPage the maxPage to set
     */
    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    /**
     * @return the cursor
     */
    public Cursor getCursor() {
        return cursor;
    }

    /**
     * @param cursor the cursor to set
     */
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        cursorConsumer.accept(cursor);
    }

    /**
     * @return the holdPoint
     */
    public Point getHoldPoint() {
        return holdPoint;
    }

    /**
     * @param holdPoint the holdPoint to set
     */
    public void setHoldPoint(Point holdPoint) {
        this.holdPoint = holdPoint;
    }

    public int getMaxViewX() {
        return Math.max(imageWrapper.getWidth() - viewport.getWidth(), 0);
    }

    public int getMaxViewY() {
        return Math.max(imageWrapper.getHeight() - viewport.getHeight(), 0);
    }

    public void updateAdditionIndex(int addition) {
        int newIndex = index + addition;

        if (newIndex <= 0) {
            newIndex = 0;
        } else if (newIndex >= maxPage - 1) {
            newIndex = maxPage - 1;
        }

        if (index != newIndex) {
            index = newIndex;
            reloadImage();
        }
    }

    public boolean isScrollable() {
        return isScrollableVertical() || isScrollableHorizontal();
    }

    public boolean isScrollableHorizontal() {
        return imageWrapper.getWidth() - viewport.getWidth() > 0;
    }

    public boolean isScrollableVertical() {
        return imageWrapper.getHeight() - viewport.getHeight() > 0;
    }

    public void updateToVisiblePosition(Point point) {
        if (viewport == null) {
            return;
        }

        relativePoint = genVisiblePoint(point);

        imageView.setVisible(false);
        viewport.setViewPosition(relativePoint);
        imageView.setVisible(true);

        updateStatus();
    }

    public void updateImageToRelative() {
        if (relativePoint == null) {
            int x = (imageWrapper.getWidth() - viewport.getWidth()) / 2;
            int y = (imageWrapper.getHeight() - viewport.getHeight()) / 2;
            relativePoint = new Point(x, y);
        }

        updateToVisiblePosition(relativePoint);
    }

    public void updateImage() {
        new Thread(() -> {
            try {
                ByteArrayOutputStream png = new ByteArrayOutputStream();
                String source = Files.readString(option.getFile().toPath());

                SourceStringReader reader = new SourceStringReader(source);

                DiagramDescription description = reader.outputImage(png, index,
                        new FileFormatOption(FileFormat.PNG));

                currentImageDescription = description.toString();
                maxPage = calNumOfImages(reader);

                image = ImageIO.read(new ByteArrayInputStream(png.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            refreshZoom();
            refreshImage();
            SwingUtilities.invokeLater(() -> {
                updateImageToRelative();
            });
        }).start();
    }

    public void reloadImage() {
        new Thread(() -> {
            try {
                ByteArrayOutputStream png = new ByteArrayOutputStream();
                String source = Files.readString(option.getFile().toPath());

                SourceStringReader reader = new SourceStringReader(source);

                DiagramDescription description = reader.outputImage(png, index,
                        new FileFormatOption(FileFormat.PNG));

                currentImageDescription = description.toString();

                image = ImageIO.read(new ByteArrayInputStream(png.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            refreshZoom();
            refreshImage();
            SwingUtilities.invokeLater(() -> {
                updateImageToRelative();
            });
        }).start();
    }

    public int calNumOfImages(SourceStringReader reader) {
        return reader.getBlocks().stream().mapToInt(b -> b.getDiagram().getNbImages()).sum();
    }

    public void zoomImage(boolean isZoomIn) {
        refreshZoomPosition(isZoomIn);

        if (isZoomIn) {
            zoom += ZOOM_UNIT;
        } else {
            zoom -= ZOOM_UNIT;
        }

        refreshZoom();

        refreshImage();
    }

    public void scrollImage(boolean isHorizontal, boolean isUpOrLeft) {
        int increment;
        if (isUpOrLeft) {
            increment = -INCREMENT_UNIT;
        } else {
            increment = INCREMENT_UNIT;
        }

        Point viewPoint = viewport.getViewPosition();
        if (isHorizontal) {
            viewPoint.x += increment;
        } else {
            viewPoint.y += increment;
        }

        updateToVisiblePosition(viewPoint);
    }

    public void scrollToSize(ScrollSizeType size) {
        Point viewPoint = viewport.getViewPosition();

        if (size == ScrollSizeType.TOP) {
            viewPoint.y = 0;
        } else if (size == ScrollSizeType.RIGHT) {
            viewPoint.x = imageWrapper.getWidth();
        } else if (size == ScrollSizeType.BOTTOM) {
            viewPoint.y = imageWrapper.getHeight();
        } else if (size == ScrollSizeType.LEFT) {
            viewPoint.x = 0;
        }

        updateToVisiblePosition(viewPoint);
    }

    public void refreshImage() {
        Image scaledImage = generateImage();

        if (scaledImage != null) {
            imageWrapper.setText(null);
            imageWrapper.setIcon(new ImageIcon(scaledImage));
        } else {
            imageWrapper.setText("...(Pending)...");
            imageWrapper.setIcon(null);
        }

        updateStatus();
    }

    public void refreshZoom() {
        if (image == null) {
            return;
        }

        int width = -1;
        int height = -1;

        if (zoomAction == ZoomAction.BEST_FIT) {
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
        } else if (zoomAction == ZoomAction.WIDTH_FIT) {
            width = viewport.getWidth() - 20;
        } else if (zoomAction == ZoomAction.FIT) {
            width = image.getWidth(null);
        } else if (zoomAction == ZoomAction.ZOOMABLE) {
            width = image.getWidth(null) * zoom / 100;
        }

        if (width == -1) {
            zoom = height * 100 / image.getHeight(null);
        } else if (height == -1) {
            zoom = width * 100 / image.getWidth(null);
        }

        if (zoom < 20) {
            zoom = 20;
        }
    }

    private Point genVisiblePoint(Point point) {
        int maxViewX = getMaxViewX();
        int maxViewY = getMaxViewY();

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

    private void refreshZoomPosition(boolean isZoomIn) {
        if (image == null) {
            return;
        }

        Point viewPoint = viewport.getViewPosition();
        int zoomX = image.getWidth(null) * ZOOM_UNIT / 100 / 2;
        int zoomY = image.getHeight(null) * ZOOM_UNIT / 100 / 2;
        if (!isZoomIn) {
            zoomX = -zoomX;
            zoomY = -zoomY;
        }
        viewPoint.x += zoomX;
        viewPoint.y += zoomY;

        updateToVisiblePosition(viewPoint);
    }

    public void updateStatus() {
        statusStr = generateHtmlStr(generateImageStatusStr());
        statusConsumer.accept(statusStr);
    }

    private String generateHtmlStr(String str) {
        String htmlFormat = "<html><body style=\"text-align: justify; text-justify: inter-word;\">%s</body></html>";

        return String.format(htmlFormat, StringUtils.unicodeForHtml(str));
    }

    private String generateImageStatusStr() {
        String pageNumber = String.format("%d/%d", index + 1, maxPage);
        String name = String.format("%s %s", option.getFile().getName(), currentImageDescription);
        String source = String.format("- %s -", option.getSourceType().toString());
        String zoom = String.format("[%d%%]", getZoom());
        StringBuilder position = new StringBuilder("");
        if (isScrollable() && relativePoint != null) {
            int relativeXPercent = getPercent(relativePoint.x, getMaxViewX());
            int relativeYPercent = getPercent(relativePoint.y, getMaxViewY());
            position.append(relativeXPercent);
            position.append(", ");
            position.append(relativeYPercent);
        }
        String padding = "   ";

        return String.join(padding, pageNumber, source, name, zoom, position);
    }

    private int getPercent(int x, int maxX) {
        if (maxX == 0) {
            return 0;
        }

        return x * 100 / maxX;
    }

    private Image generateImage() {
        if (image == null) {
            return null;
        }

        return image.getScaledInstance(image.getWidth(null) * zoom / 100, -1, Image.SCALE_FAST);
    }

}
