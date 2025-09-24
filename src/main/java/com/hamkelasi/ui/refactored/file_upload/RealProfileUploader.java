package com.hamkelasi.ui.refactored.file_upload;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

public class RealProfileUploader implements Uploader {

    private final Part filePart;

    public RealProfileUploader(Part filePart) {
        this.filePart = filePart;
    }


    @Override
    public String getFileName() {
        return filePart.getSubmittedFileName();
    }

    @Override
    public String write(String savedPath) throws IOException {
        if (filePart == null || filePart.getSize() <= 0) return "";

        File uploadDir = new File(savedPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        filePart.write(savedPath);
        return savedPath;
    }
}
