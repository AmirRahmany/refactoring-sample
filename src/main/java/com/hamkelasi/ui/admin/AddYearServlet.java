package com.hamkelasi.ui.admin;


import com.hamkelasi.bll.User;
import com.hamkelasi.bll.Year;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/add-year")
public class AddYearServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();

        // Check for UserID cookie
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName()) && session.getAttribute("UserID") == null) {
                    session.setAttribute("UserID", cookie.getValue());
                    break;
                }
            }
        }

        boolean isAuthenticated = false;

        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                User loggedUser = new User(loggedID);

                if (loggedUser.getPermission() == 1) {
                    isAuthenticated = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (!isAuthenticated) {
            request.setAttribute("panelVisible", false);
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
        } else {
            request.setAttribute("panelVisible", true);
        }

        // Forward to Thymeleaf template
        request.getRequestDispatcher("/WEB-INF/templates/admin/add-academic-year.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        boolean isAuthenticated = false;
        String errorMessage = null;

        // Check authentication
        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                User loggedUser = new User(loggedID);

                if (loggedUser.getPermission() == 1) {
                    isAuthenticated = true;

                    // Process form submission
                    String yearName = request.getParameter("textYearName");

                    if (yearName != null && !yearName.trim().isEmpty()) {
                        Year year = new Year();
                        int result = year.add(yearName);

                        switch (result) {
                            case 0:
                                errorMessage = "سال تحصیلی با موفقیت ثبت گردید";
                                break;
                            case 1:
                                errorMessage = "سال تحصیلی وارد شده تکراری است";
                                break;
                            case 9:
                                errorMessage = "اشکال در ثبت داده";
                                break;
                            default:
                                errorMessage = "نتیجه نامشخص: " + result;
                                break;
                        }
                    } else {
                        errorMessage = "لطفا سال تحصیلی را وارد کنید";
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                errorMessage = "خطا در پردازش درخواست";
            }
        }

        if (!isAuthenticated) {
            errorMessage = "شما مجوز دسترسی به این صفحه را ندارید";
        }

        request.setAttribute("panelVisible", isAuthenticated);
        request.setAttribute("errorMessage", errorMessage);

        // Forward to Thymeleaf template
        request.getRequestDispatcher("/WEB-INF/templates/admin/add-academic-year.jsp").forward(request, response);
    }
}