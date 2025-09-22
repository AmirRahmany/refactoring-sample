package com.hamkelasi.ui.admin.report;

import com.hamkelasi.bll.City;
import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.School;
import com.hamkelasi.bll.SchoolType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/admin/reports/schools/by-user-count")
public class ReportSchoolUserCountServlet extends HttpServlet {
    private static final String CSRF_TOKEN_PARAM = "_csrf";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isAuthenticated = session.getAttribute("isAuthenticated") != null && (boolean) session.getAttribute("isAuthenticated");
        boolean isAdmin = session.getAttribute("isAdmin") != null && (boolean) session.getAttribute("isAdmin");

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
            return;
        }

        // Initialize provinces
        List<Province> provinces = Arrays.asList(Province.getList());
        request.setAttribute("provinces", provinces);

        // Initialize cities for the first province
        int provinceId = provinces.get(0).getId();
        List<City> cities = List.of(City.getList(provinceId));
        request.setAttribute("cities", cities);
        request.setAttribute("selectedProvinceId", provinceId);

        // Initialize schools for the first city
        int cityId = cities.get(0).getId();
        List<School> schools = School.getList(cityId);
        request.setAttribute("schools", schools);
        request.setAttribute("selectedCityId", cityId);
        request.setAttribute("isAuthenticated", true);

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isAuthenticated = session.getAttribute("isAuthenticated") != null && (boolean) session.getAttribute("isAuthenticated");
        boolean isAdmin = session.getAttribute("isAdmin") != null && (boolean) session.getAttribute("isAdmin");

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
            return;
        }

        // Get request parameters
        String provinceIdStr = request.getParameter("listProvinces");
        String cityIdStr = request.getParameter("listCities");
        String schoolIdStr = request.getParameter("listSchools");
        String buttonFillCities = request.getParameter("buttonFillCities");
        String buttonFillSchools = request.getParameter("buttonFillSchools");
        String buttonShow = request.getParameter("buttonShow");
        String csrfToken = request.getParameter(CSRF_TOKEN_PARAM);

        // CSRF validation
        String sessionCsrfToken = (String) session.getAttribute(CSRF_TOKEN_PARAM);
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("labelError", "خطای امنیتی: توکن CSRF نامعتبر است");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
            return;
        }

        // Initialize provinces
        List<Province> provinces = Arrays.asList(Province.getList());
        request.setAttribute("provinces", provinces);
        int provinceId = provinceIdStr != null ? Integer.parseInt(provinceIdStr) : provinces.get(0).getId();
        request.setAttribute("selectedProvinceId", provinceId);

        // Initialize cities
        List<City> cities = List.of(City.getList(provinceId));
        request.setAttribute("cities", cities);
        int cityId = cityIdStr != null ? Integer.parseInt(cityIdStr) : cities.get(0).getId();
        request.setAttribute("selectedCityId", cityId);

        // Initialize schools
        List<School> schools = School.getList(cityId);
        request.setAttribute("schools", schools);
        request.setAttribute("isAuthenticated", true);

        if (buttonFillCities != null) {
            // Handle "نمایش شهرها" button
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
            return;
        }

        if (buttonFillSchools != null) {
            // Handle "نمایش مدارس" button
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
            return;
        }

        if (buttonShow != null) {
            // Handle "نمایش" button
            if (schoolIdStr == null || schoolIdStr.trim().isEmpty()) {
                request.setAttribute("labelError", "لطفاً یک آموزشگاه را انتخاب کنید");
                request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
                return;
            }

            int schoolId = Integer.parseInt(schoolIdStr);
            request.setAttribute("selectedSchoolId", schoolId);

            // Generate report
            School school = new School(schoolId);
            SchoolType schoolType = new SchoolType(school.getType());
            int userCount = school.getUserCount();

            City city = null;
            for (City c : cities) {
                if (c.getId() == cityId) {
                    city = c;
                    break;
                }
            }

            request.setAttribute("showResultTable", true);
            request.setAttribute("cityName", city != null ? city.getName() : "");
            request.setAttribute("schoolType", schoolType.getTypeName());
            request.setAttribute("schoolName", school.getName());
            request.setAttribute("userCount", userCount);
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
    }
}
