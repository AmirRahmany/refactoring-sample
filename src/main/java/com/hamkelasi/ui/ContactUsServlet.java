package com.hamkelasi.ui;


import com.hamkelasi.bll.Email;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/contactUs")
public class ContactUsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Page_Load is empty in C#, so just forward to JSP
        request.getRequestDispatcher("/WEB-INF/templates/contactus.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Mimic Page.IsValid: Check for required fields
        if (isValidForm(request)) {
            Email email = new Email();
            email.setTo(email.getAdminEmail()); // Assumes admin email is defined in Email class
            email.setSubject(request.getParameter("textSubject"));
            email.setText(request.getParameter("textMessage"));

            int result = email.send();

            if (result == 1) {
                request.setAttribute("labelError", "فرمت ایمیل وارد شده نادرست است");
            } else if (result == 9) {
                request.setAttribute("labelError", "اشکال در ارسال ایمیل. تنظیمات اینترنت و پورت و آدرس ایمیل را چک کنید.");
            }
            // Note: C# code has an empty block for result == 0 (success), so no action taken here
        }

        request.getRequestDispatcher("/WEB-INF/templates/contactus.jsp").forward(request, response);
    }

    private boolean isValidForm(HttpServletRequest request) {
        // Mimic Page.IsValid: Check for required fields
        return request.getParameter("textSubject") != null &&
                request.getParameter("textMessage") != null &&
                !request.getParameter("textSubject").isEmpty() &&
                !request.getParameter("textMessage").isEmpty();
    }
}
