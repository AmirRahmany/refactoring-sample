package com.hamkelasi.ui.refactored.file_upload;


public interface Uploader {

    boolean hasFile();

    String getFileName();

    String write(String dir);
}
