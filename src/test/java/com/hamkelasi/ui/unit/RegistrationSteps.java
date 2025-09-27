package com.hamkelasi.ui.unit;

import com.hamkelasi.bll.refactored.permissions.Permissions;
import com.hamkelasi.bll.refactored.registration.RegisterUserDTO;
import com.hamkelasi.bll.refactored.shared.Clock;
import com.hamkelasi.ui.refactored.RegistrationPresenter;
import com.hamkelasi.ui.test_double.*;
import com.tngtech.jgiven.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class RegistrationSteps extends Stage<RegistrationSteps> {

    private final SpyUserService userService;
    private final StubRegistrationView view;
    private final RegistrationPresenter presenter;
    private final StubClock clock;

    public RegistrationSteps() {
        view = new StubRegistrationView();
        userService = new SpyUserService();
        var fakeSession = new FakeSession();
        var fakeCookie = new FakeCookie();
        clock = new StubClock();
        presenter = new RegistrationPresenter(view, userService, fakeCookie, fakeSession, clock);
    }

    public void validationsFailsWithErrorCode(int errorCode) {
        userService.setValidationResult(errorCode);
    }

    public void userTriesToRegister() {
        presenter.register();
    }

    public void errorDisplayOnScreen(String error) {
        assertThat(view.getDisplayErrors()).contains(error);
    }

    public RegistrationSteps user_entered_following_information(Consumer<StubRegistrationView> configurator) {
        configurator.accept(view);
        return self();
    }

    public RegistrationSteps validation_is_successful() {
        userService.setValidationResult(0);
        return self();
    }

    public RegistrationSteps user_does_not_provide_any_profile_picture() {
        final StubUploader stubUploader = new StubUploader();
        stubUploader.setHasFile(false);
        view.setProfileImage(stubUploader);
        return self();
    }

    public void user_registered_successfully(Consumer<RegisterUserDTO> configurator) {
        final RegisterUserDTO expectedDto = new RegisterUserDTO();
        configurator.accept(expectedDto);
        var actualDto = userService.getRegisteredDto();

        assertThat(userService.calledTimes()).isEqualTo(1);
        assertThat(userService.register(any())).isZero();
        assertThat(actualDto).usingRecursiveComparison()
                .ignoringFields("permission", "registerDate", "profilePicture").isEqualTo(expectedDto);
    }

    public void user_registered_with_normal_permission() {
        final RegisterUserDTO actualDto = userService.getRegisteredDto();

        assertThat(actualDto.permission).isEqualTo(Permissions.NORMAL_USER);
    }

    public void currentRegistrationDateIs(String dateTime) {
        final DateTimeFormatter formatter = getDateTimeFormatter();
        final LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        clock.timeTravelTo(localDateTime);
    }

    public void registerDateOfUserSetTo(String expectedDateTime) {
        final LocalDateTime expectedTime = LocalDateTime.parse(expectedDateTime, getDateTimeFormatter());
        final RegisterUserDTO registeredDto = userService.getRegisteredDto();
        final LocalDateTime actualDate = registeredDto.registerDate;
        assertThat(actualDate).isEqualTo(expectedTime);
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd' 'H:mm:ss");
    }
}
