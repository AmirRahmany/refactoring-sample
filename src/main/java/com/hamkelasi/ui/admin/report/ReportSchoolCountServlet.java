package com.hamkelasi.ui.admin.report;

import com.hamkelasi.bll.City;
import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.School;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/admin/reports/schools/by-count")
public class ReportSchoolCountServlet extends HttpServlet {
    private static final String CSRF_TOKEN_PARAM = "_csrf";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isAuthenticated = session.getAttribute("isAuthenticated") != null && (boolean) session.getAttribute("isAuthenticated");
        boolean isAdmin = session.getAttribute("isAdmin") != null && (boolean) session.getAttribute("isAdmin");

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-count.jsp").forward(request, response);
            return;
        }

        // Initialize provinces
        List<Province> provinces = List.of(Province.getList());
        request.setAttribute("provinces", provinces);

        // Initialize cities for the first province
        int provinceId = provinces.get(0).getId();
        List<City> cities = List.of(City.getList(provinceId));
        request.setAttribute("cities", cities);
        request.setAttribute("selectedProvinceId", provinceId);
        request.setAttribute("isAuthenticated", true);

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-count.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isAuthenticated = session.getAttribute("isAuthenticated") != null && (boolean) session.getAttribute("isAuthenticated");
        boolean isAdmin = session.getAttribute("isAdmin") != null && (boolean) session.getAttribute("isAdmin");

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-count.jsp").forward(request, response);
            return;
        }

        // Get request parameters
        String provinceIdStr = request.getParameter("listProvinces");
        String cityIdStr = request.getParameter("listCities");
        String checkCity = request.getParameter("checkCity");
        String buttonFillCities = request.getParameter("buttonFillCities");
        String buttonShow = request.getParameter("buttonShow");
        String csrfToken = request.getParameter(CSRF_TOKEN_PARAM);

        // CSRF validation
        String sessionCsrfToken = (String) session.getAttribute(CSRF_TOKEN_PARAM);
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("labelError", "خطای امنیتی: توکن CSRF نامعتبر است");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-count.jsp").forward(request, response);
            return;
        }

        // Initialize provinces
        List<Province> provinces = Arrays.asList(Province.getList());
        request.setAttribute("provinces", provinces);
        int provinceId = provinceIdStr != null ? Integer.parseInt(provinceIdStr) : provinces.get(0).getId();

        // Handle city dropdown population
        List<City> cities = List.of(City.getList(provinceId));
        request.setAttribute("cities", cities);
        request.setAttribute("selectedProvinceId", provinceId);
        request.setAttribute("isAuthenticated", true);

        if (buttonFillCities != null || request.getParameter("listProvinces") != null) {
            // Handle "نمایش شهرها" button or province dropdown change
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-count.jsp").forward(request, response);
            return;
        }

        if (buttonShow != null) {
            // Handle "نمایش" button
            boolean isCityChecked = "on".equals(checkCity);
            request.setAttribute("checkCity", isCityChecked);

            if (isCityChecked) {
                // Show province and city table
                int cityId = Integer.parseInt(cityIdStr);
                int schoolCount = School.getCount();
                String provinceName = provinces.stream().filter(p -> p.getId() == provinceId).findFirst().map(p -> p.getName()).orElse("");
                String cityName = cities.stream().filter(c -> c.getId() == cityId).findFirst().map(c -> c.getName()).orElse("");

                request.setAttribute("showProvinceCityTable", true);
                request.setAttribute("provinceName", provinceName);
                request.setAttribute("cityName", cityName);
                request.setAttribute("schoolCount", schoolCount);
                request.setAttribute("selectedCityId", cityId);
            } else {
                // Show province-only table
                int schoolCount = School.getCount();
                String provinceName = provinces.stream().filter(p -> p.getId() == provinceId).findFirst().map(p -> p.getName()).orElse("");

                request.setAttribute("showProvinceTable", true);
                request.setAttribute("provinceName", provinceName);
                request.setAttribute("schoolCount", schoolCount);
            }
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-count.jsp").forward(request, response);
    }
}
