package com.hamkelasi.ui.refactored.file_upload;


public interface Uploader {

    boolean hasFile();

    String getFileName();

    void upload(String dir);
}
