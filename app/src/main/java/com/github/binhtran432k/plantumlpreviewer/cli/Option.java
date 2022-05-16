package com.github.binhtran432k.plantumlpreviewer.cli;

import java.io.File;

import com.github.binhtran432k.plantumlpreviewer.gui.model.SourceType;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;

/**
 * Option parser from cli
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class Option {

    private File file;
    private FileFormatOption fileFormatOption = new FileFormatOption(FileFormat.PNG);
    private CommandLineType cliType = CommandLineType.PLANTUML;
    private SourceType sourceType = SourceType.BUILTIN;

    public Option(String... args) {
        initOptions(args);
    }

    /**
     * @return the sourceType
     */
    public SourceType getSourceType() {
        return sourceType;
    }

    /**
     * @param sourceType the sourceType to set
     */
    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the fileFormatOption
     */
    public FileFormatOption getFileFormatOption() {
        return fileFormatOption;
    }

    /**
     * @param fileFormatOption the fileFormatOption to set
     */
    public void setFileFormatOption(FileFormatOption fileFormatOption) {
        this.fileFormatOption = fileFormatOption;
    }

    /**
     * @return the cliType
     */
    public CommandLineType getCliType() {
        return cliType;
    }

    /**
     * @param cliType the cliType to set
     */
    public void setCliType(CommandLineType cliType) {
        this.cliType = cliType;
    }

    public void initOptions(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equalsIgnoreCase("-plantuml")) {
                arg = args[++i];
                if (i == args.length) {
                    continue;
                } else if (file == null) {
                    file = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg));
                }
            } else if (arg.equalsIgnoreCase("-help") || arg.equalsIgnoreCase("-h")) {
                cliType = CommandLineType.HELP;
                return;
            } else if (file == null) {
                file = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg));
            }
        }
    }

}
