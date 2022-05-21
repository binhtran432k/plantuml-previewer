package com.github.binhtran432k.plantumlpreviewer.gui.helper;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * The extended version of {@link ImageHelper}
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class ImageHelperPlus {

    public static BufferedImage getScaledImage(BufferedImage image, double zoom) {
        final Dimension imageDim = new Dimension(image.getWidth(), image.getHeight());
        final Dimension newImgDim = ImageHelper.getScaledDimension(imageDim, zoom);
        return ImageHelper.getScaledInstance(image, newImgDim, getAntiAliasHints(), false);
    }

    public static Point getScaledPoint(int x, int y, int viewWidth, int viewHeight, double zoom) {
        double viewWidthDiv2 = viewWidth / 2;
        double viewHeightDiv2 = viewHeight / 2;

        double newPointX = zoom * (x + viewWidthDiv2) - viewWidthDiv2;
        double newPointY = zoom * (y + viewHeightDiv2) - viewHeightDiv2;

        Point newPoint = new Point();
        newPoint.setLocation(newPointX, newPointY);

        return newPoint;
    }

    public static double getBestFitZoom(int imageWidth, int imageHeight, int viewWidth, int viewHeight) {
        double zoom = viewWidth / (double) imageWidth;
        if (viewHeight < imageHeight * zoom) {
            zoom = viewHeight / (double) imageHeight;
        }
        return zoom;
    }

    public static double getWidthFitZoom(int imageWidth, int viewWidth) {
        return viewWidth / (double) imageWidth;
    }

    private static RenderingHints getAntiAliasHints() {
        final RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return hints;
    }

}
