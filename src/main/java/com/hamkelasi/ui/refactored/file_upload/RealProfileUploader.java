package com.hamkelasi.ui.refactored.file_upload;

import jakarta.servlet.http.Part;

import java.io.IOException;

public class RealProfileUploader implements Uploader {

    private final Part filePart;

    public RealProfileUploader(Part filePart) {
        this.filePart = filePart;
    }


    @Override
    public boolean hasFile() {
        return (filePart != null && filePart.getSize() > 0);
    }

    @Override
    public String getFileName() {
        return filePart.getSubmittedFileName();
    }

    @Override
    public void upload(String savedPath) {
        try {
            filePart.write(savedPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
