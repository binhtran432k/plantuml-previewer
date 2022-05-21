package com.github.binhtran432k.plantumlpreviewer.gui.controller;

import com.github.binhtran432k.plantumlpreviewer.gui.model.MenuBarModel;

import lombok.RequiredArgsConstructor;

/**
 * Controller for Menu bar
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@RequiredArgsConstructor
public class MenuBarController {
    private final MenuBarModel menuBarModel;

    public void toggleMenuBar() {
        menuBarModel.setVisibleAndNotify(!menuBarModel.isVisible());
    }
}
