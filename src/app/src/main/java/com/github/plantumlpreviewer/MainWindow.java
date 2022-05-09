package com.github.plantumlpreviewer;

import javax.swing.JFrame;

/**
 * Main window of application
 *
 * Please see the {@link javax.swing.JFrame} for true identity
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class MainWindow extends JFrame {

    public MainWindow() {
        initUI();
    }

    private void initUI() {
        setTitle("PlantUML Previewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // always startup center
    }

}
