package com.hamkelasi.ui.refactored.file_upload;

import jakarta.servlet.ServletException;

import java.io.IOException;

public interface Uploader {

    String getFileName();

    String write(String savedPath) throws ServletException, IOException;
}
