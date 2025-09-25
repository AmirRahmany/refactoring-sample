package com.hamkelasi.ui.refactored;

import com.hamkelasi.bll.refactored.RealUserService;
import com.hamkelasi.bll.refactored.RegisterUserDTO;
import com.hamkelasi.bll.refactored.UserService;
import com.hamkelasi.ui.refactored.cookies.MyCookie;
import com.hamkelasi.ui.refactored.session_management.Session;

public class RegistrationPresenter {
    public static final int REGULAR_USER = 3;
    private final RegistrationView view;
    private final UserService userService;
    private final MyCookie cookie;
    private final Session session;

    public RegistrationPresenter(RegistrationView view, UserService userService, MyCookie cookie, Session session) {
        this.view = view;
        this.userService = userService;
        this.cookie = cookie;
        this.session = session;
    }

    public RegistrationPresenter(RegistrationView view, MyCookie cookie, Session session) {
        this.view = view;
        this.cookie = cookie;
        this.session = session;
        this.userService = new RealUserService();
    }

    public void register() {
        if (view.isPageValid()) {
            String username = view.username();
            String email = view.email();
            String website = view.website();

            int retVal = userService.isValid(username, email, website);

            final RegisterUserDTO dto = new RegisterUserDTO();
            switch (retVal) {
                case 0:
                    /*dto.username = username;
                    dto.password = view.password();
                    dto.firstName = view.firstname();
                    dto.lastName = view.lastname();
                    dto.email = view.email();
                    dto.website = view.website();
                    dto.registerDate = LocalDateTime.now();
                    dto.isPmActive = view.isPmActivate();
                    dto.permission = REGULAR_USER;

                    // Handle file upload
                    final Uploader profilePicture = view.profileImage();
                    dto.profilePicture = profilePicture.write("/UserImages/");


                    int valInsert = userService.register(dto);
                    if (valInsert == 0) {
                        session.set("UserID", userService.getId());
                        final int expiredTime = 30 * 24 * 60 * 60;// 1 month in seconds
                        cookie.add("UserID",userService.getId(),expiredTime);
                        view.redirectToSuccessfulView("/register?action=successfull");
                    } else if (valInsert == 9) {
                        view.showError("خطا در ثبت داده");
                    }*/
                    break;
                case 1:
                  view.showError("ایمیل وارد شده تکراری می باشد");
                    break;
                case 2:
                   view.showError("نام کاربری وارد شده تکراری می باشد");
                    break;
                case REGULAR_USER:
                    view.showError("فرمت ایمیل وارد شده نادرست می باشد");
                    break;
                case 4:
                    view.showError("فرمت وب سایت وارد شده نادرست می باشد");
                    break;
            }
        }
    }
}
