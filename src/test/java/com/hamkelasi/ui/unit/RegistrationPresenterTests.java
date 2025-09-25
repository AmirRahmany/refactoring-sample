package com.hamkelasi.ui.unit;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RegistrationPresenterTests extends ScenarioTest<RegistrationSteps, RegistrationSteps, RegistrationSteps> {

    @ParameterizedTest
    @CsvSource({
            "1,ایمیل وارد شده تکراری می باشد",
            "2,نام کاربری وارد شده تکراری می باشد",
            "3,فرمت ایمیل وارد شده نادرست می باشد",
            "4,فرمت وب سایت وارد شده نادرست می باشد"
    })
    void show_corresponding_error_on_failed_registration(int errorCode, String error) {
        given().validationsFailsWithErrorCode(errorCode);
        when().userTriesToRegister();
        then().errorDisplayOnScreen(error);
    }

    @Test
    void registration_successful_when_user_entered_valid_data() {
        given().user_entered_following_information(d -> {
                    d.withUsername("admin")
                            .withPassword("12345")
                            .withFirstname("hadi")
                            .withLastname("jabbari")
                            .withEmail("admin@yahoo.com")
                            .withWebsite("www.hadijabbari.com")
                            .withPmActivate(true);

                })
                .and().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture();
        when().userTriesToRegister();
        then().user_registered_successfully(dto -> {
            dto.username = "admin";
            dto.password = "12345";
            dto.firstName = "hadi";
            dto.lastName = "jabbari";
            dto.email = "admin@yahoo.com";
            dto.website = "www.hadijabbari.com";
            dto.isPmActive = true;
        });
    }

    @Test
    void registers_user_with_user_normal_permission() {
        given().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture();
        when().userTriesToRegister();
        then().user_registered_with_normal_permission();
    }
}

