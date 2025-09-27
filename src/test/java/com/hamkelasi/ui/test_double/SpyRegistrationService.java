package com.hamkelasi.ui.test_double;

import com.hamkelasi.bll.refactored.registration.RegisterUserDTO;
import com.hamkelasi.bll.refactored.registration.RegistrationService;

public class SpyRegistrationService implements RegistrationService {
    private int validationResult;
    private RegisterUserDTO registerDTO;
    private int registrationCalled;
    private int registrationResult;

    public void setRegistrationResult(int resultCode){
        registrationResult = resultCode;
    }

    public void setValidationResult(int result) {
        this.validationResult = result;
    }

    @Override
    public int register(RegisterUserDTO registerUserDto) {
        this.registerDTO = registerUserDto;
        registrationCalled++;
        return registrationResult;
    }

    @Override
    public int isValid(String username, String email, String website) {
        return validationResult;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public RegisterUserDTO getRegisteredDto() {
        return registerDTO;
    }

    public int calledTimes() {
        return registrationCalled;
    }

    public int getRegistrationResultCode() {
        return registrationResult;
    }
}
