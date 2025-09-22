package com.hamkelasi.ui.admin;


import com.hamkelasi.bll.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/edit-schools")
public class EditSchoolServlet extends HttpServlet {

    private String oldCityID, oldSchoolName, oldSchoolType;

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
                    request.setAttribute("errorMessage", "آموزشگاه مورد نظر با موفقیت حذف گردید");
                } else if ("fail".equals(msgParam)) {
                    request.setAttribute("errorMessage", "اشکال در حذف آموزشگاه");
                }
            }

            if (idParam == null) {
                // Show province/city selection and school list
                request.setAttribute("panelEditSchoolVisible", false);
                request.setAttribute("panelChooseProvinceVisible", true);

                if (!isPostBack(request)) {
                    List<Province> provinceList = List.of(new Province().getList());
                    request.setAttribute("provinces", provinceList);

                    if (!provinceList.isEmpty()) {
                        int provinceID = provinceList.get(0).getId();
                        List<City> cities = List.of(City.getList(provinceID));
                        request.setAttribute("cities", cities);
                    }
                }
            } else {
                // Edit specific school
                request.setAttribute("panelChooseProvinceVisible", false);
                request.setAttribute("tableSchoolsVisible", false);

                int schoolID = Integer.parseInt(idParam);
                School school = new School(schoolID);

                oldCityID = String.valueOf(school.getCityId());
                oldSchoolName = school.getName();
                oldSchoolType = String.valueOf(school.getType());

                if (!isPostBack(request)) {
                    City city = new City(school.getCityId());
                    List<Province> provinceList = List.of(new Province().getList());
                    request.setAttribute("provinces", provinceList);

                    // Set selected province
                    for (int i = 0; i < provinceList.size(); i++) {
                        if (city.getProvinceId() == provinceList.get(i).getId()) {
                            request.setAttribute("selectedProvinceId", provinceList.get(i).getId());
                            break;
                        }
                    }

                    // Load cities for selected province
                    List<City> cityList = List.of(City.getList(city.getProvinceId()));
                    request.setAttribute("cities", cityList);

                    // Load school types
                    List<SchoolType> types = List.of(SchoolType.getList());
                    request.setAttribute("schoolTypes", types);

                    request.setAttribute("schoolId", school.getId());
                    request.setAttribute("schoolName", school.getName());
                    request.setAttribute("selectedSchoolTypeId", school.getType());
                    request.setAttribute("selectedCityId", school.getCityId());
                }

                request.setAttribute("panelEditSchoolVisible", true);
            }
        } else {
            request.setAttribute("panelChooseProvinceVisible", false);
            request.setAttribute("tableSchoolsVisible", false);
            request.setAttribute("panelEditSchoolVisible", false);
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/edit-schools.jsp").forward(request, response);
    }

    @Override
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
                        }
                    } else if ("showSchools".equals(action)) {
                        String cityIdStr = request.getParameter("cityId");
                        if (cityIdStr != null) {
                            int cityID = Integer.parseInt(cityIdStr);
                            List<School> schools = School.getList(cityID);

                            if (schools != null && !schools.isEmpty()) {
                                request.setAttribute("schools", schools);
                                request.setAttribute("tableSchoolsVisible", true);
                            } else {
                                errorMessage = "هیچ مدرسه ای در شهر انتخاب شده وجود ندارد";
                            }
                            request.setAttribute("selectedCityId", cityID);
                        }
                    } else if ("fillCity".equals(action)) {
                        String provinceIdStr = request.getParameter("provinceId");
                        if (provinceIdStr != null) {
                            int provinceID = Integer.parseInt(provinceIdStr);
                            List<City> cities = List.of(City.getList(provinceID));
                            request.setAttribute("cities", cities);
                            request.setAttribute("selectedProvinceId", provinceID);
                        }
                    } else if ("saveChanges".equals(action)) {
                        String schoolIdStr = request.getParameter("schoolId");
                        String schoolName = request.getParameter("schoolName");
                        String schoolTypeStr = request.getParameter("schoolType");
                        String cityIdStr = request.getParameter("cityId");

                        if (schoolIdStr != null && schoolName != null && schoolTypeStr != null && cityIdStr != null) {
                            int schoolID = Integer.parseInt(schoolIdStr);
                            int schoolType = Integer.parseInt(schoolTypeStr);
                            int cityID = Integer.parseInt(cityIdStr);

                            if (schoolName.equals(oldSchoolName) && cityIdStr.equals(oldCityID) && schoolTypeStr.equals(oldSchoolType)) {
                                // No changes made
                                errorMessage = "هیچ تغییری اعمال نشده است";
                            } else {
                                School school = new School();
                                boolean isValid = false;
                                int result = 0;

                                if (oldSchoolName.equals(schoolName)) {
                                    isValid = true;
                                } else {
                                    isValid = school.isUpdateValid(schoolName, cityID);
                                    if (!isValid) {
                                        result = 1;
                                    }
                                }

                                if (isValid) {
                                    result = school.update(schoolID, schoolName, schoolType, cityID);
                                }

                                switch (result) {
                                    case 0:
                                        errorMessage = "اطلاعات با موفقیت تغییر یافت";
                                        break;
                                    case 1:
                                        errorMessage = "آموزشگاهی با همین نام در شهر انتخاب شده وجود دارد";
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
        List<SchoolType> schoolTypes = List.of(SchoolType.getList());
        request.setAttribute("schoolTypes", schoolTypes);

        request.getRequestDispatcher("/WEB-INF/templates/admin/edit-schools.jsp").forward(request, response);
    }

    private boolean isPostBack(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }
}