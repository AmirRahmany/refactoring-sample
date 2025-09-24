package com.hamkelasi.bll.refactored;

import java.time.LocalDateTime;

public class RegisterUserDto {
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
}
