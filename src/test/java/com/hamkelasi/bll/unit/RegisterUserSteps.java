package com.hamkelasi.bll.unit;

import com.hamkelasi.bll.refactored.registration.RealRegistrationService;
import com.hamkelasi.bll.refactored.registration.UserValidationResults;
import com.hamkelasi.dal.refactored.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterUserSteps {

    private final RealRegistrationService registrationService;

    private final UserRepository userRepository;

    private int result;

    public RegisterUserSteps() {
        userRepository = mock(UserRepository.class);
        this.registrationService = new RealRegistrationService(userRepository);
    }

    public void thereIsAUserWithEmail(String email) {
        when(userRepository.getUserCountByEmail(email)).thenReturn(1);
    }

    public void heShouldBeGetDuplicateEmailError() {
        assertThat(result).isEqualTo(UserValidationResults.EMAIL_IS_DUPLICATED);
    }

    public void thereIsAUserWithUsername(String username) {
        when(userRepository.getUserCountByUsername(username)).thenReturn(1);
    }

    public void someoneValidatesTheRegistration(String username, String email, String website) {
        result = registrationService.isValid(username,email,website);
    }

    public void heShouldBeGetDuplicateUsernameError() {
        assertThat(result).isEqualTo(UserValidationResults.USERNAME_IS_DUPLICATED);
    }

    public void heShouldBeGetInvalidEmailFormatError() {
        assertThat(result).isEqualTo(UserValidationResults.INVALID_EMAIL_FORMAT);
    }


    public void heShouldBeGetInvalidWebsiteFormatError() {
        assertThat(result).isEqualTo(UserValidationResults.INVALID_WEBSITE_FORMAT);
    }



    public void heShouldBeRegisteredSuccessfully() {
        assertThat(result).isEqualTo(UserValidationResults.SUCCESSFUL);
    }
}
