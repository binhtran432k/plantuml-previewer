package com.github.binhtran432k.plantumlpreviewer.gui.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import lombok.Getter;

/**
 * A panel with image view
 * {@link https://coderanch.com/t/338284/java/zoom-zoom-picture-swing}
 *
 * @author Tran Duc Binh
 *
 */
public class ImagePanel extends JPanel {

    private @Getter BufferedImage image;
    private @Getter double scale = 1;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int w = getWidth();
        int h = getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double x = (w - scale * imageWidth) / 2;
        double y = (h - scale * imageHeight) / 2;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
    }

    /**
     * For the scroll pane.
     */
    @Override
    public Dimension getPreferredSize() {
        if (image == null) {
            return super.getPreferredSize();
        }
        int w = (int) (scale * image.getWidth());
        int h = (int) (scale * image.getHeight());
        return new Dimension(w, h);
    }

    public void setScale(double scale) {
        this.scale = scale;
        revalidate(); // update the scroll pane
        repaint();
    }

    public void setImageAndScale(BufferedImage image, double scale) {
        this.image = image;
        this.scale = scale;
        revalidate(); // update the scroll pane
        repaint();
    }

}
