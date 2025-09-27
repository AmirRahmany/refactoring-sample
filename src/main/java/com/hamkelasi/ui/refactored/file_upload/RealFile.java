package com.hamkelasi.ui.refactored.file_upload;

import java.io.File;

public class RealFile implements MyFile {
    private final File file;
    private final String realPathDir;

    public RealFile(String realPathDir) {
        this.realPathDir = realPathDir;
        this.file = new File(this.realPathDir);
    }

    @Override
    public boolean dirExists() {
        return file.exists();
    }

    @Override
    public void makeDir() {
        file.mkdirs();
    }

    @Override
    public String getDirectoryRealPath() {
        return "";
    }

    @Override
    public String getSavedPathOf(String filename) {
        return realPathDir + "/" + filename;
    }


}
