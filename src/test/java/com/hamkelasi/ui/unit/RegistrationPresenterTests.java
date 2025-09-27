package com.hamkelasi.ui.unit;

import com.hamkelasi.ui.test_utils.RegistrationTestInfo.Mehdi;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RegistrationPresenterTests extends ScenarioTest<RegistrationSteps, RegistrationSteps, RegistrationSteps> {

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
                    d.withUsername(Mehdi.USERNAME)
                            .withPassword(Mehdi.PASSWORD)
                            .withFirstname(Mehdi.FIRST_NAME)
                            .withLastname(Mehdi.LAST_NAME)
                            .withEmail(Mehdi.EMAIL)
                            .withWebsite(Mehdi.WEBSITE)
                            .withPmActivate(true);

                })
                .and().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture();
        when().userTriesToRegister();
        then().user_registered_successfully(dto -> {
            dto.username = Mehdi.USERNAME;
            dto.password = Mehdi.PASSWORD;
            dto.firstName = Mehdi.FIRST_NAME;
            dto.lastName = Mehdi.LAST_NAME;
            dto.email = Mehdi.EMAIL;
            dto.website = Mehdi.WEBSITE;
            dto.isPmActive = Mehdi.IS_PM_ACTIVE;
        });
    }

    @Test
    void registers_user_with_user_normal_permission() {
        given().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture();
        when().userTriesToRegister();
        then().user_registered_with_normal_permission();
    }

    @Test
    void sets_now_as_a_register_date_of_user() {
        given().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture()
                .and().currentTimeIs("2025-08-27 03:41:00");
        when().userTriesToRegister();
        then().registerDateOfUserSetTo("2025-08-27 03:41:00");
    }

    @Test
    void shown_an_error_when_registration_fails() {
        var error = "خطا در ثبت داده";
        given().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture()
                .and().registrationProcessIsFailing();
        when().userTriesToRegister();
        then().errorDisplayOnScreen(error);
    }

    @Test
    void user_id_added_to_session_after_successful_registration() {
        given().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture();
        when().userTriesToRegister();
        then().sessionFilledWithUserId();
    }

    @Test
    void user_id_added_to_cookie_after__successful_registration() {
        given().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture();
        when().userTriesToRegister();
        then().cookieSetWithUserId();
    }

    @Test
    void expire_date_for_cookie_is_set_to_one_month() {
        given().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture()
                .and().currentTimeIs("2025-08-27 18:32:00");
        when().userTriesToRegister();
        then().cookieFilledWithExpireDateOf("2025-09-27 18:32:00");
    }

    @Test
    void redirect_to_successful_page_after_successful_registration_() {
        given().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture();
        when().userTriesToRegister();
        then().userRedirectToSuccessfulPage();
    }

    @Test
    void save_user_profile_image_when_image_dir_exists() {
        given().validation_is_successful()
                .and().user_provide_a_profile_picture("image.png")
                .and().image_directory_exists_in("/UserImages/");
        when().userTriesToRegister();
        then().imageSavedPathSetAsUserProfileImage("/UserImages/image.png");
    }

    @Test
    void save_user_profile_image_and_make_dir() {
        given().validation_is_successful()
                .and().user_provide_a_profile_picture("image.png")
                .and().image_directory_does_not_exists_in("/UserImages/");
        when().userTriesToRegister();
        then().makeUserImageDirectory()
                .and().imageSavedPathSetAsUserProfileImage("/UserImages/image.png");

    }

    @Test
    void set_default_image_as_user_profile_image_when_user_does_not_provide_any_profile() {
        given().validation_is_successful()
                .and().user_does_not_provide_any_profile_picture()
                .and().set_a_default_image_for_user_profile_image("default.png");
        when().userTriesToRegister();
        then().imageSavedPathSetAsUserProfileImage("/UserImages/default.png");
    }
}

