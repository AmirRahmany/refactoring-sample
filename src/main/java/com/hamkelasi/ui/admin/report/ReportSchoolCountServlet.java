package com.hamkelasi.ui.admin.report;

import com.hamkelasi.bll.City;
import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.School;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/reports/schools/by-count")
public class ReportSchoolCountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Handle cookies for user authentication
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    if (session.getAttribute("UserID") == null) {
                        session.setAttribute("UserID", userId);
                    }
                    break;
                }
            }
        }

        boolean isAuthenticated = false;
        if (session.getAttribute("UserID") != null) {
            int loggedId = Integer.parseInt(session.getAttribute("UserID").toString());
            User loggedUser = new User(loggedId);
            if (loggedUser.getPermission() == 1) {
                isAuthenticated = true;
            }
        }

        if (isAuthenticated) {
            // Populate provinces on initial load
            List<Province> provinces = List.of(Province.getList());
            request.setAttribute("provinces", provinces);

            // Populate cities for the first province (if any)
            List<City> cities = new ArrayList<>();
            if (!provinces.isEmpty()) {
                int provinceId = provinces.get(0).getId();
                cities = List.of(City.getList(provinceId));
                request.setAttribute("selectedProvinceId", provinceId);
            }
            request.setAttribute("cities", cities);

            request.setAttribute("showProvinceCityTable", false);
            request.setAttribute("showProvinceTable", false);
            request.setAttribute("isAuthenticated",true);
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-count.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("showReportPanel", false);
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-count.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        // Re-populate provinces for all POST requests
        List<Province> provinces = List.of(Province.getList());
        request.setAttribute("provinces", provinces);

        String selectedProvinceId = request.getParameter("selectProvinceId");
        String selectedCityId = request.getParameter("cityId");

        // Populate cities based on selected province
        List<City> cities = new ArrayList<>();
        if (selectedProvinceId != null && !selectedProvinceId.isEmpty()) {
            int provinceId = Integer.parseInt(selectedProvinceId);
            cities = List.of(City.getList(provinceId));
            request.setAttribute("selectedProvinceId", provinceId);
        }
        request.setAttribute("cities", cities);

        // Handle "Show" action to display report
        if ("show".equals(action)) {
            String checkCity = request.getParameter("checkCity");
            request.setAttribute("checkCity", checkCity);
            if ("on".equals(checkCity)) {
                // Province and City report
                int provinceId = Integer.parseInt(selectedProvinceId);
                int cityId = Integer.parseInt(selectedCityId);
                int count = new School().getCount(provinceId, cityId);

                String provinceName = provinces.stream()
                        .filter(p -> p.getId() == provinceId)
                        .findFirst()
                        .map(Province::getName)
                        .orElse("");
                String cityName = cities.stream()
                        .filter(c -> c.getId() == cityId)
                        .findFirst()
                        .map(City::getName)
                        .orElse("");

                request.setAttribute("provinceName", provinceName);
                request.setAttribute("cityName", cityName);
                request.setAttribute("schoolCount", count);
                request.setAttribute("provinceName", provinceName);
                request.setAttribute("showProvinceCityTable", true);
                request.setAttribute("showProvinceTable", false);
                request.setAttribute("selectedCityId", cityId);
                request.setAttribute("isAuthenticated",true);
            } else {
                // Province-only report
                int provinceId = Integer.parseInt(selectedProvinceId);
                int count = new School().getCount(provinceId);

                String provinceName = provinces.stream()
                        .filter(p -> p.getId() == provinceId)
                        .findFirst()
                        .map(Province::getName)
                        .orElse("");


                request.setAttribute("provinceName", provinceName);
                request.setAttribute("schoolCount", count);
                request.setAttribute("showProvinceCityTable", false);
                request.setAttribute("showProvinceTable", true);
                request.setAttribute("isAuthenticated",true);
            }
        } else {
            // For fillCities action
            request.setAttribute("showProvinceCityTable", false);
            request.setAttribute("showProvinceTable", false);
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-count.jsp").forward(request, response);
    }
}