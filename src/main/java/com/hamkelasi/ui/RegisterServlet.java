package com.hamkelasi.ui;


import com.hamkelasi.bll.refactored.registration.RealUserService;
import com.hamkelasi.bll.refactored.shared.SystemClock;
import com.hamkelasi.ui.refactored.RegistrationPresenter;
import com.hamkelasi.ui.refactored.RegistrationView;
import com.hamkelasi.ui.refactored.cookies.RealCookie;
import com.hamkelasi.ui.refactored.file_upload.RealProfileUploader;
import com.hamkelasi.ui.refactored.file_upload.Uploader;
import com.hamkelasi.ui.refactored.session_management.RealSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/register")
@MultipartConfig
public class RegisterServlet extends HttpServlet implements RegistrationView {

    private String labelError = "";
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Part uploadPicture;
    private RegistrationPresenter presenter;
    private Uploader profileImage;

    public RegisterServlet(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.request = request;
        this.response = response;
        HttpSession session = request.getSession();

        // Check for UserID cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName()) && session.getAttribute("UserID") == null) {
                    session.setAttribute("UserID", cookie.getValue());
                }
            }
        }

        // Check query parameter for action
        String action = request.getParameter("action");
        if ("successfull".equals(action)) {
            request.setAttribute("panelRegisterVisible", false);
            request.setAttribute("labelError", "ثبت نام با موفقیت انجام گردید");
        } else if (session.getAttribute("UserID") != null) {
            request.setAttribute("panelRegisterVisible", false);
            request.setAttribute("labelError", "شما عضو سایت می باشید. ثبت نام مجدد امکان پذیر نمی باشد");
        } else {
            request.setAttribute("panelRegisterVisible", true);
        }

        // Forward to JSP (equivalent to rendering ASPX page)
        request.getRequestDispatcher("/WEB-INF/templates/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initPresenter(request, response);
        presenter.register();
/*        this.request=request;
        this.response = response;
        HttpSession session = request.getSession();

        // Mimic Page.IsValid (basic check for required fields)
        if (isValidForm(request)) {
            User newUser = new User();
            String username = request.getParameter("textUsername");
            String email = request.getParameter("textEmail");
            String website = request.getParameter("textWebsite");

            int retVal = newUser.isValid(username, email, website);

            switch (retVal) {
                case 0:
                    newUser.setUsername(username);
                    newUser.setPassword(request.getParameter("textPassword"));
                    newUser.setFirstname(request.getParameter("textFirstname"));
                    newUser.setLastname(request.getParameter("textLastname"));
                    newUser.setEmail(email);
                    newUser.setWebsite(website);
                    newUser.setRegisterDate(LocalDateTime.now());
                    newUser.setPmActivate("on".equals(request.getParameter("checkPmActivate")));
                    newUser.setPermission(3); // Regular user

                    // Handle file upload
                    uploadPicture = request.getPart("uploadPicture");
                    if (uploadPicture != null && uploadPicture.getSize() > 0) {
                        String filename = uploadPicture.getSubmittedFileName();
                        String savePath = getServletContext().getRealPath("/UserImages/") + File.separator + filename;
                        File uploadDir = new File(getServletContext().getRealPath("/UserImages/"));
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }
                        uploadPicture.write(savePath);
                        newUser.setProfilePicture("/UserImages/" + filename);
                    } else {
                        newUser.setProfilePicture("");
                    }

                    int valInsert = newUser.add();
                    if (valInsert == 0) {
                        session.setAttribute("UserID", newUser.getId());
                        Cookie userIdCookie = new Cookie("UserID", String.valueOf(newUser.getId()));
                        userIdCookie.setMaxAge(30 * 24 * 60 * 60); // 1 month in seconds
                        response.addCookie(userIdCookie);
                        response.sendRedirect(request.getContextPath() + "/register?action=successfull");
                        return;
                    } else if (valInsert == 9) {
                        labelError = "خطا در ثبت داده";
                        request.setAttribute("labelError", labelError);
                    }
                    break;
                case 1:
                    labelError = "ایمیل وارد شده تکراری می باشد";
                    request.setAttribute("labelError", labelError);
                    break;
                case 2:
                    labelError = "نام کاربری وارد شده تکراری می باشد";
                    request.setAttribute("labelError", labelError);
                    break;
                case 3:
                    labelError = "فرمت ایمیل وارد شده نادرست می باشد";
                    request.setAttribute("labelError", labelError);
                    break;
                case 4:
                    labelError = "فرمت وب سایت وارد شده نادرست می باشد";
                    request.setAttribute("labelError", labelError);
                    break;
            }
        }*/

        //request.setAttribute("panelRegisterVisible", true);
        //request.getRequestDispatcher("/WEB-INF/templates/register.jsp").forward(request, response);
    }

    private void initPresenter(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.presenter = new RegistrationPresenter(this, new RealUserService(), new RealCookie(this.request, this.response), new RealSession(this.request), new SystemClock());
    }

    private boolean isValidForm(HttpServletRequest request) {
        // Basic validation to mimic Page.IsValid
        return request.getParameter("textUsername") != null &&
                request.getParameter("textPassword") != null &&
                request.getParameter("textEmail") != null &&
                !request.getParameter("textFirstname").isEmpty() &&
                !request.getParameter("textLastname").isEmpty();
    }

    @Override
    public String username() {
        return request.getParameter("textUsername");
    }

    @Override
    public String password() {
        return request.getParameter("textPassword");
    }

    @Override
    public String firstname() {
        return request.getParameter("textFirstname");
    }

    @Override
    public String lastname() {
        return request.getParameter("textLastname");
    }

    @Override
    public String email() {
        return request.getParameter("textEmail");
    }

    @Override
    public String website() {
        return request.getParameter("textWebsite");
    }

    @Override
    public boolean isPmActivate() {
        return "on".equals(request.getParameter("checkPmActivate"));
    }

    @Override
    public void showError(String errorText) {
        request.setAttribute("labelError", errorText);
    }

    @Override
    public void redirectToSuccessfulView(String to) {
        try {
            request.getRequestDispatcher(request.getContextPath() + to).forward(request, response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isPageValid() {
        return isValidForm(request);
    }

    @Override
    public Uploader profileImage() {
        return new RealProfileUploader(uploadPicture, this);
    }

    public void setProfileImage(Uploader uploader) {
        profileImage = uploader;
    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return request;
    }

    @Override
    public HttpServletResponse getHttpResponse() {
        return response;
    }

    @Override
    public void setMessage(String message) {
        request.setAttribute("message",message);
    }
}