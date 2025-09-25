package com.hamkelasi.ui.unit;

import com.hamkelasi.bll.refactored.RegisterUserDTO;
import com.hamkelasi.bll.refactored.UserService;
import com.hamkelasi.ui.refactored.RegistrationPresenter;
import com.hamkelasi.ui.test_double.*;
import com.tngtech.jgiven.Stage;
import org.mockito.Mockito;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegistrationSteps extends Stage<RegistrationSteps> {

    private final SpyUserService userService;
    private final StubRegistrationView view;
    private final RegistrationPresenter presenter;

    public RegistrationSteps() {
        view = new StubRegistrationView();
        userService = new SpyUserService();
        var fakeSession = new FakeSession();
        var fakeCookie = new FakeCookie();
        presenter = new RegistrationPresenter(view, userService, fakeCookie, fakeSession);
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

    public void user_does_not_provide_any_profile_picture() {
        final StubUploader stubUploader = new StubUploader();
        stubUploader.setHasFile(false);
        view.setProfileImage(stubUploader);
    }

    public void user_registered_successfully(Consumer<RegisterUserDTO> configurator) {
        final RegisterUserDTO expectedDto = new RegisterUserDTO();
        configurator.accept(expectedDto);
        var actualDto = userService.getRegisteredDto();

        assertThat(userService.calledTimes()).isEqualTo(1);
        assertThat(userService.register(any())).isZero();
        assertThat(actualDto).usingRecursiveComparison()
                .ignoringFields("permission","registerDate","profilePicture").isEqualTo(expectedDto);
    }

    public void user_registered_with_normal_permission() {
        final RegisterUserDTO actualDto = userService.getRegisteredDto();

        assertThat(actualDto.permission).isEqualTo(3);
    }
}
