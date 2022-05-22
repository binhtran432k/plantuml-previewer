package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.gui.helper.StringHelper;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ImageBoardModel;
import com.github.binhtran432k.plantumlpreviewer.gui.model.StatusAction;
import com.github.binhtran432k.plantumlpreviewer.gui.model.StatusBarModel;
import com.github.binhtran432k.plantumlpreviewer.gui.ui.SmoothLabel;

import lombok.Getter;

/**
 * View for status bar
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class StatusBarView implements IViewSubcriber {

    private @Getter JPanel statusBar;
    private SmoothLabel label;
    private StatusBarModel statusBarModel;
    private ImageBoardModel imageBoardModel;

    public StatusBarView(StatusBarModel statusBarModel, ImageBoardModel imageBoardModel) {
        this.statusBarModel = statusBarModel;
        this.imageBoardModel = imageBoardModel;
        this.label = initLabel();
        this.statusBar = initStatusBar(this.label);

        statusBarModel.subcribe(SubcribeAction.STATUS_BAR, this);
        imageBoardModel.subcribe(SubcribeAction.STATUS_BAR, this);
    }

    private JPanel initStatusBar(SmoothLabel label) {
        JPanel statusBar = new JPanel();
        statusBar.setBackground(new Color(Option.SECONDARY_COLOR));
        statusBar.setLayout(new BorderLayout());
        statusBar.add(label);

        return statusBar;
    }

    private SmoothLabel initLabel() {
        SmoothLabel label = new SmoothLabel();
        label.setFont(label.getFont().deriveFont(14f));
        label.setBorder(new EmptyBorder(3, 6, 3, 6));

        return label;
    }

    @Override
    public void update(SubcribeAction action) {
        if (action == SubcribeAction.STATUS_BAR) {
            updateStatus();
        }
    }

    private void updateStatus() {
        StatusAction statusAction = statusBarModel.getStatusAction();
        String status = "";

        if (statusAction == StatusAction.INITIALIZING) {
            status = "Initializing...";
        } else if (statusAction == StatusAction.PENDING) {
            status = "Pending...";
        } else if (statusAction == StatusAction.OPENING) {
            status = "Opening...";
        } else if (statusAction == StatusAction.ZOOMING) {
            status = "Zooming...";
        } else if (statusAction == StatusAction.PREVIEWING) {
            status = generateImageStatus();
        }

        label.setText(generateHtmlLabel(status));
    }

    private String generateHtmlLabel(String status) {
        String htmlFormat = "<html><body style=\"text-align: justify; text-justify: inter-word;\">%s</body></html>";

        return String.format(htmlFormat, StringHelper.unicodeForHtml(status));
    }

    private String generateImageStatus() {
        File file = imageBoardModel.getFileWatcher().getFile();
        if (file == null) {
            return "No file to preview";
        } else if (!file.exists()) {
            return "File not found";
        } else if (imageBoardModel.getImage() == null) {
            return "No image to preview";
        }

        String pageNumber = String.format("%d/%d", imageBoardModel.getIndex() + 1, imageBoardModel.getMaxImage());
        String name = String.format("%s %s", file.getName(),
                imageBoardModel.getDescription());
        String zoom = String.format("[%d%%]", Math.round(imageBoardModel.getZoom() * 100));
        String coordinate = String.format("%s %s", imageBoardModel.getX(), imageBoardModel.getY());
        String padding = "   ";

        return String.join(padding, pageNumber, name, zoom, coordinate);
    }
}
