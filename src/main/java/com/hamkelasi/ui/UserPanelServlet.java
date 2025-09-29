package com.hamkelasi.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user-panel")
public class UserPanelServlet extends HttpServlet {
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

        // Initialize variables
        boolean panelProfileVisible = true;
        String labelError = null;
        String linkEditUserUrl = "";
        String linkEditFriendsUrl = "";

        if (session.getAttribute("UserID") == null) {
            panelProfileVisible = false;
            labelError = "شما مجوز دسترسی به این صفحه را ندارید";
        } else {
            try {
                int userId = Integer.parseInt(session.getAttribute("UserID").toString());
                linkEditUserUrl = request.getContextPath() + "/admin/edit-users?ID=" + userId;
                linkEditFriendsUrl = request.getContextPath() + "/edit-friends?ID=" + userId;
            } catch (NumberFormatException e) {
                panelProfileVisible = false;
                labelError = "شما مجوز دسترسی به این صفحه را ندارید";
            }
        }

        // Set attributes for JSP
        request.setAttribute("panelProfileVisible", panelProfileVisible);
        request.setAttribute("labelError", labelError);
        request.setAttribute("linkEditUserUrl", linkEditUserUrl);
        request.setAttribute("linkEditFriendsUrl", linkEditFriendsUrl);

        request.getRequestDispatcher("/WEB-INF/templates/user-panel.jsp").forward(request, response);
    }
}