package com.hamkelasi.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isSignedIn = false;

        // Check for UserID cookie and expire it
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName())) {
                    cookie.setMaxAge(0); // Expire cookie
                    cookie.setPath("/"); // Ensure cookie is accessible site-wide
                    response.addCookie(cookie);
                    isSignedIn = true;
                }
            }
        }

        // Remove UserID from session
        if (session.getAttribute("UserID") != null) {
            session.removeAttribute("UserID");
            isSignedIn = true;
        }

        // Set error message based on sign-in status
        if (isSignedIn) {
            request.setAttribute("labelError", "شما با موفقیت خارج شدید");
        } else {
            request.setAttribute("labelError", "کاربر عزیز شما هنوز وارد سایت نشده اید");
        }

        session.removeAttribute("isAuthenticated");
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/templates/logout.jsp").forward(request, response);
    }
}
