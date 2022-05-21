package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Image Panel of main window
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class PreviewPrincipalView extends PrincipalView {

    public PreviewPrincipalView(ImageBoardView imageBoardView, StatusBarView statusBarView) {
        setPrincipal(initPreviewPrincipal(imageBoardView, statusBarView));
    }

    private JPanel initPreviewPrincipal(ImageBoardView imageBoardView, StatusBarView statusBarView) {
        JPanel principal = new JPanel();
        principal.setLayout(new BorderLayout());

        principal.add(statusBarView.getStatusBar(), BorderLayout.SOUTH);
        principal.add(imageBoardView.getImageBoard(), BorderLayout.CENTER);

        return principal;
    }

}
