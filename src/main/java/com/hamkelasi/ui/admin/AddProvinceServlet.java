package com.hamkelasi.ui.admin;

import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


import java.io.IOException;

@WebServlet("/admin/add-province")
public class AddProvinceServlet extends HttpServlet {

    @Override
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
                // Assuming you have a User class similar to BLL.User
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

        // Forward to JSP page
        request.getRequestDispatcher("/WEB-INF/templates/admin/add-province.jsp").forward(request, response);
    }

    @Override
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
                    String provinceName = request.getParameter("textProvinceName");

                    if (provinceName != null && !provinceName.trim().isEmpty()) {
                        Province province = new Province();
                        int result = province.add(provinceName);

                        switch (result) {
                            case 0:
                                errorMessage = "ثبت اطلاعات با موفقیت انجام گرفت";
                                break;
                            case 1:
                                errorMessage = "نام استان وارد شده تکراری است";
                                break;
                            case 9:
                                errorMessage = "اشکال در ثبت اطلاعات";
                                break;
                            default:
                                errorMessage = "نتیجه نامشخص: " + result;
                                break;
                        }
                    } else {
                        errorMessage = "لطفا نام استان را وارد کنید";
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

        // Forward to JSP page
        request.getRequestDispatcher("/WEB-INF/templates/admin/add-province.jsp").forward(request, response);
    }
}