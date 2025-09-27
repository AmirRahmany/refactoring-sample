package com.hamkelasi.ui.unit;

import com.hamkelasi.bll.refactored.permissions.Permissions;
import com.hamkelasi.bll.refactored.registration.RegisterUserDTO;
import com.hamkelasi.ui.refactored.RegistrationPresenter;
import com.hamkelasi.ui.test_double.*;
import com.tngtech.jgiven.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class RegistrationSteps extends Stage<RegistrationSteps> {

    public static final int ONE_MONTH = 30 * 24 * 60 * 60;
    private final SpyRegistrationService userService;
    private final StubRegistrationView view;
    private final RegistrationPresenter presenter;
    private final StubClock clock;
    private final FakeSession session;
    private final FakeCookie cookie;

    public RegistrationSteps() {
        view = new StubRegistrationView();
        userService = new SpyRegistrationService();
        session = new FakeSession();
        cookie = new FakeCookie();
        clock = new StubClock();
        presenter = new RegistrationPresenter(view, userService, cookie, session, clock);
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
        assertThat(userService.register(any()).resultCode).isZero();
        assertThat(actualDto).usingRecursiveComparison()
                .ignoringFields("permission", "registerDate", "profilePicture").isEqualTo(expectedDto);
    }

    public void user_registered_with_normal_permission() {
        final RegisterUserDTO actualDto = userService.getRegisteredDto();

        assertThat(actualDto.permission).isEqualTo(Permissions.NORMAL_USER);
    }

    public void currentTimeIs(String dateTime) {
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

    public void registrationProcessIsFailing() {
        userService.setRegistrationResult(9);
    }

    public void sessionFilledWithUserId() {
        final int expectedUserId = userService.getUserId();
        assertThat(session.get("UserID")).isEqualTo(expectedUserId);
    }

    public RegistrationSteps cookieSetWithUserId() {
        final int expectedUserId = userService.getUserId();
        final int actualUserId = Integer.parseInt(cookie.get("UserID").toString());
        assertThat(actualUserId).isEqualTo(expectedUserId);

        return self();
    }

    public void userIdCookieExpiredAfterOneMonth() {
        final int actualExpireDate = cookie.getExpireDateOf("UserID");
        assertThat(actualExpireDate).isEqualTo(ONE_MONTH);
    }

    public void cookieFilledWithExpireDateOf(String dateTime) {
        final LocalDateTime expiredDateTime = LocalDateTime.parse(dateTime, getDateTimeFormatter());


        assertThat(expiredDateTime.getSecond()).isEqualTo(cookie.getExpireDateOf("UserID"));

    }

    private static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
    }

    public void userRedirectToSuccessfulPage() {

        assertThat(view.getRedirectionCalls()).isEqualTo(1);
    }
}
