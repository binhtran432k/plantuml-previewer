package com.github.binhtran432k.plantumlpreviewer;

import java.io.File;

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
public class MyOption {

    private File file;
    private FileFormatOption fileFormatOption = new FileFormatOption(FileFormat.PNG);

    public MyOption(String... args) {
        initOptions(args);
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

    public void initOptions(String[] args) {
        if (args.length > 0) {
            file = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(args[0]));
        }
    }

}
