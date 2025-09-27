package com.hamkelasi.ui;


import com.hamkelasi.bll.City;
import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.School;
import com.hamkelasi.bll.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class Site1Filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        // Check for UserID cookie
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName()) && session.getAttribute("UserID") == null) {
                    session.setAttribute("UserID", String.valueOf(cookie.getValue()));
                }
            }
        }

        // Set statistics
        request.setAttribute("labelMemberCount", "تعداد اعضا : " + User.getCount());
        request.setAttribute("labelProvinceCount", "تعداد استان ها : " + Province.getCount());
        request.setAttribute("labelCityCount", "تعداد شهر ها : " + City.getCount());
        request.setAttribute("labelSchoolCount", "تعداد مدارس : " + School.getCount());

        // Handle UI visibility based on login status
        if (session.getAttribute("UserID") == null) {
            request.setAttribute("linkRegisterVisible", true);
            request.setAttribute("linkPanelVisible", false);
            request.setAttribute("linkExitVisible", false);
            request.setAttribute("userPanelVisible", false);
            request.setAttribute("linkSendInviteVisible", false);
            request.setAttribute("linkAdminPanelVisible", false);
            request.setAttribute("loginVisible", true);
        } else {
            request.setAttribute("linkRegisterVisible", false);
            request.setAttribute("linkPanelVisible", true);
            request.setAttribute("linkExitVisible", true);
            request.setAttribute("userPanelVisible", true);
            request.setAttribute("linkSendInviteVisible", true);
            request.setAttribute("loginVisible", false);

            int userId = Integer.parseInt(session.getAttribute("UserID").toString());
            User user = new User(userId);
            request.setAttribute("welcomeMessage", "خوش آمدید، " + user.getUsername());
            request.setAttribute("linkAdminPanelVisible", user.getPermission() == 1);
        }

        // Continue request processing
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
