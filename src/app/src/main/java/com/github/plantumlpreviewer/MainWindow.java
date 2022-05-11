package com.github.plantumlpreviewer;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main window of application
 *
 * Please see the {@link javax.swing.JFrame} for true identity
 * 
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class MainWindow extends JFrame {

    private ImagePanel imagePanel = new ImagePanel();
    private JPanel currentPanel;

    public MainWindow() {
        initUI();
    }

    private void initUI() {
        setTitle("PlantUML Previewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // always startup center

        initMenubar();

        switchToPanel(imagePanel);
    }

    private void initMenubar() {
        setJMenuBar(new MyMenuBar());
    }

    private void switchToPanel(JPanel toPanel) {
        if (currentPanel == toPanel) {
            return;
        }

        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = toPanel;
        add(currentPanel);

        revalidate();
        repaint();
    }

}
