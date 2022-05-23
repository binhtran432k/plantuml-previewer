package com.github.binhtran432k.plantumlpreviewer.gui.controller;

import com.github.binhtran432k.plantumlpreviewer.gui.model.StatusAction;
import com.github.binhtran432k.plantumlpreviewer.gui.model.StatusBarModel;

import lombok.RequiredArgsConstructor;

/**
 * Controller for status bar
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@RequiredArgsConstructor
public class StatusBarController {

    private final StatusBarModel statusBarModel;

    public void setPendingStatus() {
        statusBarModel.setStatusActionAndNotify(StatusAction.PENDING);
    }

    public void setPreviewImageStatus() {
        statusBarModel.setStatusActionAndNotify(StatusAction.PREVIEWING);
    }

    public void setNoPlantUmlStatus() {
        statusBarModel.setStatusActionAndNotify(StatusAction.NO_PLANTUML);
    }

    public void setOpeningStatus() {
        statusBarModel.setStatusActionAndNotify(StatusAction.OPENING);
    }

    public void setZoomingStatus() {
        statusBarModel.setStatusActionAndNotify(StatusAction.ZOOMING);
    }

}
