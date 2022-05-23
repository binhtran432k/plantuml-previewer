package com.github.binhtran432k.plantumlpreviewer.cli;

import com.github.binhtran432k.plantumlpreviewer.gui.GuiManager;
import com.github.binhtran432k.plantumlpreviewer.gui.helper.StringHelper;

/**
 * Command line manager of application
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class CommandLineManager {

    private Option option;

    public CommandLineManager(String... args) {
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

        return String.format("%s%s", str, StringHelper.repeat(" ", padding));
    }

    private void runPlantumlGui() {
        new GuiManager(option);
    }

}
