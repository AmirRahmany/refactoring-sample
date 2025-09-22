package com.hamkelasi.ui.admin;

import com.hamkelasi.bll.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/add-school")
public class AddSchoolServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();

        // Check for UserID cookie
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName()) && session.getAttribute("UserID") == null) {
                    session.setAttribute("UserID", cookie.getValue());
                    break;
                }
            }
        }

        boolean isAuthenticated = false;

        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                User loggedUser = new User(loggedID);

                if (loggedUser.getPermission() == 1) {
                    isAuthenticated = true;

                    // Load initial data for dropdowns
                    List<SchoolType> schoolTypes = List.of(SchoolType.getList());
                    List<Province> provinces = List.of(new Province().getList());
                    List<City> cities = List.of(City.getList(provinces.get(0).getId()));

                    request.setAttribute("schoolTypes", schoolTypes);
                    request.setAttribute("provinces", provinces);
                    request.setAttribute("cities", cities);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (!isAuthenticated) {
            request.setAttribute("panelVisible", false);
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
        } else {
            request.setAttribute("panelVisible", true);
        }

        // Forward to Thymeleaf template
        request.getRequestDispatcher("/WEB-INF/templates/admin/add-school.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        boolean isAuthenticated = false;
        String errorMessage = null;

        // Check authentication
        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                User loggedUser = new User(loggedID);

                if (loggedUser.getPermission() == 1) {
                    isAuthenticated = true;

                    String action = request.getParameter("action");

                    if ("showCities".equals(action)) {
                        // Handle city dropdown update
                        String provinceIdStr = request.getParameter("provinceId");
                        if (provinceIdStr != null && !provinceIdStr.isEmpty()) {
                            int provinceId = Integer.parseInt(provinceIdStr);
                            List<City> cities = List.of(City.getList(provinceId));
                            request.setAttribute("cities", cities);
                        }
                    } else {
                        // Handle form submission
                        String schoolName = request.getParameter("textSchoolName");
                        String cityIdStr = request.getParameter("listCity");
                        String schoolTypeStr = request.getParameter("listSchoolType");

                        if (schoolName != null && !schoolName.trim().isEmpty() &&
                                cityIdStr != null && !cityIdStr.isEmpty() &&
                                schoolTypeStr != null && !schoolTypeStr.isEmpty()) {

                            School school = new School();
                            int cityID = Integer.parseInt(cityIdStr);
                            int schoolType = Integer.parseInt(schoolTypeStr);

                            int result = school.add(schoolName, schoolType, cityID);

                            switch (result) {
                                case 0:
                                    errorMessage = "آموزشگاه مورد نظر ثبت گردید";
                                    break;
                                case 1:
                                    errorMessage = "مدرسه/دانشگاهی با این نام در شهر مذکور وجود دارد";
                                    break;
                                case 9:
                                    errorMessage = "اشکال در ثبت اطلاعات";
                                    break;
                                default:
                                    errorMessage = "نتیجه نامشخص: " + result;
                                    break;
                            }
                        } else {
                            errorMessage = "لطفا تمام فیلدها را پر کنید";
                        }
                    }

                    // Reload dropdown data
                    List<SchoolType> schoolTypes = List.of(SchoolType.getList());
                    List<Province> provinces = List.of(new Province().getList());
                    request.setAttribute("schoolTypes", schoolTypes);
                    request.setAttribute("provinces", provinces);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                errorMessage = "خطا در پردازش درخواست";
            }
        }

        if (!isAuthenticated) {
            errorMessage = "شما مجوز دسترسی به این صفحه را ندارید";
        }

        request.setAttribute("panelVisible", isAuthenticated);
        request.setAttribute("errorMessage", errorMessage);

        // Forward to Thymeleaf template
        request.getRequestDispatcher("/WEB-INF/templates/admin/add-school.jsp").forward(request, response);
    }
}
