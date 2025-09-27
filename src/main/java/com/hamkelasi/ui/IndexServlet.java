package com.hamkelasi.ui;

import com.hamkelasi.bll.City;
import com.hamkelasi.bll.Province;
import com.hamkelasi.bll.School;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;


@WebServlet("/home")
public class IndexServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(IndexServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("IndexServlet doGet() called");

        //info
        //Statics Part
        request.setAttribute("memberCount",User.getCount());
        request.setAttribute("provinceCount",Province.getCount());
        request.setAttribute("cityCount", City.getCount());
        request.setAttribute("schoolCount", School.getCount());


        HttpSession session = request.getSession();
        logger.info("Session ID: " + session.getId());

        // Check for UserID cookie
        Cookie[] cookies = request.getCookies();
        logger.info("Number of cookies: " + (cookies != null ? cookies.length : 0));

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.info("Cookie: " + cookie.getName() + " = " + cookie.getValue());
                if ("UserID".equals(cookie.getName()) && session.getAttribute("UserID") == null) {
                    session.setAttribute("UserID", cookie.getValue());
                    logger.info("Set UserID from cookie: " + cookie.getValue());
                }
            }
        }

        logger.info("Forwarding to index.jsp");

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/templates/index.jsp").forward(request, response);
    }
}