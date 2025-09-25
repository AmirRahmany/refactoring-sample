package com.hamkelasi.bll.refactored.registration;

import java.time.LocalDateTime;

public class RegisterUserDTO {
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String email;
    public String profilePicture;
    public String website;
    public boolean isPmActive;
    public int permission;
    public LocalDateTime registerDate;

    @Override
    public String toString() {
        return "RegisterUserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", website='" + website + '\'' +
                ", isPmActive=" + isPmActive +
                ", permission=" + permission +
                ", registerDate=" + registerDate +
                '}';
    }
}
