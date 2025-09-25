package com.hamkelasi.ui.unit;

import com.tngtech.jgiven.junit5.ScenarioTest;
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
}

