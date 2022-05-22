package com.github.binhtran432k.plantumlpreviewer.gui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.gui.controller.ImageBoardController;
import com.github.binhtran432k.plantumlpreviewer.gui.controller.MenuBarController;
import com.github.binhtran432k.plantumlpreviewer.gui.controller.StatusBarController;
import com.github.binhtran432k.plantumlpreviewer.gui.controller.WindowController;
import com.github.binhtran432k.plantumlpreviewer.gui.listener.ImageBoardListener;
import com.github.binhtran432k.plantumlpreviewer.gui.listener.MainWindowListener;
import com.github.binhtran432k.plantumlpreviewer.gui.listener.MenuBarListener;
import com.github.binhtran432k.plantumlpreviewer.gui.model.MenuBarModel;
import com.github.binhtran432k.plantumlpreviewer.gui.model.StatusBarModel;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ImageBoardModel;
import com.github.binhtran432k.plantumlpreviewer.gui.view.ImageBoardView;
import com.github.binhtran432k.plantumlpreviewer.gui.view.MainWindowView;
import com.github.binhtran432k.plantumlpreviewer.gui.view.MenuBarView;
import com.github.binhtran432k.plantumlpreviewer.gui.view.PreviewPrincipalView;
import com.github.binhtran432k.plantumlpreviewer.gui.view.StatusBarView;

/**
 * Gui Manager for Application
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class GuiManager {

    public GuiManager(Option option) {
        initialize(option);
    }

    private void initialize(Option option) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }

            // Model
            MenuBarModel menuBarModel = new MenuBarModel(option);
            ImageBoardModel imageBoardModel = new ImageBoardModel(option);
            StatusBarModel statusBarModel = new StatusBarModel(option);

            // Controller
            WindowController windowController = new WindowController();
            StatusBarController statusBarController = new StatusBarController(statusBarModel);
            ImageBoardController imageBoardController = new ImageBoardController(imageBoardModel, statusBarController);
            MenuBarController menuBarController = new MenuBarController(menuBarModel);

            // View listener
            ImageBoardListener listener = new ImageBoardListener(imageBoardController);
            MainWindowListener mainWindowListener = new MainWindowListener(windowController, imageBoardController,
                    menuBarController);
            MenuBarListener menuBarListener = new MenuBarListener(menuBarController, imageBoardController,
                    windowController);

            // View
            MenuBarView menuBarView = new MenuBarView(menuBarModel, menuBarListener);
            MainWindowView mainWindowView = new MainWindowView(menuBarView, mainWindowListener);

            ImageBoardView imageBoardView = new ImageBoardView(imageBoardModel, listener);
            StatusBarView statusBarView = new StatusBarView(statusBarModel, imageBoardModel);
            PreviewPrincipalView previewPrincipalView = new PreviewPrincipalView(imageBoardView, statusBarView);
            mainWindowView.setPrincipleView(previewPrincipalView);

            watchFileChange(imageBoardController, option);

            // Notify First time
            menuBarModel.notifyAllSubcribers();
            imageBoardModel.notifyAllSubcribers();
        });
    }

    private void watchFileChange(ImageBoardController imageBoardController, Option option) {
        ExecutorService executor = Executors.newFixedThreadPool(option.getNbThread());
        Timer timer = new Timer(option.getPeriod(), e -> {
            executor.submit(() -> {
                imageBoardController.reloadImageFromFileOnChanged();
            });
        });
        timer.setInitialDelay(0);
        timer.start();
    }

}
