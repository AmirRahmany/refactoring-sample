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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@WebServlet("/admin/reports/schools/by-post-count")
public class ReportSchoolPostCountServlet extends HttpServlet {
    private static final String CSRF_TOKEN_PARAM = "_csrf";

    private Date convertToDate(String year, String month, String day) throws ParseException {
        // Convert Persian date to Gregorian (simplified for demo)
        String dateStr = String.format("%s/%s/%s", year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.parse(dateStr);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isAuthenticated = session.getAttribute("isAuthenticated") != null && (boolean) session.getAttribute("isAuthenticated");
        boolean isAdmin = session.getAttribute("isAdmin") != null && (boolean) session.getAttribute("isAdmin");

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
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

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isAuthenticated = session.getAttribute("isAuthenticated") != null && (boolean) session.getAttribute("isAuthenticated");
        boolean isAdmin = session.getAttribute("isAdmin") != null && (boolean) session.getAttribute("isAdmin");

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
            return;
        }

        // Get request parameters
        String provinceIdStr = request.getParameter("listProvinces");
        String cityIdStr = request.getParameter("listCities");
        String schoolIdStr = request.getParameter("listSchools");
        String checkStartDate = request.getParameter("checkStartDate");
        String textStartDay = request.getParameter("textStartDay");
        String textStartMonth = request.getParameter("textStartMonth");
        String textStartYear = request.getParameter("textStartYear");
        String checkEndDate = request.getParameter("checkEndDate");
        String textEndDay = request.getParameter("textEndDay");
        String textEndMonth = request.getParameter("textEndMonth");
        String textEndYear = request.getParameter("textEndYear");
        String buttonFillCities = request.getParameter("buttonFillCities");
        String buttonFillSchools = request.getParameter("buttonFillSchools");
        String buttonShow = request.getParameter("buttonShow");
        String csrfToken = request.getParameter(CSRF_TOKEN_PARAM);

        // CSRF validation
        String sessionCsrfToken = (String) session.getAttribute(CSRF_TOKEN_PARAM);
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("labelError", "خطای امنیتی: توکن CSRF نامعتبر است");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
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
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
            return;
        }

        if (buttonFillSchools != null) {
            // Handle "نمایش مدارس" button
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
            return;
        }

        if (buttonShow != null) {
            // Handle "نمایش" button
            if (schoolIdStr == null || schoolIdStr.trim().isEmpty()) {
                request.setAttribute("labelError", "لطفاً یک آموزشگاه را انتخاب کنید");
                request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
                return;
            }

            int schoolId = Integer.parseInt(schoolIdStr);
            request.setAttribute("selectedSchoolId", schoolId);

            // Validate dates
            boolean validDate = true;
            Date startDate = null;
            Date endDate = null;

            if ("on".equals(checkStartDate)) {
                request.setAttribute("checkStartDate", true);
                request.setAttribute("textStartDay", textStartDay);
                request.setAttribute("textStartMonth", textStartMonth);
                request.setAttribute("textStartYear", textStartYear);
                if (textStartDay.isEmpty() || textStartMonth.isEmpty() || textStartYear.isEmpty()) {
                    request.setAttribute("startDateError", "تمام فیلدهای تاریخ شروع الزامی است");
                    validDate = false;
                } else {
                    try {
                        int day = Integer.parseInt(textStartDay);
                        int month = Integer.parseInt(textStartMonth);
                        int year = Integer.parseInt(textStartYear);
                        if (day < 1 || day > 30 || month < 1 || month > 12 || year < 1300 || year > 1500) {
                            request.setAttribute("startDateError", "مقادیر تاریخ شروع نامعتبر است");
                            validDate = false;
                        } else {
                            startDate = convertToDate(textStartYear, textStartMonth, textStartDay);
                        }
                    } catch (NumberFormatException | ParseException e) {
                        request.setAttribute("startDateError", "فرمت تاریخ شروع نامعتبر است");
                        validDate = false;
                    }
                }
            } else {
                try {
                    startDate = convertToDate("1300", "01", "01"); // Default Persian date
                } catch (ParseException e) {
                    validDate = false;
                }
            }

            if ("on".equals(checkEndDate)) {
                request.setAttribute("checkEndDate", true);
                request.setAttribute("textEndDay", textEndDay);
                request.setAttribute("textEndMonth", textEndMonth);
                request.setAttribute("textEndYear", textEndYear);
                if (textEndDay.isEmpty() || textEndMonth.isEmpty() || textEndYear.isEmpty()) {
                    request.setAttribute("endDateError", "تمام فیلدهای تاریخ پایان الزامی است");
                    validDate = false;
                } else {
                    try {
                        int day = Integer.parseInt(textEndDay);
                        int month = Integer.parseInt(textEndMonth);
                        int year = Integer.parseInt(textEndYear);
                        if (day < 1 || day > 30 || month < 1 || month > 12 || year < 1300 || year > 1500) {
                            request.setAttribute("endDateError", "مقادیر تاریخ پایان نامعتبر است");
                            validDate = false;
                        } else {
                            endDate = convertToDate(textEndYear, textEndMonth, textEndDay);
                        }
                    } catch (NumberFormatException | ParseException e) {
                        request.setAttribute("endDateError", "فرمت تاریخ پایان نامعتبر است");
                        validDate = false;
                    }
                }
            } else {
                try {
                    endDate = convertToDate("1500", "12", "30"); // Default Persian date
                } catch (ParseException e) {
                    validDate = false;
                }
            }

            if (validDate) {
                // Generate report
                School school = schools.stream().filter(s -> s.getId() == schoolId).findFirst().orElse(null);
                if (school != null) {
                    SchoolType schoolType = new SchoolType();
                    City city = cities.stream().filter(c -> c.getId() == cityId).findFirst().orElse(null);
                    int postCount = new School(schoolId).getPostCount(schoolId,startDate,endDate);

                    request.setAttribute("showResultTable", true);
                    request.setAttribute("cityName", city != null ? city.getName() : "");
                    request.setAttribute("schoolType", schoolType.getTypeName());
                    request.setAttribute("schoolName", school.getName());
                    request.setAttribute("postCount", postCount);
                }
            }
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-school-post-count.jsp").forward(request, response);
    }
}
