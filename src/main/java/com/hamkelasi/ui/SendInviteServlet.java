package com.hamkelasi.ui;

import com.hamkelasi.bll.Email;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/send-invite")
public class SendInviteServlet extends HttpServlet {
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

        // Check user authentication
        if (session.getAttribute("UserID") == null) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("panelSendVisible", false);
        } else {
            request.setAttribute("panelSendVisible", true);
        }

        // Forward to JSP (equivalent to rendering ASPX page)
        request.getRequestDispatcher("/WEB-INF/templates/send-invite.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check user authentication (redundant but mirrors C# flow)
        if (session.getAttribute("UserID") == null) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("panelSendVisible", false);
        } else {
            String emailText = request.getParameter("textemail");
            if (emailText != null && !emailText.isEmpty()) {
                Email email = new Email();
                email.setTo(emailText);
                email.setSubject("دعوتنامه از سایت همکلاسی");
                email.setText("یکی از دوستان شما دعوتنامه ای از طرف سایت همکلاسی برای شما ارسال کرده است. برای عضویت لطفا کلیک کنید");

                int result = email.send();

                if (result == 1) {
                    request.setAttribute("labelError", "فرمت ایمیل وارد شده نادرست است");
                } else if (result == 9) {
                    request.setAttribute("labelError", "اشکال در ارسال ایمیل. تنظیمات اینترنت و پورت و آدرس ایمیل را چک کنید.");
                }
                // Note: C# code has an empty block for result == 0 (success), so no action taken here
            }

            request.setAttribute("panelSendVisible", true);
        }

        request.getRequestDispatcher("/WEB-INF/templates/send-invite.jsp").forward(request, response);
    }
}
