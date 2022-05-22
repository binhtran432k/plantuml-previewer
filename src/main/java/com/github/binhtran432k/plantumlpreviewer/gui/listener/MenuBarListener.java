package com.github.binhtran432k.plantumlpreviewer.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.binhtran432k.plantumlpreviewer.gui.controller.ApplicationController;

import lombok.RequiredArgsConstructor;

/**
 * Input listener for Menu bar
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@RequiredArgsConstructor
public class MenuBarListener implements ActionListener {
    private final ApplicationController applicationController;

    @Override
    public void actionPerformed(ActionEvent e) {
        applicationController.doAction(ApplicationAction.valueOf(e.getActionCommand()));
    }

}
