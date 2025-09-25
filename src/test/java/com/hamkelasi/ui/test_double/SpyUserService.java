package com.hamkelasi.ui.test_double;

import com.hamkelasi.bll.refactored.RegisterUserDTO;
import com.hamkelasi.bll.refactored.UserService;

public class SpyUserService implements UserService {
    private int validationResult;
    private RegisterUserDTO registerDTO;
    private int registrationCalled;

    public void setValidationResult(int result) {
        this.validationResult = result;
    }

    @Override
    public int register(RegisterUserDTO registerUserDto) {
        this.registerDTO = registerUserDto;
        registrationCalled++;
        return 0;
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
}
