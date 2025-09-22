package com.hamkelasi.bll.unit;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class when_registering_user extends ScenarioTest<RegisterUserSteps, RegisterUserSteps, RegisterUserSteps> {

    @Test
    public void every_user_signup_with_unique_email() {
        String email = "test@example.com";
        given().thereIsAUserWithEmail(email);
        when().someoneValidatesTheRegistration("Admin",email,"www.google.com");
        then().heShouldBeGetDuplicateEmailError();
    }

    @Test
    public void every_user_signup_with_unique_username() {
        String username = "hasan";
        given().thereIsAUserWithUsername(username);
        when().someoneValidatesTheRegistration(username,"email@example.com","www.google.com");
        then().heShouldBeGetDuplicateUsernameError();
    }

    @Test
    public void every_user_signup_with_valid_email() {
        String invalidEmailFormat = "example.com";
        when().someoneValidatesTheRegistration("Admin",invalidEmailFormat,"www.google.com");
        then().heShouldBeGetInvalidEmailFormatError();
    }

    @ParameterizedTest
    @ValueSource(strings = {"example","http://example.com"})
    public void every_user_signup_with_valid_website_if_they_have(String website) {
        when().someoneValidatesTheRegistration("Admin","email@example.com",website);
        then().heShouldBeGetInvalidWebsiteFormatError();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void user_can_be_pass_empty_and_null_website_and_registration_gets_validated(String website) {
        when().someoneValidatesTheRegistration("Admin","email@example.com",website);
        then().heShouldBeRegisteredSuccessfully();
    }

    @Test
    void user_can_be_pass_empty_website_and_registration_gets_validated() {
        when().someoneValidatesTheRegistration("Admin","email@example.com","www.yahoo.com");
        then().heShouldBeRegisteredSuccessfully();
    }
}
