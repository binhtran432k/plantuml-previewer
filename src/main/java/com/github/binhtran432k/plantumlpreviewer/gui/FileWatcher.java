package com.github.binhtran432k.plantumlpreviewer.gui;

import java.io.File;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * File Watcher for auto reload image when update
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@NoArgsConstructor
public class FileWatcher {

    private @Getter File file;
    private long modified;

    public FileWatcher(File file) {
        setFileAndReset(file);
    }

    public void setFileAndReset(File file) {
        this.file = file;
        this.modified = -1;
    }

    public boolean hasChanged() {
        if (isFileNotExists()) {
            return false;
        }
        return file.lastModified() != modified;
    }

    public void refreshModified() {
        if (isFileNotExists()) {
            return;
        }
        modified = file.lastModified();
    }

    public boolean isFileNotExists() {
        return file == null || !file.exists();
    }

}
