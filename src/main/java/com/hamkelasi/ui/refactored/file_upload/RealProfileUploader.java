package com.hamkelasi.ui.refactored.file_upload;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

public class RealProfileUploader implements Uploader {

    HttpServlet httpServlet;
    private final Part filePart;

    public RealProfileUploader(Part filePart, HttpServlet httpServlet) {
        this.filePart = filePart;
        this.httpServlet = httpServlet;
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
