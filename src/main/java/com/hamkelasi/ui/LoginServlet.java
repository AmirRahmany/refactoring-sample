package com.hamkelasi.ui;


import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String CSRF_TOKEN_PARAM = "_csrf";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get request parameters
        String username = request.getParameter("TextUsername");
        String password = request.getParameter("TextPassword");
        String rememberMe = request.getParameter("CheckRemember");
        String csrfToken = request.getParameter(CSRF_TOKEN_PARAM);


        // Get session and CSRF token
        HttpSession session = request.getSession();
        String sessionCsrfToken = (String) session.getAttribute(CSRF_TOKEN_PARAM);

        // Initialize error flags
        boolean hasErrors = false;

        // CSRF validation
       /* if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("loginError", "خطای امنیتی: توکن CSRF نامعتبر است");
            hasErrors = true;
        }*/

        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("usernameError", "نام کاربری الزامی است");
            hasErrors = true;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("passwordError", "رمز عبور الزامی است");
            hasErrors = true;
        }

        // If no input errors, validate credentials
        if (!hasErrors) {
            User user = new User();
            final int result = user.login(username, password);
            // Simple in-memory validation (replace with actual authentication logic)
            if (result == 0) {
                // Set session attributes for authenticated user
                session.setAttribute("isAuthenticated", true);
                session.setAttribute("userName", username);
                session.setAttribute("UserID", user.getId());
                Cookie userIdCookie = new Cookie("UserID", String.valueOf(user.getId()));
                userIdCookie.setMaxAge(30 * 24 * 60 * 60); // 1 month in seconds
                response.addCookie(userIdCookie);

                // Set admin status (example: user "admin" is an admin)
                session.setAttribute("isAdmin", username.equals("admin"));

                // Handle "Remember Me" (example: extend session timeout)
                if ("on".equals(rememberMe)) {
                    session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
                } else {
                    session.setMaxInactiveInterval(30 * 60); // 30 minutes
                }

                // Redirect to Index.jsp on successful login
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            if (result == 2) {
                request.setAttribute("loginError", "نام کاربری یا رمز عبور اشتباه است");
                hasErrors = true;
            }
        }
        request.getRequestDispatcher("/WEB-INF/templates/index.jsp").forward(request, response);

    }
}
