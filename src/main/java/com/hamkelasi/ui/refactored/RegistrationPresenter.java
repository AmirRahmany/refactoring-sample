package com.hamkelasi.ui.refactored;

import com.hamkelasi.bll.refactored.permissions.Permissions;
import com.hamkelasi.bll.refactored.registration.RealRegistrationService;
import com.hamkelasi.bll.refactored.registration.RegisterUserDTO;
import com.hamkelasi.bll.refactored.registration.RegistrationResult;
import com.hamkelasi.bll.refactored.registration.RegistrationService;
import com.hamkelasi.bll.refactored.shared.Clock;
import com.hamkelasi.bll.refactored.shared.SystemClock;
import com.hamkelasi.ui.refactored.cookies.MyCookie;
import com.hamkelasi.ui.refactored.file_upload.MyFile;
import com.hamkelasi.ui.refactored.file_upload.RealFile;
import com.hamkelasi.ui.refactored.file_upload.RealProfileUploader;
import com.hamkelasi.ui.refactored.session_management.Session;

public class RegistrationPresenter {
    public static final int REGULAR_USER = 3;
    public static final int EXPIRED_TIME = 30 * 24 * 60 * 60; // 1 month
    private final RegistrationView view;
    private final RegistrationService userService;
    private final MyCookie cookie;
    private final Session session;
    private final Clock clock;

    public RegistrationPresenter(RegistrationView view, RegistrationService userService, MyCookie cookie, Session session, Clock clock) {
        this.view = view;
        this.userService = userService;
        this.cookie = cookie;
        this.session = session;
        this.clock = clock;
    }

    public RegistrationPresenter(RegistrationView view, MyCookie cookie, Session session) {
        this.view = view;
        this.cookie = cookie;
        this.session = session;
        this.clock = new SystemClock();
        this.userService = new RealRegistrationService();
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
                    dto.username = username;
                    dto.password = view.password();
                    dto.firstName = view.firstname();
                    dto.lastName = view.lastname();
                    dto.email = view.email();
                    dto.website = view.website();
                    dto.isPmActive = view.isPmActivate();
                    dto.permission = Permissions.NORMAL_USER;
                    dto.registerDate = clock.now();


                    var profileImage = view.profileImage();
                    if (profileImage.hasFile()) {
                        String filename = profileImage.getFileName();
                        final MyFile uploadDir = view.getUploadDirectoryFile();
                        String savePath = uploadDir.getSavedPathOf(filename);
                        if (!uploadDir.dirExists()) {
                            uploadDir.makeDir();
                        }
                        profileImage.upload(savePath);
                        dto.profilePicture = ("/images/" + filename);
                    } else {
                        dto.profilePicture = ("/images/default.png");
                    }
                    final RegistrationResult registrationResult = userService.register(dto);
                    final int result = registrationResult.resultCode;
                    if (result == 0) {
                        session.set("UserID", registrationResult.userId);
                        cookie.add("UserID", registrationResult.userId, clock.now().plusMonths(1));
                        view.setMessage("ثبت نام شما با موفقیت انجام شد");
                        view.redirectToSuccessfulPage();
                        return;
                    } else if (result == 9) {
                        view.showError("خطا در ثبت داده");
                    }
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
        view.redirectToSuccessfulPage();
    }
}
