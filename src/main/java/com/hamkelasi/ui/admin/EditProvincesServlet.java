package com.hamkelasi.ui.admin;

import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;


@WebServlet("/admin/edit-provinces")
public class EditProvincesServlet extends HttpServlet {
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
            String provinceId = request.getParameter("ID");
            String message = request.getParameter("msg");

            if (provinceId == null) {
                // Handle messages from delete operation
                if (message != null) {
                    if ("ok".equals(message)) {
                        request.setAttribute("errorMessage", "استان مورد نظر با موفقیت حذف گردید");
                    } else if ("fail".equals(message)) {
                        request.setAttribute("errorMessage", "اشکال در حذف استان");
                    }
                }

                // List all provinces
                List<Province> provinceList = List.of(Province.getList());
                request.setAttribute("provinceList", provinceList);
                request.setAttribute("showEditPanel", false);
            } else {
                // Fill edit panel
                int id = Integer.parseInt(provinceId);
                Province province = new Province(id);
                request.setAttribute("provinceId", province.getId());
                request.setAttribute("provinceName", province.getName());
                request.setAttribute("showTable", false);
                session.setAttribute("oldProvinceName", province.getName());
            }
            request.getRequestDispatcher("/WEB-INF/templates/admin/edit-provinces.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
            request.setAttribute("showTable", false);
            request.setAttribute("showEditPanel", false);
            request.getRequestDispatcher("/WEB-INF/templates/admin/edit-provinces.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String oldProvinceName = session.getAttribute("oldProvinceName").toString();
        String provinceName = request.getParameter("provinceName");
        String provinceId = request.getParameter("provinceId");

        if (provinceName != null && !provinceName.equals(oldProvinceName)) {
            Province province = new Province();
            int id = Integer.parseInt(provinceId);
            int result = province.update(id, provinceName);

            switch (result) {
                case 0:
                    request.setAttribute("errorMessage", "تغییرات با موفقیت ذخیره گردید");
                    break;
                case 1:
                    request.setAttribute("errorMessage", "نام استان وارد شده تکراری است");
                    break;
                case 9:
                    request.setAttribute("errorMessage", "اشکال در ثبت داده");
                    break;
            }
        }

        // Reload edit panel with updated data
        request.setAttribute("provinceId", provinceId);
        request.setAttribute("provinceName", provinceName);
        request.setAttribute("showTable", false);
        request.getRequestDispatcher("/WEB-INF/templates/admin/edit-provinces.jsp").forward(request, response);
    }
}