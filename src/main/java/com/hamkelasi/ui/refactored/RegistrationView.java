package com.hamkelasi.ui.refactored;

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
}
