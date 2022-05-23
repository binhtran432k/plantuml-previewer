package com.github.binhtran432k.plantumlpreviewer.cli;

import java.io.File;

import com.github.binhtran432k.plantumlpreviewer.core.PlantUmlFormat;
import com.github.binhtran432k.plantumlpreviewer.gui.helper.StringHelper;
import com.github.binhtran432k.plantumlpreviewer.gui.model.StatusAction;
import com.github.binhtran432k.plantumlpreviewer.gui.model.ZoomAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Option parser from cli
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@Getter
@Setter
public class Option {

    public static final double FOLD_ZOOM_IN = 1.1;
    public static final int MAX_PIXEL = 1900;
    public static final int MIN_PIXEL = 1;
    public static final int BORDER_SIZE = 0;
    public static final int FAST_SPEED_FACTOR = 5;
    public static final int INCREMENT_UNIT = 30;
    public static final int PRIMARY_COLOR = 0xe5e5e5;
    public static final int SECONDARY_COLOR = 0xc1c1c1;

    private boolean isViewMenuBar = true;
    private boolean isViewScrollBar = false;
    private int index = 0;
    private int nbThread = 1;
    private int period = 300;
    private ZoomAction zoomAction = ZoomAction.UNKOWN;
    private StatusAction statusAction = StatusAction.INITIALIZING;

    private File file;
    private PlantUmlFormat plantUmlFormat = PlantUmlFormat.PNG;
    private CommandLineType cliType = CommandLineType.PLANTUML;

    public Option(String... args) {
        initOptions(args);
    }

    public void initOptions(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equalsIgnoreCase("-plantuml")) {
                arg = args[++i];
                if (i == args.length) {
                    continue;
                } else if (file == null) {
                    file = new File(StringHelper.eventuallyRemoveStartingAndEndingDoubleQuote(arg));
                }
            } else if (arg.equalsIgnoreCase("-help") || arg.equalsIgnoreCase("-h")) {
                cliType = CommandLineType.HELP;
                return;
            } else if (file == null) {
                file = new File(StringHelper.eventuallyRemoveStartingAndEndingDoubleQuote(arg));
            }
        }
    }

}
