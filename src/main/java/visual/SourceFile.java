/*
 * Decompiled with CFR 0.152.
 */
package visual;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class SourceFile {
    private File file;
    private boolean unsavedChanges = false;
    private boolean neverSaved;

    public SourceFile(File file, boolean neverSaved) {
        this.file = file;
        this.neverSaved = neverSaved;
    }

    public String getFileAsString() {
        try {
            return FileUtils.readFileToString(this.file);
        }
        catch (IOException e) {
            return "";
        }
    }

    public String getStageTitleForFile() {
        String stageTitle = this.file.getName();
        if (this.unsavedChanges) {
            stageTitle = stageTitle + " - [Unsaved]";
        }
        return stageTitle;
    }

    public File getRawFile() {
        return this.file;
    }

    public void updateSavedStatus(String newString) {
        this.unsavedChanges = !newString.equals(this.getFileAsString());
    }

    public boolean hasUnsavedChanges() {
        return this.unsavedChanges;
    }

    public boolean isNeverSaved() {
        return this.neverSaved;
    }
}

