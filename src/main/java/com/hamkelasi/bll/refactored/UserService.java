package com.hamkelasi.bll.refactored;

public interface UserService {
    int register(RegisterUserDTO registerUserDto);

    int isValid(String username, String email, String website);

    int getId();
}
