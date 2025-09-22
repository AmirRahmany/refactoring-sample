package com.hamkelasi.ui;


import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/register")
@MultipartConfig
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    Part filePart = request.getPart("uploadPicture");
                    if (filePart != null && filePart.getSize() > 0) {
                        String filename = filePart.getSubmittedFileName();
                        String savePath = getServletContext().getRealPath("/UserImages/") + File.separator + filename;
                        File uploadDir = new File(getServletContext().getRealPath("/UserImages/"));
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }
                        filePart.write(savePath);
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
                        request.setAttribute("labelError", "خطا در ثبت داده");
                    }
                    break;
                case 1:
                    request.setAttribute("labelError", "ایمیل وارد شده تکراری می باشد");
                    break;
                case 2:
                    request.setAttribute("labelError", "نام کاربری وارد شده تکراری می باشد");
                    break;
                case 3:
                    request.setAttribute("labelError", "فرمت ایمیل وارد شده نادرست می باشد");
                    break;
                case 4:
                    request.setAttribute("labelError", "فرمت وب سایت وارد شده نادرست می باشد");
                    break;
            }
        }

        request.setAttribute("panelRegisterVisible", true);
        request.getRequestDispatcher("/WEB-INF/templates/register.jsp").forward(request, response);
    }

    private boolean isValidForm(HttpServletRequest request) {
        // Basic validation to mimic Page.IsValid
        return request.getParameter("textUsername") != null &&
                request.getParameter("textPassword") != null &&
                request.getParameter("textEmail") != null &&
                !request.getParameter("textFirstname").isEmpty() &&
                !request.getParameter("textLastname").isEmpty();
    }
}