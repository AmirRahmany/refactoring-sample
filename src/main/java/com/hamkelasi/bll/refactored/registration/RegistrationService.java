package com.hamkelasi.bll.refactored.registration;

public interface RegistrationService {
    int register(RegisterUserDTO registerUserDto);

    int isValid(String username, String email, String website);

    int getId();

    RegisterUserDTO getRegisteredDto();
}
