package com.hamkelasi.ui.admin;

import com.hamkelasi.bll.City;
import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.School;
import com.hamkelasi.bll.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;


@WebServlet("/admin/delete-provinces")
public class DeleteProvinceServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();

        // Check authentication
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName()) && session.getAttribute("UserID") == null) {
                    session.setAttribute("UserID", cookie.getValue());
                }
            }
        }

        boolean isAuthenticated = false;
        String errorMessage = null;

        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                User loggedUser = new User(loggedID);

                if (loggedUser.getPermission() == 1) {
                    isAuthenticated = true;

                    String provinceID = request.getParameter("ID");
                    if (provinceID != null && !provinceID.trim().isEmpty()) {
                        try {
                            int provinceId = Integer.parseInt(provinceID);
                            Province province = new Province(provinceId);
                            int cityCount = City.getCount(provinceId);
                            int schoolCount = new School().getCount(provinceId);

                            request.setAttribute("provinceID", provinceId);
                            request.setAttribute("provinceName", province.getName());
                            request.setAttribute("cityCount", cityCount);
                            request.setAttribute("schoolCount", schoolCount);
                        } catch (NumberFormatException e) {
                            response.sendRedirect("/admin/delete-provinces");
                            return;
                        }
                    } else {
                        response.sendRedirect("/admin/delete-provinces");
                        return;
                    }
                }
            } catch (Exception e) {
                errorMessage = "خطا در پردازش اطلاعات";
            }
        }

        if (!isAuthenticated) {
            errorMessage = "شما مجوز دسترسی به این صفحه را ندارید";
        }

        request.setAttribute("isAuthenticated", isAuthenticated);
        request.setAttribute("errorMessage", errorMessage);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/delete-province.html");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String provinceID = request.getParameter("provinceID");
        String action = request.getParameter("action");

        if ("yes".equals(action) && provinceID != null && !provinceID.trim().isEmpty()) {
            try {
                int provinceId = Integer.parseInt(provinceID);
                Province province = new Province();
                int result = province.delete(provinceId);

                if (result == 0) {
                    response.sendRedirect("/admin/edit-provinces?msg=ok");
                } else {
                    response.sendRedirect("/admin/edit-provinces?msg=fail");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("/Admin/admin/edit-provinces?msg=fail");
            }
        } else {
            response.sendRedirect("/admin/admin/edit-provinces");
        }
    }
}