package com.hamkelasi.ui.refactored;

import com.hamkelasi.ui.refactored.file_upload.Uploader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RegistrationView {
    String username();
    String password();
    String firstname();
    String lastname();
    String email();
    String website();
    boolean isPmActivate();
    void showError(String errorText);
    void redirectToSuccessfulView(String to);
    boolean isPageValid();
    Uploader profileImage();
    void setProfileImage(Uploader uploader);

    HttpServletRequest getHttpRequest();

    HttpServletResponse getHttpResponse();
}
