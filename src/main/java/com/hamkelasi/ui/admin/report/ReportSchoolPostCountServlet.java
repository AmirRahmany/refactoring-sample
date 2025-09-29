package com.hamkelasi.ui.admin.report;

import com.hamkelasi.bll.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/admin/reports/schools/by-post-count")
public class ReportSchoolPostCountServlet extends HttpServlet {
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
            request.setAttribute("isAuthenticated",true);
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

            // Populate schools for the first city (if any)
            List<School> schools = new ArrayList<>();
            if (!cities.isEmpty()) {
                int cityId = cities.get(0).getId();
                schools = School.getList(cityId);
                request.setAttribute("selectedCityId", cityId);
            }
            request.setAttribute("schools", schools);

            request.setAttribute("showTable", false);
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("showReportPanel", false);
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        // Re-populate provinces for all POST requests
        List<Province> provinces = List.of(Province.getList());
        request.setAttribute("provinces", provinces);

        String selectedProvinceId = request.getParameter("provinceId");
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

        // Handle date parameters
        boolean validDate = true;
        Date startDate = new IranianCalendar().convertToDateTime(1900, 1, 1);
        Date endDate = new IranianCalendar().convertToDateTime(3000, 12, 30);

        String checkStartDate = request.getParameter("checkStartDate");
        if ("on".equals(checkStartDate)) {
            String startYear = request.getParameter("startYear");
            String startMonth = request.getParameter("startMonth");
            String startDay = request.getParameter("startDay");
            if (startYear != null && !startYear.isEmpty() &&
                    startMonth != null && !startMonth.isEmpty() &&
                    startDay != null && !startDay.isEmpty()) {
                try {
                    startDate = new IranianCalendar().convertToDateTime(
                            Integer.parseInt(startYear),
                            Integer.parseInt(startMonth),
                            Integer.parseInt(startDay)
                    );
                    request.setAttribute("startYear", startYear);
                    request.setAttribute("startMonth", startMonth);
                    request.setAttribute("startDay", startDay);
                } catch (IllegalArgumentException ex) {
                    validDate = false;
                }
            } else {
                validDate = false;
            }
        }

        String checkEndDate = request.getParameter("checkEndDate");
        if ("on".equals(checkEndDate)) {
            String endYear = request.getParameter("endYear");
            String endMonth = request.getParameter("endMonth");
            String endDay = request.getParameter("endDay");
            if (endYear != null && !endYear.isEmpty() &&
                    endMonth != null && !endMonth.isEmpty() &&
                    endDay != null && !endDay.isEmpty()) {
                try {
                    endDate = new IranianCalendar().convertToDateTime(
                            Integer.parseInt(endYear),
                            Integer.parseInt(endMonth),
                            Integer.parseInt(endDay)
                    );
                    request.setAttribute("endYear", endYear);
                    request.setAttribute("endMonth", endMonth);
                    request.setAttribute("endDay", endDay);
                } catch (IllegalArgumentException ex) {
                    validDate = false;
                }
            } else {
                validDate = false;
            }
        }

        // Handle "Show" action to display report
        if ("show".equals(action) && validDate) {
            if (selectedSchoolId != null && !selectedSchoolId.isEmpty()) {
                int schoolId = Integer.parseInt(selectedSchoolId);
                School school = new School(schoolId);
                SchoolType schoolType = new SchoolType(school.getType());
                int postCount = school.getPostCount(schoolId, startDate, endDate);

                // Create result object for the table
                class ReportResult {
                    private String cityName;
                    private String schoolType;
                    private String schoolName;
                    private int postCount;

                    public ReportResult(String cityName, String schoolType, String schoolName, int postCount) {
                        this.cityName = cityName;
                        this.schoolType = schoolType;
                        this.schoolName = schoolName;
                        this.postCount = postCount;
                    }

                    public String getCityName() { return cityName; }
                    public String getSchoolType() { return schoolType; }
                    public String getSchoolName() { return schoolName; }
                    public int getPostCount() { return postCount; }
                }

                String cityName = cities.stream()
                        .filter(city -> city.getId() == Integer.parseInt(selectedCityId))
                        .findFirst()
                        .map(City::getName)
                        .orElse("");
                ReportResult result = new ReportResult(cityName, schoolType.getTypeName(), school.getName(), postCount);
                List<ReportResult> results = new ArrayList<>();
                results.add(result);
                request.setAttribute("results", results);
                request.setAttribute("showTable", true);
                request.setAttribute("selectedSchoolId", schoolId);
                request.setAttribute("checkStartDate", checkStartDate);
                request.setAttribute("checkEndDate", checkEndDate);
            } else {
                request.setAttribute("errorMessage", "لطفا یک آموزشگاه را انتخاب کنید");
                request.setAttribute("showTable", false);
            }
        } else if ("show".equals(action)) {
            request.setAttribute("errorMessage", "لطفا تاریخ‌های معتبر را وارد کنید");
            request.setAttribute("showTable", false);
            request.setAttribute("checkStartDate", checkStartDate);
            request.setAttribute("checkEndDate", checkEndDate);
        } else {
            request.setAttribute("showTable", false);
        }

        request.setAttribute("isAuthenticated",true);
        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
    }
}