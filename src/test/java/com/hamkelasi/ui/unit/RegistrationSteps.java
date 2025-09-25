package com.hamkelasi.ui.unit;

import com.hamkelasi.bll.refactored.UserService;
import com.hamkelasi.ui.refactored.RegistrationPresenter;
import com.hamkelasi.ui.test_double.FakeCookie;
import com.hamkelasi.ui.test_double.FakeSession;
import com.hamkelasi.ui.test_double.StubRegistrationView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegistrationSteps {

    private final UserService userService;
    private final StubRegistrationView view;
    private final RegistrationPresenter presenter;

    public RegistrationSteps() {
        view = new StubRegistrationView();
        userService = mock(UserService.class);
        var fakeSession = new FakeSession();
        var fakeCookie = new FakeCookie();
        presenter = new RegistrationPresenter(view, userService, fakeCookie, fakeSession);
    }

    public void validationsFailsWithErrorCode(int errorCode) {
        when(userService.isValid(any(),any(),any())).thenReturn(errorCode);
    }

    public void userTriesToRegister() {
        presenter.register();
    }

    public void errorDisplayOnScreen(String error) {
        assertThat(view.getDisplayErrors()).contains(error);
    }
}
