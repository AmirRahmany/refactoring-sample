package com.hamkelasi.ui.refactored.uploader;

import jakarta.servlet.ServletException;

import java.io.IOException;

public interface ImageUploader {

    String upload(String fileName,String savedPath) throws ServletException, IOException;
}
