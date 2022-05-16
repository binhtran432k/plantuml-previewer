package com.github.binhtran432k.plantumlpreviewer.cli;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.binhtran432k.plantumlpreviewer.gui.model.GuiModel;
import com.github.binhtran432k.plantumlpreviewer.gui.view.MainWindow;

public class CommandLine {

    Option option;

    public CommandLine(String... args) {
        option = new Option(args);

        CommandLineType cliType = option.getCliType();
        if (cliType == CommandLineType.HELP) {
            printCommandLineHelp();
        } else if (cliType == CommandLineType.PLANTUML) {
            runPlantumlGui();
        }
    }

    private void printCommandLineHelp() {
        int nameWidth = 30;

        String help = String.join(
                "\n",
                "Usage: java -jar plantumlpreviewer.jar [options]",
                "",
                "where options include:",
                getFixedWidthString("   [-plantuml] [file]", nameWidth) +
                        "To run the previewer grapical with file if exists",
                getFixedWidthString("   -h[elp]", nameWidth) +
                        "To display this help",
                "",
                "where file is path to file to open");

        System.out.println(help);
    }

    private String getFixedWidthString(String str, int width) {
        int padding = width - str.length();
        if (padding <= 0) {
            return str;
        }

        return String.format("%s%s", str, " ".repeat(padding));
    }

    private void runPlantumlGui() {
        SwingUtilities.invokeLater(() -> {
            GuiModel model = GuiModel.getInstance();
            model.setOption(option);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }

}
