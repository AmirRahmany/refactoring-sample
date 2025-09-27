package com.hamkelasi.ui.test_double;

import com.hamkelasi.ui.refactored.RegistrationView;
import com.hamkelasi.ui.refactored.file_upload.MyFile;
import com.hamkelasi.ui.refactored.file_upload.Uploader;
import com.hamkelasi.ui.test_utils.RegistrationTestInfo.Mehdi;

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
    private int redirectionCalls;
    private MyFile file;
    private Uploader profileImage;

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
    public void redirectToSuccessfulPage() {
        redirectionCalls++;
    }

    @Override
    public boolean isPageValid() {
        return true;
    }

    @Override
    public Uploader profileImage() {
        return profileImage;
    }

    @Override
    public void setProfileImage(Uploader uploader) {
        this.profileImage = uploader;
    }

    @Override
    public void setMessage(String message) {

    }

    @Override
    public MyFile getUploadDirectoryFile() {
        return file;
    }

    @Override
    public void setFileDirectoryMaker(MyFile file) {
        this.file = file;
    }

    public List<String> getDisplayErrors() {
        return errors;
    }


    public int getRedirectionCalls() {
        return redirectionCalls;
    }
}
