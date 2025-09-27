package com.hamkelasi.bll.refactored.registration;

public interface RegistrationService {
    RegistrationResult register(RegisterUserDTO registerUserDto);

    int isValid(String username, String email, String website);


    RegisterUserDTO getRegisteredDto();

}
