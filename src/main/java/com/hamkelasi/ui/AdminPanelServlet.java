package com.hamkelasi.ui;


import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/adminPanel")
public class AdminPanelServlet extends HttpServlet {
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

        // Check user authentication and permission
        boolean isAuthenticated = false;
        if (session.getAttribute("UserID") != null) {
            int loggedID = Integer.parseInt((String) session.getAttribute("UserID"));
            User loggedUser = new User(loggedID);
            if (loggedUser.getPermission() == 1) {
                isAuthenticated = true;
            }
        }

        // Set attributes for JSP
        if (!isAuthenticated) {
            request.setAttribute("panelAdminVisible", false);
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
        } else {
            request.setAttribute("panelAdminVisible", true);
        }

        // Forward to JSP (equivalent to rendering ASPX page)
        request.getRequestDispatcher("/WEB-INF/templates/admin-panel.jsp").forward(request, response);
    }
}
