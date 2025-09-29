package com.hamkelasi.ui.admin.report;
import com.hamkelasi.bll.IranianCalendar;
import com.hamkelasi.bll.Permission;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/admin/reports/users/by-type")
public class ReportUserTypeServlet extends HttpServlet {
    private static final String CSRF_TOKEN_PARAM = "_csrf";

    private static class ReportResult {
        int userId;
        String username;
        String fullName;
        String email;

        ReportResult(int userId, String username, String fullName, String email) {
            this.userId = userId;
            this.username = username;
            this.fullName = fullName;
            this.email = email;
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
                request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
                return;
            }
        }

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("isAuthenticated",false);
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
            return;
        }

        // Initialize permissions
        List<Permission> permissions = Permission.getList();
        if (permissions == null || permissions.isEmpty()) {
            request.setAttribute("labelError", "هیچ نوع کاربری یافت نشد");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
            return;
        }
        request.setAttribute("permissions", permissions);
        request.setAttribute("isAuthenticated", true);

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
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
                request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
                return;
            }
        }

        if (!isAuthenticated || !isAdmin) {
            request.setAttribute("panelVisible",false);
            request.setAttribute("labelError", "شما مجوز دسترسی به این صفحه را ندارید");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
            return;
        }

        // Get request parameters
        String typeIdStr = request.getParameter("listTypes");
        String buttonShow = request.getParameter("buttonShow");
        String csrfToken = request.getParameter(CSRF_TOKEN_PARAM);

        // CSRF validation
        String sessionCsrfToken = (String) session.getAttribute(CSRF_TOKEN_PARAM);
        if (csrfToken == null || !csrfToken.equals(sessionCsrfToken)) {
            request.setAttribute("labelError", "خطای امنیتی: توکن CSRF نامعتبر است");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
            return;
        }

        // Initialize permissions
        List<Permission> permissions = Permission.getList();
        if (permissions == null || permissions.isEmpty()) {
            request.setAttribute("labelError", "هیچ نوع کاربری یافت نشد");
            request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
            return;
        }
        request.setAttribute("permissions", permissions);
        request.setAttribute("isAuthenticated", true);

        if (buttonShow != null) {
            if (typeIdStr == null || typeIdStr.trim().isEmpty()) {
                request.setAttribute("labelError", "لطفاً یک نوع کاربر را انتخاب کنید");
                request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
                return;
            }

            try {
                int typeId = Integer.parseInt(typeIdStr);
                request.setAttribute("selectedTypeId", typeId);
                List<User> userList = new User().getListByType(typeId);
                List<ReportResult> results = new ArrayList<>();

                if (userList != null && !userList.isEmpty()) {
                    for (User user : userList) {
                        String fullName = (user.getLastname() != null ? user.getFirstname() : "") + " " + (user.getLastname() != null ? user.getLastname() : "");
                        String email = user.getEmail() != null ? user.getEmail() : "";
                        results.add(new ReportResult(user.getId(), user.getUsername(), fullName.trim(), email));
                    }
                    request.setAttribute("showResultTable", true);
                    request.setAttribute("results", results);
                } else {
                    request.setAttribute("labelError", "هیچ کاربری در گروه انتخاب شده وجود ندارد");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("labelError", "شناسه نوع کاربر نامعتبر است");
            }
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/report/report-user-type.jsp").forward(request, response);
    }
}
