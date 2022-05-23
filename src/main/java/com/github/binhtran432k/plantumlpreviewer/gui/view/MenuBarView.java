package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.event.KeyEvent;

import javax.swing.JMenuBar;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.gui.listener.ApplicationAction;
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

        menuBar.add(initFileMenu(listener));
        menuBar.add(initViewMenu(listener));
        menuBar.add(initNavigateMenu(listener));
        menuBar.add(initMoveMenu(listener));
        menuBar.add(initMovePlusMenu(listener));

        menuBarModel.subcribe(SubcribeAction.MENU_BAR, this);

        return menuBar;
    }

    private SmoothMenu initFileMenu(MenuBarListener listener) {
        SmoothMenu menu = new SmoothMenu("File");

        SmoothMenuItem openMenuItem = new SmoothMenuItem("Open (o)");
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setActionCommand(ApplicationAction.OPEN_FILE.toString());
        openMenuItem.addActionListener(listener);

        SmoothMenuItem quitMenuItem = new SmoothMenuItem("Quit (q)");
        quitMenuItem.setMnemonic(KeyEvent.VK_Q);
        quitMenuItem.setActionCommand(ApplicationAction.QUIT.toString());
        quitMenuItem.addActionListener(listener);

        menu.add(openMenuItem);

        menu.addSeparator();

        menu.add(quitMenuItem);

        return menu;
    }

    private SmoothMenu initViewMenu(MenuBarListener listener) {
        SmoothMenu menu = new SmoothMenu("View");

        SmoothMenuItem reloadImageMenuItem = new SmoothMenuItem("Reload Image (r)");
        reloadImageMenuItem.setMnemonic(KeyEvent.VK_R);
        reloadImageMenuItem.setActionCommand(ApplicationAction.RELOAD_IMAGE.toString());
        reloadImageMenuItem.addActionListener(listener);

        SmoothMenuItem toggleMenuMenuItem = new SmoothMenuItem("Toggle Menu (m)");
        toggleMenuMenuItem.setMnemonic(KeyEvent.VK_M);
        toggleMenuMenuItem.setActionCommand(ApplicationAction.TOGGLE_MENU_BAR.toString());
        toggleMenuMenuItem.addActionListener(listener);

        SmoothMenuItem toggleScrollMenuItem = new SmoothMenuItem("Toggle Scroll (c)");
        toggleScrollMenuItem.setMnemonic(KeyEvent.VK_C);
        toggleScrollMenuItem.setActionCommand(ApplicationAction.TOGGLE_SCROLL_BAR.toString());
        toggleScrollMenuItem.addActionListener(listener);

        SmoothMenuItem zoomInMenuItem = new SmoothMenuItem("Zoom In (plus)");
        zoomInMenuItem.setMnemonic(KeyEvent.VK_I);
        zoomInMenuItem.setActionCommand(ApplicationAction.ZOOM_IN.toString());
        zoomInMenuItem.addActionListener(listener);

        SmoothMenuItem zoomOutMenuItem = new SmoothMenuItem("Zoom Out (minus)");
        zoomOutMenuItem.setMnemonic(KeyEvent.VK_O);
        zoomOutMenuItem.setActionCommand(ApplicationAction.ZOOM_OUT.toString());
        zoomOutMenuItem.addActionListener(listener);

        SmoothMenuItem imageSizeMenuItem = new SmoothMenuItem("Zoom Image Size (equal)");
        imageSizeMenuItem.setMnemonic(KeyEvent.VK_S);
        imageSizeMenuItem.setActionCommand(ApplicationAction.ZOOM_IMAGE_SIZE.toString());
        imageSizeMenuItem.addActionListener(listener);

        SmoothMenuItem bestFitMenuItem = new SmoothMenuItem("Zoom Best Fit (a)");
        bestFitMenuItem.setMnemonic(KeyEvent.VK_B);
        bestFitMenuItem.setActionCommand(ApplicationAction.ZOOM_BEST_FIT.toString());
        bestFitMenuItem.addActionListener(listener);

        SmoothMenuItem widthFitMenuItem = new SmoothMenuItem("Zoom Width Fit (s)");
        widthFitMenuItem.setMnemonic(KeyEvent.VK_W);
        widthFitMenuItem.setActionCommand(ApplicationAction.ZOOM_WIDTH_FIT.toString());
        widthFitMenuItem.addActionListener(listener);

        menu.add(reloadImageMenuItem);

        menu.addSeparator();

        menu.add(toggleMenuMenuItem);
        menu.add(toggleScrollMenuItem);

        menu.addSeparator();

        menu.add(zoomInMenuItem);
        menu.add(zoomOutMenuItem);

        menu.addSeparator();

        menu.add(imageSizeMenuItem);
        menu.add(bestFitMenuItem);
        menu.add(widthFitMenuItem);

        return menu;
    }

    private SmoothMenu initNavigateMenu(MenuBarListener listener) {
        SmoothMenu menu = new SmoothMenu("Navigate");

        SmoothMenuItem nextMenuItem = new SmoothMenuItem("Next Image (n)");
        nextMenuItem.setMnemonic(KeyEvent.VK_N);
        nextMenuItem.setActionCommand(ApplicationAction.GO_NEXT_IMAGE.toString());
        nextMenuItem.addActionListener(listener);

        SmoothMenuItem prevMenuItem = new SmoothMenuItem("Previous Image (p)");
        prevMenuItem.setMnemonic(KeyEvent.VK_P);
        prevMenuItem.setActionCommand(ApplicationAction.GO_PREV_IMAGE.toString());
        prevMenuItem.addActionListener(listener);

        menu.add(nextMenuItem);
        menu.add(prevMenuItem);

        return menu;
    }

    private SmoothMenu initMoveMenu(MenuBarListener listener) {
        SmoothMenu menu = new SmoothMenu("Move");

        SmoothMenuItem upMenuItem = new SmoothMenuItem("Up (k / up)");
        upMenuItem.setMnemonic(KeyEvent.VK_U);
        upMenuItem.setActionCommand(ApplicationAction.MOVE_UP.toString());
        upMenuItem.addActionListener(listener);

        SmoothMenuItem rightMenuItem = new SmoothMenuItem("Right (l / right)");
        rightMenuItem.setMnemonic(KeyEvent.VK_R);
        rightMenuItem.setActionCommand(ApplicationAction.MOVE_RIGHT.toString());
        rightMenuItem.addActionListener(listener);

        SmoothMenuItem downMenuItem = new SmoothMenuItem("Down (j / down)");
        downMenuItem.setMnemonic(KeyEvent.VK_D);
        downMenuItem.setActionCommand(ApplicationAction.MOVE_DOWN.toString());
        downMenuItem.addActionListener(listener);

        SmoothMenuItem leftMenuItem = new SmoothMenuItem("Left (h / left)");
        leftMenuItem.setMnemonic(KeyEvent.VK_L);
        leftMenuItem.setActionCommand(ApplicationAction.MOVE_LEFT.toString());
        leftMenuItem.addActionListener(listener);

        SmoothMenuItem topMenuItem = new SmoothMenuItem("Top (g)");
        topMenuItem.setMnemonic(KeyEvent.VK_T);
        topMenuItem.setActionCommand(ApplicationAction.MOVE_TOP.toString());
        topMenuItem.addActionListener(listener);

        SmoothMenuItem endMenuItem = new SmoothMenuItem("End (shift + 4)");
        endMenuItem.setMnemonic(KeyEvent.VK_E);
        endMenuItem.setActionCommand(ApplicationAction.MOVE_END.toString());
        endMenuItem.addActionListener(listener);

        SmoothMenuItem bottomMenuItem = new SmoothMenuItem("Bottom (shift + g)");
        bottomMenuItem.setMnemonic(KeyEvent.VK_B);
        bottomMenuItem.setActionCommand(ApplicationAction.MOVE_BOTTOM.toString());
        bottomMenuItem.addActionListener(listener);

        SmoothMenuItem beginMenuItem = new SmoothMenuItem("Begin (0)");
        beginMenuItem.setMnemonic(KeyEvent.VK_G);
        beginMenuItem.setActionCommand(ApplicationAction.MOVE_BEGIN.toString());
        beginMenuItem.addActionListener(listener);

        menu.add(upMenuItem);
        menu.add(downMenuItem);
        menu.add(leftMenuItem);
        menu.add(rightMenuItem);

        menu.addSeparator();

        menu.add(topMenuItem);
        menu.add(bottomMenuItem);
        menu.add(beginMenuItem);
        menu.add(endMenuItem);

        return menu;
    }

    private SmoothMenu initMovePlusMenu(MenuBarListener listener) {
        SmoothMenu menu = new SmoothMenu("Move (Plus)");

        int fastSpeed = Option.FAST_SPEED_FACTOR;

        SmoothMenuItem upMenuItem = new SmoothMenuItem(String.format("Up x%d (shift + k / up)", fastSpeed));
        upMenuItem.setMnemonic(KeyEvent.VK_U);
        upMenuItem.setActionCommand(ApplicationAction.MOVE_UP_PLUS.toString());
        upMenuItem.addActionListener(listener);

        SmoothMenuItem rightMenuItem = new SmoothMenuItem(String.format("Right x%d (shift + l / right)", fastSpeed));
        rightMenuItem.setMnemonic(KeyEvent.VK_R);
        rightMenuItem.setActionCommand(ApplicationAction.MOVE_RIGHT_PLUS.toString());
        rightMenuItem.addActionListener(listener);

        SmoothMenuItem downMenuItem = new SmoothMenuItem(String.format("Down x%d (shift + j / down)", fastSpeed));
        downMenuItem.setMnemonic(KeyEvent.VK_D);
        downMenuItem.setActionCommand(ApplicationAction.MOVE_DOWN_PLUS.toString());
        downMenuItem.addActionListener(listener);

        SmoothMenuItem leftMenuItem = new SmoothMenuItem(String.format("Left x%d (shift + h / left)", fastSpeed));
        leftMenuItem.setMnemonic(KeyEvent.VK_L);
        leftMenuItem.setActionCommand(ApplicationAction.MOVE_LEFT_PLUS.toString());
        leftMenuItem.addActionListener(listener);

        menu.add(upMenuItem);
        menu.add(downMenuItem);
        menu.add(leftMenuItem);
        menu.add(rightMenuItem);

        return menu;
    }

}
