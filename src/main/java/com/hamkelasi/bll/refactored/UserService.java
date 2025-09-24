package com.hamkelasi.bll.refactored;

public interface UserService {
    int isValid(String username,String email,String website);
    void register(RegisterUserDto dto);
}
