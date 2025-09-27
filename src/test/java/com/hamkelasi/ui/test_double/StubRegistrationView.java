package com.hamkelasi.ui.test_double;

import com.hamkelasi.ui.refactored.RegistrationView;
import com.hamkelasi.ui.refactored.file_upload.Uploader;
import com.hamkelasi.ui.test_utils.RegistrationTestInfo;
import com.hamkelasi.ui.test_utils.RegistrationTestInfo.Mehdi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StubRegistrationView implements RegistrationView {
    private final List<String> errors = new ArrayList<>();
    private String username = Mehdi.USERNAME;
    private String password = Mehdi.PASSWORD;
    private String firstname = Mehdi.FIRST_NAME;
    private String lastname = Mehdi.LAST_NAME;
    private String email = Mehdi.EMAIL;
    private String website = Mehdi.WEBSITE;
    private boolean isPmActivate = Mehdi.IS_PM_ACTIVE;

    public StubRegistrationView withUsername(String username) {
        this.username = username;
        return this;
    }

    public StubRegistrationView withPassword(String password) {
        this.password = password;
        return this;
    }

    public StubRegistrationView withFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public StubRegistrationView withLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public StubRegistrationView withEmail(String email) {
        this.email = email;
        return this;
    }

    public StubRegistrationView withWebsite(String website) {
        this.website = website;
        return this;
    }

    public StubRegistrationView withPmActivate(boolean isPmActivate) {
        this.isPmActivate = isPmActivate;
        return this;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public String firstname() {
        return firstname;
    }

    @Override
    public String lastname() {
        return lastname;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String website() {
        return website;
    }

    @Override
    public boolean isPmActivate() {
        return isPmActivate;
    }

    @Override
    public void showError(String errorText) {
        errors.add(errorText);
    }

    @Override
    public void redirectToSuccessfulView(String to) {

    }

    @Override
    public boolean isPageValid() {
        return true;
    }

    @Override
    public Uploader profileImage() {
        final Uploader uploader = mock(Uploader.class);
        when(uploader.write(any())).thenReturn("");
        when(uploader.getFileName()).thenReturn("");
        return uploader;
    }

    @Override
    public void setProfileImage(Uploader uploader) {

    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return null;
    }

    @Override
    public HttpServletResponse getHttpResponse() {
        return null;
    }

    @Override
    public void setMessage(String message) {

    }

    public List<String> getDisplayErrors() {
        return errors;
    }
}
