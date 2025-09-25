package com.hamkelasi.ui.test_double;

import com.hamkelasi.ui.refactored.file_upload.Uploader;

public class StubUploader implements Uploader {
    private boolean hasFile;
    private String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    @Override
    public boolean hasFile() {
        return hasFile;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String write(String dir) {
        return "";
    }
}
