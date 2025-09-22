package com.hamkelasi.bll;

import java.util.regex.Pattern;

public class Validation {

    public boolean isEmailValid(String email) {
        Pattern regex = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        return regex.matcher(email).matches();
    }

    public boolean isEmailNotValid(String email) {
        return !isEmailValid(email);
    }

    public boolean isUrlValid(String url) {
        Pattern regex = Pattern.compile("^[a-zA-Z0-9\\-\\.]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU|IR|ir)$");
        return regex.matcher(url).matches();
    }

    public boolean isUrlNotValid(String website) {
        return !isUrlValid(website);
    }
}
