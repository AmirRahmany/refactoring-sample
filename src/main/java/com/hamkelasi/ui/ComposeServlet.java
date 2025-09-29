package com.hamkelasi.ui;

import com.hamkelasi.bll.Message;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/compose")
public class ComposeServlet extends HttpServlet {
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

        // Handle user authentication and populate user list
        if (session.getAttribute("UserID") != null) {
            // Mimic !IsPostBack by checking if this is the initial GET request
            if (!"POST".equalsIgnoreCase(request.getMethod())) {
                int loggedUserId = Integer.parseInt( session.getAttribute("UserID").toString());
                List<User> users = new User().getOrderedList();
                List<UserItem> userItems = new ArrayList<>();
                for (User user : users) {
                    if (user.getId() != loggedUserId) {
                        userItems.add(new UserItem(user.getUsername(), String.valueOf(user.getId())));
                    }
                }
                request.setAttribute("userItems", userItems);
                request.setAttribute("panelSendPMVisible", true);
            }
        } else {
            request.setAttribute("panelSendPMVisible", false);
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
        }

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/templates/compose.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check user authentication
        if (session.getAttribute("UserID") == null) {
            request.setAttribute("panelSendPMVisible", false);
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
        } else if (isValidForm(request)) {
            Message message = new Message();
            message.setReceiverUser(Integer.parseInt(request.getParameter("listUsers")));
            message.setSubject(request.getParameter("textSubject"));
            message.setSenderUser(Integer.parseInt(session.getAttribute("UserID").toString()));
            message.setDate(LocalDateTime.now());
            message.setText(request.getParameter("textPM"));

            int result = message.add();

            if (result == 0) {
                request.setAttribute("panelSendPMVisible", false);
                request.setAttribute("labelError", "پیام خصوصی با موفقیت ارسال گردید");
            } else {
                request.setAttribute("panelSendPMVisible", false);
                request.setAttribute("labelError", "اشکال در ارسال پیام");
            }
        }

        // Re-populate user list for JSP in case of error
        if (session.getAttribute("UserID") != null) {
            int loggedUserId = Integer.parseInt(session.getAttribute("UserID").toString());
            List<User> users = new User().getOrderedList();
            List<UserItem> userItems = new ArrayList<>();
            for (User user : users) {
                if (user.getId() != loggedUserId) {
                    userItems.add(new UserItem(user.getUsername(), String.valueOf(user.getId())));
                }
            }
            request.setAttribute("userItems", userItems);
        }

        request.getRequestDispatcher("/Compose.jsp").forward(request, response);
    }

    private boolean isValidForm(HttpServletRequest request) {
        // Mimic Page.IsValid: Check for required fields
        return request.getParameter("listUsers") != null &&
                request.getParameter("textSubject") != null &&
                request.getParameter("textPM") != null &&
                !request.getParameter("listUsers").isEmpty();
    }

    // Helper class to represent dropdown items
    public static class UserItem {
        private String text;
        private String value;

        public UserItem(String text, String value) {
            this.text = text;
            this.value = value;
        }

        public String getText() { return text; }
        public String getValue() { return value; }
    }
}
