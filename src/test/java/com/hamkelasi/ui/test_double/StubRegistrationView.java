package com.hamkelasi.ui.test_double;

import com.hamkelasi.ui.refactored.RegistrationView;
import com.hamkelasi.ui.refactored.file_upload.Uploader;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StubRegistrationView implements RegistrationView {
    private final List<String> errors = new ArrayList<>();

    @Override
    public String username() {
        return "foo";
    }

    @Override
    public String password() {
        return "foo123";
    }

    @Override
    public String firstname() {
        return "foo1";
    }

    @Override
    public String lastname() {
        return "foo2";
    }

    @Override
    public String email() {
        return "foo@gmail.com";
    }

    @Override
    public String website() {
        return "";
    }

    @Override
    public boolean isPmActivate() {
        return true;
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

    public List<String> getDisplayErrors(){
        return errors;
    }
}
