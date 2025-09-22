package com.hamkelasi.ui.admin;

import com.hamkelasi.bll.City;
import com.hamkelasi.bll.School;
import com.hamkelasi.bll.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;


@WebServlet("/admin/delete-cities")
public class DeleteCityServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();

        // Check authentication from cookies
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

                    String cityId = request.getParameter("ID");
                    if (cityId != null && !cityId.trim().isEmpty()) {
                        try {
                            int cityID = Integer.parseInt(cityId);
                            City city = new City(cityID);
                            int schoolCount = new School().getCount(city.getProvinceId());

                            request.setAttribute("cityId", cityID);
                            request.setAttribute("cityName", city.getName());
                            request.setAttribute("schoolCount", schoolCount);
                        } catch (NumberFormatException e) {
                            response.sendRedirect(request.getContextPath() + "/admin/edit-city");
                            return;
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/admin/edit-city");
                        return;
                    }
                }
            } catch (Exception e) {
                errorMessage = "Error processing request";
            }
        }

        if (!isAuthenticated) {
            errorMessage = "شما مجوز دسترسی به این صفحه را ندارید";
        }

        request.setAttribute("isAuthenticated", isAuthenticated);
        request.setAttribute("errorMessage", errorMessage);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/delete-city.html");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cityId = request.getParameter("cityId");
        String action = request.getParameter("action");

        if ("yes".equals(action) && cityId != null && !cityId.trim().isEmpty()) {
            try {
                int cityID = Integer.parseInt(cityId);
                City city = new City();
                int result = city.delete(cityID);

                if (result == 0) {
                    response.sendRedirect(request.getContextPath() + "/admin/edit-cities?msg=ok");
                } else if (result == 9) {
                    response.sendRedirect(request.getContextPath() + "/admin/edit-cities?msg=fail");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/edit-cities?msg=fail");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/edit-cities");
        }
    }
}