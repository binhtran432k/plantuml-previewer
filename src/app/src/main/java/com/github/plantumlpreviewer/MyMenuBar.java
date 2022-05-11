package com.github.plantumlpreviewer;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuListener;

/**
 * Menu bar for application
 *
 * Please see the {@link javax.swing.JMenuBar} for true identity
 * 
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class MyMenuBar extends JMenuBar {

    public MyMenuBar() {
        initUI();
    }

    private void initUI() {
        setBackground(new Color(0xe5e5e5));

        add(initFileMenu());
    }

    private JMenu initFileMenu() {
        JMenu menu = new JMenu("File");

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setActionCommand("Exit");
        exitMenuItem.addActionListener(e -> {
            System.exit(0);
        });

        menu.add(exitMenuItem);

        return menu;
    }

}
