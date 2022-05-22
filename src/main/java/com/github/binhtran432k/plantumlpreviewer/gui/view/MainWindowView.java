package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.io.File;
import java.util.prefs.Preferences;
import java.awt.event.*;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import com.github.binhtran432k.plantumlpreviewer.gui.GuiManager;
import com.github.binhtran432k.plantumlpreviewer.gui.listener.MainWindowListener;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ImageBoardModel;
import com.github.binhtran432k.plantumlpreviewer.gui.model.StatusAction;
import com.github.binhtran432k.plantumlpreviewer.gui.model.StatusBarModel;

/**
 * Main window of application
 * 
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class MainWindowView implements IViewSubcriber {

    private static final Preferences prefs = Preferences.userNodeForPackage(GuiManager.class);
    private static final String KEY_DIR = "cur";
    private final String DEFAULT_TITLE = "PlantUML Previewer";

    private final JFrame mainWindow;
    private final ImageBoardModel imageBoardModel;
    private final StatusBarModel statusBarModel;

    public MainWindowView(MenuBarView menuBarView, ImageBoardModel imageBoardModel, StatusBarModel statusBarModel,
            MainWindowListener listener) {
        this.mainWindow = initMainWindow(menuBarView, listener);
        this.imageBoardModel = imageBoardModel;
        this.statusBarModel = statusBarModel;

        statusBarModel.subcribe(SubcribeAction.STATUS_BAR, this);
    }

    public void setPrincipleView(PrincipalView principalView) {
        mainWindow.getContentPane().removeAll();
        mainWindow.getContentPane().add(principalView.getPrincipal());

        onModifyComponent();
    }

    public void displayDialogChangeDir() {
        JFileChooser chooser = initFileChooser();
        final int returnVal = chooser.showOpenDialog(mainWindow);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            changeFile(file);
        }
    }

    @Override
    public void update(SubcribeAction action) {
        if (action == SubcribeAction.STATUS_BAR) {
            StatusAction status = statusBarModel.getStatusAction();
            if (status == StatusAction.OPENING) {
                displayDialogChangeDir();
                statusBarModel.setStatusActionAndNotify(StatusAction.PREVIEWING);
            } else if (status == StatusAction.PREVIEWING) {
                changeTitleFromFile();
            }
        }
    }

    public void changeFile(File file) {
        imageBoardModel.setFileAndResetAndNotify(file);
        if (file != null && file.exists()) {
            prefs.put(KEY_DIR, file.getAbsolutePath());
        }
    }

    private JFrame initMainWindow(MenuBarView menuBarView, MainWindowListener listener) {
        JFrame window = new JFrame();
        window.setTitle(DEFAULT_TITLE);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(800, 600);
        window.setFocusable(true);

        window.setJMenuBar(menuBarView.getMenuBar());

        window.addComponentListener(listener);
        window.addKeyListener(listener);

        window.setVisible(true);

        return window;
    }

    private JFileChooser initFileChooser() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
        chooser.setDialogTitle("Directory to watch:");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        chooser.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, KeyEvent.CTRL_DOWN_MASK), "cancel");
        chooser.getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooser.cancelSelection();
            }
        });

        final String currentPath = prefs.get(KEY_DIR, ".");
        chooser.setCurrentDirectory(new File(currentPath));

        return chooser;
    }

    private void onModifyComponent() {
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    private void changeTitleFromFile() {
        File file = imageBoardModel.getFileWatcher().getFile();
        if (file == null || !file.exists()) {
            mainWindow.setTitle(DEFAULT_TITLE);
            return;
        }

        mainWindow.setTitle(file.getAbsolutePath());
    }

}
