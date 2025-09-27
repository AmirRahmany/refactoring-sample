package com.hamkelasi.ui.test_double;

import com.hamkelasi.ui.refactored.file_upload.MyFile;

import java.util.ArrayList;
import java.util.List;

public class FakeFile implements MyFile {
    List<String> dirs = new ArrayList<>();
    private boolean isDirExists;
    private String dir;
    private int makeDirCalls;

    public void setDir(String dirName) {
        this.dir = dirName;
    }

    public void setDirExists(boolean dirExists) {
        isDirExists = dirExists;
    }

    @Override
    public boolean dirExists() {
        return isDirExists;
    }

    @Override
    public void makeDir() {
        makeDirCalls++;
        if (!dirs.contains(dir)) {
            dirs.add(dir);
        }
    }

    @Override
    public String getDirectoryRealPath() {
        return dir;
    }

    @Override
    public String getSavedPathOf(String filename) {
        return dir + "/" + filename;
    }

    public int getMakeDirCalls() {
        return makeDirCalls;
    }
}
