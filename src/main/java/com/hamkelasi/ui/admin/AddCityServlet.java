package com.hamkelasi.ui.admin;
import com.hamkelasi.bll.City;
import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


import java.io.IOException;
import java.util.List;

@WebServlet("/admin/add-city")
public class AddCityServlet extends HttpServlet {

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

        // Check authentication and permission
        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                // Assuming you have a User class similar to BLL.User
                User loggedUser = new User(loggedID);

                if (loggedUser.getPermission() == 1) {
                    isAuthenticated = true;

                    // Load provinces for dropdown if not postback
                    List<Province> provinces = List.of(new Province().getList());
                    request.setAttribute("provinces", provinces);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!isAuthenticated) {
            request.setAttribute("panelVisible", false);
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
        } else {
            request.setAttribute("panelVisible", true);
        }

        request.getRequestDispatcher("/WEB-INF/templates/admin/add-city.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        boolean isAuthenticated = false;

        // Re-check authentication on POST
        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                User loggedUser = new User(loggedID);

                if (loggedUser.getPermission() == 1) {
                    isAuthenticated = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!isAuthenticated) {
            request.setAttribute("panelVisible", false);
            request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
            request.getRequestDispatcher("/WEB-INF/templates/admin/add-city.jsp").forward(request, response);
            return;
        }

        // Process form submission
        String cityName = request.getParameter("textCityName");
        String provinceIdStr = request.getParameter("listProvince");

        if (cityName != null && !cityName.trim().isEmpty() &&
                provinceIdStr != null && !provinceIdStr.trim().isEmpty()) {

            try {
                City city = new City();
                int provinceID = Integer.parseInt(provinceIdStr);

                int result = city.add(cityName, provinceID);

                switch (result) {
                    case 0:
                        request.setAttribute("errorMessage", "شهر مورد نظر ثبت گردید");
                        break;
                    case 1:
                        request.setAttribute("errorMessage", "شهری با این نام در استان مذکور وجود دارد");
                        break;
                    case 9:
                        request.setAttribute("errorMessage", "اشکال در ثبت اطلاعات");
                        break;
                    default:
                        request.setAttribute("errorMessage", "نتیجه نامشخص: " + result);
                        break;
                }

            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "فرمت استان نامعتبر است");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "خطا در پردازش: " + e.getMessage());
            }
        } else {
            request.setAttribute("errorMessage", "لطفا تمام فیلدها را پر کنید");
        }

        // Reload provinces for dropdown
        List<Province> provinces = List.of(new Province().getList());
        request.setAttribute("provinces", provinces);
        request.setAttribute("panelVisible", true);

        request.getRequestDispatcher("/WEB-INF/templates/admin/add-city.jsp").forward(request, response);
    }
}
