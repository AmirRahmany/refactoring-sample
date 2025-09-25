package com.hamkelasi.ui.refactored;

import com.hamkelasi.bll.refactored.RealUserService;
import com.hamkelasi.bll.refactored.RegisterUserDTO;
import com.hamkelasi.bll.refactored.UserService;
import com.hamkelasi.ui.refactored.cookies.MyCookie;
import com.hamkelasi.ui.refactored.file_upload.Uploader;
import com.hamkelasi.ui.refactored.session_management.Session;

import java.io.File;
import java.time.LocalDateTime;

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
                    dto.username = username;
                    dto.password = view.password();
                    dto.firstName = view.firstname();
                    dto.lastName = view.lastname();
                    dto.email = view.email();
                    dto.website = view.website();
                  dto.isPmActive = view.isPmActivate();
                  dto.permission=3;

                  userService.register(dto);
                  // dto.registerDate = LocalDateTime.now();
                    //dto.permission = REGULAR_USER;
//
//                    // Handle file upload
//                    uploadPicture = request.getPart("uploadPicture");
//                    if (uploadPicture != null && uploadPicture.getSize() > 0) {
//                        String filename = uploadPicture.getSubmittedFileName();
//                        String savePath = getServletContext().getRealPath("/UserImages/") + File.separator + filename;
//                        File uploadDir = new File(getServletContext().getRealPath("/UserImages/"));
//                        if (!uploadDir.exists()) {
//                            uploadDir.mkdirs();
//                        }
//                        uploadPicture.write(savePath);
//                        newUser.setProfilePicture("/UserImages/" + filename);
//                    } else {
//                        newUser.setProfilePicture("");
//                    }
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
