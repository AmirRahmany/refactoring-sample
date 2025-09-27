package com.hamkelasi.ui.test_double;

import com.hamkelasi.bll.refactored.registration.RegisterUserDTO;
import com.hamkelasi.bll.refactored.registration.RegistrationResult;
import com.hamkelasi.bll.refactored.registration.RegistrationService;

public class SpyRegistrationService implements RegistrationService {
    private RegisterUserDTO registerDTO;
    private int registrationCalled;
    private RegistrationResult registrationResult;
    private int validationResult;

    public SpyRegistrationService() {
        registrationResult = new RegistrationResult();
    }

    public void setRegistrationResult(int resultCode) {
        registrationResult.resultCode = resultCode;
    }

    public void setValidationResult(int result) {
        this.validationResult = result;
    }

    public void setUserId(int userId){
        this.registrationResult.userId = userId;
    }

    @Override
    public RegistrationResult register(RegisterUserDTO registerUserDto) {
        this.registerDTO = registerUserDto;
        registrationCalled++;
        return registrationResult;
    }

    @Override
    public int isValid(String username, String email, String website) {
        return validationResult;
    }


    @Override
    public RegisterUserDTO getRegisteredDto() {
        return registerDTO;
    }

    public int calledTimes() {
        return registrationCalled;
    }

    public int getRegistrationResultCode() {
        return registrationResult.resultCode;
    }

    public int getUserId() {
        return registrationResult.userId;
    }
}
