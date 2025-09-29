package com.hamkelasi.ui.admin;

import com.hamkelasi.bll.User;
import com.hamkelasi.bll.Year;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;


@WebServlet("/admin/edit-academic-years")
public class EditAcademicYearServlet extends HttpServlet {
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
            int loggedId = Integer.parseInt(session.getAttribute("UserID").toString());
            User loggedUser = new User(loggedId);
            if (loggedUser.getPermission() == 1) {
                isAuthenticated = true;
            }
        }

        if (isAuthenticated) {
            String yearId = request.getParameter("ID");
            String message = request.getParameter("msg");

            if (yearId == null) {
                // Handle messages from delete operation
                if (message != null) {
                    if ("ok".equals(message)) {
                        request.setAttribute("errorMessage", "سال تحصیلی مورد نظر با موفقیت حذف گردید");
                    } else if ("fail".equals(message)) {
                        request.setAttribute("errorMessage", "اشکال در حذف سال تحصیلی");
                    }
                }

                // List all academic years
                List<Year> yearList = List.of(Year.getList());
                request.setAttribute("yearList", yearList);
                request.setAttribute("showEditPanel", false);
            } else {
                // Fill edit panel
                int id = Integer.parseInt(yearId);
                Year year = new Year(id);
                request.setAttribute("yearId", year.getId());
                request.setAttribute("yearName", year.getAcademicYear());
                request.setAttribute("showTable", false);
                session.setAttribute("oldYearName", year.getAcademicYear());
            }
            request.getRequestDispatcher("/WEB-INF/templates/admin/edit-academic-year.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("showTable", false);
            request.setAttribute("showEditPanel", false);
            request.getRequestDispatcher("/WEB-INF/templates/admin/edit-academic-year.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String oldYearName = session.getAttribute("oldYearName").toString();
        String yearName = request.getParameter("yearName");
        String yearId = request.getParameter("yearId");

        if (yearName != null && !yearName.equals(oldYearName)) {
            Year year = new Year();
            int id = Integer.parseInt(yearId);
            int result = year.update(id, yearName);

            switch (result) {
                case 0:
                    request.setAttribute("errorMessage", "اطلاعات با موفقیت تغییر یافت");
                    break;
                case 1:
                    request.setAttribute("errorMessage", "نام تحصیلی وارد شده تکراری است");
                    break;
                case 9:
                    request.setAttribute("errorMessage", "اشکال در ثبت تغییرات");
                    break;
            }
        }

        // Reload edit panel with updated data
        request.setAttribute("yearId", yearId);
        request.setAttribute("yearName", yearName);
        request.setAttribute("showTable", false);
        request.getRequestDispatcher("/WEB-INF/templates/admin/edit-academic-year.jsp").forward(request, response);
    }
}
