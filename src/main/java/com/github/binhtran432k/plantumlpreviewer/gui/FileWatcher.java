package com.github.binhtran432k.plantumlpreviewer.gui;

import java.io.File;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * File Watcher for auto reload image when update
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@NoArgsConstructor
public class FileWatcher {

    private @Getter @Setter File file;
    private long modified = -1;

    public FileWatcher(File file) {
        this.file = file;
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
