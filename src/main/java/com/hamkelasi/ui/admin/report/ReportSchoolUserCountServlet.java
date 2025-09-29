package com.hamkelasi.ui.admin.report;

import com.hamkelasi.bll.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/admin/reports/schools/by-user-count")
public class ReportSchoolUserCountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
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
            int loggedId = Integer.parseInt( session.getAttribute("UserID").toString());
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
            }
            request.setAttribute("cities", cities);

            // Populate schools for the first city (if any)
            List<School> schools = new ArrayList<>();
            if (!cities.isEmpty()) {
                int cityId = cities.get(0).getId();
                schools = School.getList(cityId);
            }
            request.setAttribute("schools", schools);

            request.setAttribute("showResultTable", true);
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
        } else {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("showReportPanel", false);
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        // Re-populate provinces for all POST requests
        List<Province> provinces = List.of(Province.getList());
        request.setAttribute("provinces", provinces);

        String selectedProvinceId = request.getParameter("selectedProvinceId");
        String selectedCityId = request.getParameter("cityId");
        String selectedSchoolId = request.getParameter("schoolId");

        // Populate cities based on selected province
        List<City> cities = new ArrayList<>();
        if (selectedProvinceId != null && !selectedProvinceId.isEmpty()) {
            int provinceId = Integer.parseInt(selectedProvinceId);
            cities = List.of(City.getList(provinceId));
            request.setAttribute("selectedProvinceId", provinceId);
        }
        request.setAttribute("cities", cities);

        // Populate schools based on selected city
        List<School> schools = new ArrayList<>();
        if ("fillSchools".equals(action) || "show".equals(action)) {
            if (selectedCityId != null && !selectedCityId.isEmpty()) {
                int cityId = Integer.parseInt(selectedCityId);
                schools = School.getList(cityId);
                request.setAttribute("selectedCityId", cityId);
            }
        }
        request.setAttribute("schools", schools);

        // Handle "Show" action to display report
        if ("show".equals(action)) {
            if (selectedSchoolId != null && !selectedSchoolId.isEmpty()) {
                int schoolId = Integer.parseInt(selectedSchoolId);
                School school = new School(schoolId);
                SchoolType schoolType = new SchoolType(school.getType());
                int userCount = school.getUserCount();

                // Create result object for the table
                class ReportResult {
                    private String cityName;
                    private String schoolType;
                    private String schoolName;
                    private int userCount;

                    public ReportResult(String cityName, String schoolType, String schoolName, int userCount) {
                        this.cityName = cityName;
                        this.schoolType = schoolType;
                        this.schoolName = schoolName;
                        this.userCount = userCount;
                    }

                    // Getters
                    public String getCityName() { return cityName; }
                    public String getSchoolType() { return schoolType; }
                    public String getSchoolName() { return schoolName; }
                    public int getUserCount() { return userCount; }
                }

                String cityName = cities.stream()
                        .filter(city -> city.getId() == Integer.parseInt(selectedCityId))
                        .findFirst()
                        .map(City::getName)
                        .orElse("");
                ReportResult result = new ReportResult(cityName, schoolType.getTypeName(), school.getName(), userCount);
                List<ReportResult> results = new ArrayList<>();
                results.add(result);
                request.setAttribute("results", results);
                request.setAttribute("showTable", true);
                request.setAttribute("selectedSchoolId", schoolId);
            } else {
                request.setAttribute("labelError", "لطفا یک آموزشگاه را انتخاب کنید");
                request.setAttribute("showTable", false);
            }
        } else {
            request.setAttribute("showTable", false);
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-user-count.jsp").forward(request, response);
    }
}