package com.github.binhtran432k.plantumlpreviewer.gui.view;

import javax.swing.JFrame;

import com.github.binhtran432k.plantumlpreviewer.gui.listener.MainWindowListener;

/**
 * Main window of application
 * 
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class MainWindowView {

    private final JFrame mainWindow;

    public MainWindowView(MenuBarView menuBarView, MainWindowListener listener) {
        this.mainWindow = initMainWindow(menuBarView, listener);
    }

    public void setPrincipleView(PrincipalView principalView) {
        mainWindow.getContentPane().removeAll();
        mainWindow.getContentPane().add(principalView.getPrincipal());

        onModifyComponent();
    }

    private JFrame initMainWindow(MenuBarView menuBarView, MainWindowListener listener) {
        JFrame window = new JFrame();
        window.setTitle("PlantUML Previewer");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(800, 600);
        window.setFocusable(true);

        window.setJMenuBar(menuBarView.getMenuBar());

        window.addComponentListener(listener);
        window.addKeyListener(listener);

        window.setVisible(true);

        return window;
    }

    private void onModifyComponent() {
        mainWindow.revalidate();
        mainWindow.repaint();
    }

}
