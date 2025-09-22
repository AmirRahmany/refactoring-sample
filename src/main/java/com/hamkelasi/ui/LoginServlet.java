package com.hamkelasi.ui;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("loginError", "خطای امنیتی: توکن CSRF نامعتبر است");
            hasErrors = true;
        }

        // Validate input
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
            // Simple in-memory validation (replace with actual authentication logic)
            if (isValidCredentials(username, password)) {
                // Set session attributes for authenticated user
                session.setAttribute("isAuthenticated", true);
                session.setAttribute("userName", username);

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
            } else {
                request.setAttribute("loginError", "نام کاربری یا رمز عبور اشتباه است");
                hasErrors = true;
            }
        }

        // If errors, forward back to the referring page with error attributes
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            request.getRequestDispatcher(referer).forward(request, response);
        } else {
            // Fallback to Index.jsp if referer is not available
            request.getRequestDispatcher("/home").forward(request, response);
        }
    }

    // Simple in-memory credential validation (replace with database or authentication service)
    private boolean isValidCredentials(String username, String password) {
        // Example: accept "admin" with password "password123"
        return "admin".equals(username) && "password123".equals(password);
    }
}
