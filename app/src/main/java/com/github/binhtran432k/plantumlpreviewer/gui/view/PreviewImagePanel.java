package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.github.binhtran432k.plantumlpreviewer.gui.model.SourceType;
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

        new Thread(() -> {
            try {
                ByteArrayOutputStream png = new ByteArrayOutputStream();
                String source = Files.asCharSource(file, StandardCharsets.UTF_8).read();

                SourceStringReader reader = new SourceStringReader(source);
                DiagramDescription description = reader.outputImage(png, currentPage - 1,
                        new FileFormatOption(FileFormat.PNG));
                currentImageDescription = description.toString();
                statusStr = generateImageStatusStr();
                setStatusImageLabel(generateHtmlLabel());

                image = ImageIO.read(new ByteArrayInputStream(png.toByteArray()));
                imagePanel.updateImage(image);
            } catch (IOException e) {
                imagePanel.updateImage(null);
                e.printStackTrace();
            }
        }).start();
    }

}
