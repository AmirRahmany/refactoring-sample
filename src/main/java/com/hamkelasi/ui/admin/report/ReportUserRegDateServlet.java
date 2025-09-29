package com.hamkelasi.ui.admin.report;

import com.hamkelasi.bll.IranianCalendar;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@WebServlet("/admin/reports/users/by-registration-date")
public class ReportUserRegDateServlet extends HttpServlet {
    private static final String CSRF_TOKEN_PARAM = "_csrf";

    private static class ReportResult {
        int userId;
        String username;
        String fullName;
        String regDate;

        ReportResult(int userId, String username, String fullName, String regDate) {
            this.userId = userId;
            this.username = username;
            this.fullName = fullName;
            this.regDate = regDate;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isAuthenticated = false;
        boolean isAdmin = false;

        Object userIdObj = session.getAttribute("UserID");
        if (userIdObj != null) {
            try {
                int userId = userIdObj instanceof String ? Integer.parseInt((String) userIdObj) : (Integer) userIdObj;
                User loggedUser = new User(userId);
                isAdmin = loggedUser.getPermission() == 1;
                isAuthenticated = isAdmin;
            } catch (NumberFormatException e) {
                request.setAttribute("labelError", "شناسه کاربر نامعتبر است");
                request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-reg-date.jsp").forward(request, response);
                return;
            }
        }

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("isAuthenticated", false);
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-reg-date.jsp").forward(request, response);
            return;
        }

        request.setAttribute("isAuthenticated", true);
        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-reg-date.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isAuthenticated = false;
        boolean isAdmin = false;

        Object userIdObj = session.getAttribute("UserID");
        if (userIdObj != null) {
            try {
                int userId = userIdObj instanceof String ? Integer.parseInt((String) userIdObj) : (Integer) userIdObj;
                User loggedUser = new User(userId);
                isAdmin = loggedUser.getPermission() == 1;
                isAuthenticated = isAdmin;
            } catch (NumberFormatException e) {
                request.setAttribute("labelError", "شناسه کاربر نامعتبر است");
                request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-reg-date.jsp").forward(request, response);
                return;
            }
        }

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("isAuthenticated", false);
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-reg-date.jsp").forward(request, response);
            return;
        }

        // Get request parameters
        String checkStartDate = request.getParameter("checkStartDate");
        String textStartDay = request.getParameter("textStartDay");
        String textStartMonth = request.getParameter("textStartMonth");
        String textStartYear = request.getParameter("textStartYear");
        String checkEndDate = request.getParameter("checkEndDate");
        String textEndDay = request.getParameter("textEndDay");
        String textEndMonth = request.getParameter("textEndMonth");
        String textEndYear = request.getParameter("textEndYear");
        String buttonShow = request.getParameter("buttonShow");
        String csrfToken = request.getParameter(CSRF_TOKEN_PARAM);

        // CSRF validation
        String sessionCsrfToken = (String) session.getAttribute(CSRF_TOKEN_PARAM);
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("labelError", "خطای امنیتی: توکن CSRF نامعتبر است");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-reg-date.jsp").forward(request, response);
            return;
        }

        request.setAttribute("isAuthenticated", true);

        if (buttonShow != null) {
            // Validate at least one date is checked
            if (!"on".equals(checkStartDate) && !"on".equals(checkEndDate)) {
                request.setAttribute("labelError", "لطفاً حداقل یکی از تاریخ‌های شروع یا پایان را انتخاب کنید");
                request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-reg-date.jsp").forward(request, response);
                return;
            }

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
                            startDate = new IranianCalendar().convertToDateTime(year, month, day);
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("startDateError", "فرمت تاریخ شروع نامعتبر است");
                        validDate = false;
                    }
                }
            } else {
                try {
                    startDate = new IranianCalendar().convertToDateTime(1300, 1, 1);
                } catch (Exception e) {
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
                            endDate = new IranianCalendar().convertToDateTime(year, month, day);
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("endDateError", "فرمت تاریخ پایان نامعتبر است");
                        validDate = false;
                    }
                }
            } else {
                try {
                    endDate = new IranianCalendar().convertToDateTime(1500, 12, 30);
                } catch (Exception e) {
                    validDate = false;
                }
            }

            if (validDate) {
                List<ReportResult> results = new ArrayList<>();
                List<User> registeredUsers = new User().getRegisteredList(startDate, endDate);
                if (registeredUsers != null) {
                    for (User user : registeredUsers) {
                        String fullName = (user.getFirstname() != null ? user.getFirstname() : "") + " " + (user.getLastname() != null ? user.getLastname() : "");
                        String regDate = new IranianCalendar(user.getRegisterDate()).toString();
                        results.add(new ReportResult(user.getId(), user.getUsername(), fullName.trim(), regDate));
                    }
                }

                if (!results.isEmpty()) {
                    request.setAttribute("showResultTable", true);
                    request.setAttribute("results", results);
                } else {
                    request.setAttribute("labelError", "هیچ کاربری برای بازه زمانی انتخابی یافت نشد");
                }
            } else {
                request.setAttribute("labelError", "تاریخ وارد شده معتبر نمی باشد");
            }
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-reg-date.jsp").forward(request, response);
    }
}