package com.hamkelasi.ui.refactored.file_upload;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

public class RealProfileUploader implements Uploader {

    HttpServlet httpServlet;
    private final Part filePart;

    public RealProfileUploader(Part filePart,HttpServlet httpServlet) {
        this.filePart = filePart;
        this.httpServlet = httpServlet;
    }


    @Override
    public String getFileName() {
        return filePart.getSubmittedFileName();
    }

    @Override
    public String write(String dir){
        if (filePart == null || filePart.getSize() <= 0) return "";
        String savedPath = "";
        final String realPath = httpServlet.getServletContext().getRealPath(dir);
        File uploadDir = new File(realPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        try {
            savedPath = realPath + File.separator + getFileName();
            filePart.write(savedPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return savedPath;
    }
}
