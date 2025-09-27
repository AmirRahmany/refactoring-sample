package com.hamkelasi.ui.refactored.file_upload;

public interface MyFile {
    boolean dirExists();
    void makeDir();
    String getDirectoryRealPath();

    String getSavedPathOf(String filename);
}
