package com.github.binhtran432k.plantumlpreviewer.gui.helper;

import java.awt.Point;

/**
 * Image Helper for image view
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class ImageHelper {

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

}
