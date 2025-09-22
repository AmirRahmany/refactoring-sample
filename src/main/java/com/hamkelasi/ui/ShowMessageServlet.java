package com.hamkelasi.ui;


import com.hamkelasi.bll.IranianCalendar;
import com.hamkelasi.bll.Message;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@WebServlet("/show-message")
public class ShowMessageServlet extends HttpServlet {
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
        String cellSenderReceiver = "";
        String labelSenderReceiver = "";
        String labelDate = "";
        String labelSubject = "";
        String textPM = "";
        boolean panelShowMessageVisible = true;
        String labelError = null;

        if (session.getAttribute("UserID") != null) {
            String messageIDStr = request.getParameter("ID");
            if (messageIDStr != null && !messageIDStr.isEmpty()) {
                try {
                    int messageID = Integer.parseInt(messageIDStr);
                    int userID = Integer.parseInt((String) session.getAttribute("UserID"));
                    Message message = new Message(messageID);
                    boolean isAuthenticated = false;

                    if (message.getReceiverUser() == userID) {
                        cellSenderReceiver = "فرستنده : ";
                        User user = new User(message.getSenderUser());
                        labelSenderReceiver = user.getUsername();
                        isAuthenticated = true;
                        if (message.getStatus() == Message.MessageStatus.UNREAD) {
                            message.markAsRead();
                        }
                    } else if (message.getSenderUser() == userID) {
                        cellSenderReceiver = "دریافت کننده : ";
                        User user = new User(message.getReceiverUser());
                        labelSenderReceiver = user.getUsername();
                        isAuthenticated = true;
                    }

                    if (isAuthenticated) {
                        IranianCalendar shamsiDate = new IranianCalendar(toDate(message.getDate()));
                        labelDate = shamsiDate.toString();
                        labelSubject = message.getSubject();
                        textPM = message.getText();
                    } else {
                        panelShowMessageVisible = false;
                        labelError = "شما مجوز دسترسی به این صفحه را ندارید";
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/user-panel");
                    return;
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/user-panel");
                return;
            }
        } else {
            panelShowMessageVisible = false;
            labelError = "شما مجوز دسترسی به این صفحه را ندارید";
        }

        // Set attributes for JSP
        request.setAttribute("cellSenderReceiver", cellSenderReceiver);
        request.setAttribute("labelSenderReceiver", labelSenderReceiver);
        request.setAttribute("labelDate", labelDate);
        request.setAttribute("labelSubject", labelSubject);
        request.setAttribute("textPM", textPM);
        request.setAttribute("panelShowMessageVisible", panelShowMessageVisible);
        request.setAttribute("labelError", labelError);

        request.getRequestDispatcher("/WEB-INF/templates/show-message.jsp").forward(request, response);
    }

    private Date toDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.of("UTC")).toInstant());
    }
}