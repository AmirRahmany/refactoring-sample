package com.hamkelasi.ui.admin;

import com.hamkelasi.bll.City;
import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/edit-cities")
public class EditCityServlet extends HttpServlet {

    private String oldCityName, oldProvinceId;

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
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isAuthenticated) {
            String idParam = request.getParameter("ID");
            String msgParam = request.getParameter("msg");

            if (msgParam != null) {
                if ("ok".equals(msgParam)) {
                    request.setAttribute("errorMessage", "شهر مورد نظر با موفقیت حذف گردید");
                } else if ("fail".equals(msgParam)) {
                    request.setAttribute("errorMessage", "اشکال در حذف شهر");
                }
            }

            if (idParam == null) {
                // Show province selection and city list
                request.setAttribute("panelEditCityVisible", false);
                request.setAttribute("panelChooseProvinceVisible", true);

                if (!isPostBack(request)) {
                    List<Province> provinceList = List.of(new Province().getList());
                    request.setAttribute("provinces", provinceList);
                }
            } else {
                // Edit specific city
                request.setAttribute("panelChooseProvinceVisible", false);
                request.setAttribute("tableCitiesVisible", false);

                int cityID = Integer.parseInt(idParam);
                City city = new City(cityID);

                oldCityName = city.getName();
                oldProvinceId = String.valueOf(city.getProvinceId());

                if (!isPostBack(request)) {
                    List<Province> provinceList = List.of(new Province().getList());
                    request.setAttribute("provinces", provinceList);

                    // Set selected province
                    for (Province province : provinceList) {
                        if (city.getProvinceId() == province.getId()) {
                            request.setAttribute("selectedProvinceId", province.getId());
                            break;
                        }
                    }

                    request.setAttribute("cityId", city.getId());
                    request.setAttribute("cityName", city.getName());
                }

                request.setAttribute("panelEditCityVisible", true);
            }
        } else {
            request.setAttribute("panelChooseProvinceVisible", false);
            request.setAttribute("tableCitiesVisible", false);
            request.setAttribute("panelEditCityVisible", false);
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/edit-cities.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        String errorMessage = null;

        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                User loggedUser = new User(loggedID);

                if (loggedUser.getPermission() == 1) {
                    if ("showCities".equals(action)) {
                        String provinceIdStr = request.getParameter("provinceId");
                        if (provinceIdStr != null) {
                            int provinceID = Integer.parseInt(provinceIdStr);
                            List<City> cities = List.of(City.getList(provinceID));
                            request.setAttribute("cities", cities);
                            request.setAttribute("selectedProvinceId", provinceID);
                            request.setAttribute("tableCitiesVisible", true);
                        }
                    } else if ("saveChanges".equals(action)) {
                        String cityIdStr = request.getParameter("cityId");
                        String cityName = request.getParameter("cityName");
                        String provinceIdStr = request.getParameter("provinceId");

                        if (cityIdStr != null && cityName != null && provinceIdStr != null) {
                            int cityId = Integer.parseInt(cityIdStr);
                            int provinceId = Integer.parseInt(provinceIdStr);

                            if (cityName.equals(oldCityName) && provinceIdStr.equals(oldProvinceId)) {
                                // No changes made
                                errorMessage = "هیچ تغییری اعمال نشده است";
                            } else {
                                City city = new City();
                                int result = city.update(cityId, cityName, provinceId);

                                switch (result) {
                                    case 0:
                                        errorMessage = "اطلاعات با موفقیت تغییر یافت";
                                        break;
                                    case 1:
                                        errorMessage = "شهری با همین نام در استان انتخاب شده وجود دارد";
                                        break;
                                    case 9:
                                        errorMessage = "اشکال در ثبت داده";
                                        break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                errorMessage = "خطا در پردازش درخواست";
            }
        } else {
            errorMessage = "شما مجوز دسترسی به این صفحه را ندارید";
        }

        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
        }

        // Reload necessary data
        List<Province> provinceList = List.of(new Province().getList());
        request.setAttribute("provinces", provinceList);

        request.getRequestDispatcher("/WEB-INF/templates/admin/edit-cities.jsp").forward(request, response);
    }

    private boolean isPostBack(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }
}