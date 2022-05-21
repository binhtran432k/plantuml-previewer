package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JMenuBar;

import com.github.binhtran432k.plantumlpreviewer.gui.listener.MenuBarAction;
import com.github.binhtran432k.plantumlpreviewer.gui.listener.MenuBarListener;
import com.github.binhtran432k.plantumlpreviewer.gui.model.MenuBarModel;
import com.github.binhtran432k.plantumlpreviewer.gui.ui.SmoothMenuBar;
import com.github.binhtran432k.plantumlpreviewer.gui.ui.SmoothMenuBar.SmoothMenu;
import com.github.binhtran432k.plantumlpreviewer.gui.ui.SmoothMenuBar.SmoothMenuItem;

/**
 * Menu bar for application
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class MenuBarView implements IViewSubcriber {

    private final SmoothMenuBar menuBar;
    private final MenuBarModel menuBarModel;

    public MenuBarView(MenuBarModel menuBarModel, MenuBarListener listener) {
        this.menuBarModel = menuBarModel;
        this.menuBar = initMenuBar(listener);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public void update(SubcribeAction action) {
        if (action == SubcribeAction.MENU_BAR) {
            menuBar.setVisible(menuBarModel.isVisible());
            menuBar.repaint();
        }
    }

    private SmoothMenuBar initMenuBar(MenuBarListener listener) {
        SmoothMenuBar menuBar = new SmoothMenuBar();
        menuBar.setBackground(new Color(0xe5e5e5));

        menuBar.add(initFileMenu(listener));

        menuBarModel.subcribe(SubcribeAction.MENU_BAR, this);

        return menuBar;
    }

    private SmoothMenu initFileMenu(MenuBarListener listener) {
        SmoothMenu menu = new SmoothMenu("File");

        SmoothMenuItem exitMenuItem = new SmoothMenuItem("Quit");
        exitMenuItem.setMnemonic(KeyEvent.VK_Q);
        exitMenuItem.setActionCommand(MenuBarAction.QUIT.toString());
        exitMenuItem.addActionListener(listener);

        menu.add(exitMenuItem);

        return menu;
    }

}
