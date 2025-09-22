package com.hamkelasi.ui;

import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/members")
public class MembersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check for UserID cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName()) && session.getAttribute("UserID") == null) {
                    session.setAttribute("UserID", cookie.getValue());
                }
            }
        }

        // Pagination logic
        String strPage = request.getParameter("PageNum");
        int pageNo = (strPage != null && !strPage.isEmpty()) ? Integer.parseInt(strPage) : 1;

        int userCount = User.getCount();
        int pageCount = userCount / 20;
        int remain = userCount % 20;
        if (pageCount < 1) {
            pageCount = 1;
            remain = 0;
        } else if (remain > 0) {
            pageCount++;
        }

        // Populate page dropdown if not a postback
        List<String> comboPages = new ArrayList<>();
        if (!"true".equals(request.getAttribute("isPostBack"))) {
            for (int i = 1; i <= pageCount; i++) {
                comboPages.add(String.valueOf(i));
            }
            request.setAttribute("comboPages", comboPages);
            request.setAttribute("selectedPage", pageNo);
        }

        request.setAttribute("labelPageCount", "مجموع صفحات : " + pageCount);

        // Fetch users for the current page
        int rowCount, rowRemain, pageStartNo;
        User[] members;
        if (pageNo == pageCount) {
            if (pageCount == 1) {
                rowCount = userCount / 4;
                rowRemain = userCount % 4;
                members = User.getUserByRowRange(1, userCount);
            } else {
                rowCount = (userCount - (20 * (pageNo - 1))) / 4;
                rowRemain = userCount - (20 * (pageNo - 1));
                pageStartNo = (20 * (pageCount - 1));
                members = User.getUserByRowRange(pageStartNo + 1, pageStartNo + rowRemain);
            }
            if (rowCount < 1) {
                rowCount = 1;
            }
        } else {
            rowCount = 5;
            rowRemain = 0;
            pageStartNo = ((pageNo - 1) * 20) + 1;
            members = User.getUserByRowRange(pageStartNo, pageStartNo + 19);
        }

        System.out.println(members);

        // Set attributes for JSP
        request.setAttribute("members", members);
        request.getRequestDispatcher("/WEB-INF/templates/members.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GotoPage_Click
        String page = request.getParameter("comboPages");
        response.sendRedirect(request.getContextPath() + "/members?PageNum=" + page);
    }

    // Helper class for table rows
    public static class TableRow {
        private List<TableCell> cells = new ArrayList<>();

        public List<TableCell> getCells() {
            return cells;
        }
    }

    // Helper class for table cells
    public static class TableCell {
        private String content;

        public TableCell(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}
