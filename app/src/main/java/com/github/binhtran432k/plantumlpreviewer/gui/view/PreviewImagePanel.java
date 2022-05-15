package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;

import com.github.binhtran432k.plantumlpreviewer.gui.model.SourceType;

import net.sourceforge.plantuml.StringUtils;

/**
 * Image Panel of main window
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class PreviewImagePanel extends JPanel {

    private int index = 1;
    private String currentImageDescription;
    private String statusStr;

    private SourceType sourceType = SourceType.BUILTIN;
    private File file;
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

    private String generateHtmlLabel() {
        String htmlFormat = "<html><body style=\"text-align: justify; text-justify: inter-word;\">%s</body></html>";

        return String.format(htmlFormat, StringUtils.unicodeForHtml(statusStr));
    }

    private String generateImageStatusStr() {
        String pageNumber = String.format("%d/%d", index + 1, imagePanel.getMaxPage());
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

        imagePanel.updateImage(file, index, desc -> {
            currentImageDescription = desc;
            statusStr = generateImageStatusStr();
            setStatusImageLabel(generateHtmlLabel());
        });
    }

}
