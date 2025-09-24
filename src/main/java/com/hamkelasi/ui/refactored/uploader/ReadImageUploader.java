package com.hamkelasi.ui.refactored.uploader;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

public class ReadImageUploader implements ImageUploader {
    private HttpServlet servlet;
    private HttpServletRequest request;

    public ReadImageUploader(HttpServletRequest request) {
        this.request = request;
    }


    @Override
    public String upload(String fileName, String dirPath) throws ServletException, IOException {
        Part filePart = request.getPart("uploadPicture");
        final ServletContext servletContext = servlet.getServletContext();
        if (filePart != null && filePart.getSize() > 0) {
            String filename = filePart.getSubmittedFileName();
            String savePath = servletContext.getRealPath(dirPath) + File.separator + filename;
            File uploadDir = new File(servletContext.getRealPath(dirPath));
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            filePart.write(savePath);
            return savePath;
        }
        return "";
    }
}
