package com.hamkelasi.ui;
import com.hamkelasi.bll.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/schools")
public class SchoolsServlet extends HttpServlet {
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

        // Initialize table rows for JSP
        List<TableRow> tableRows = new ArrayList<>();
        String pageTitle = "Schools"; // Default title

        // Check query string
        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            // List Provinces and Cities
            Province[] provinces = new Province().getList();
            for (Province province : provinces) {
                // Add province header
                TableRow provinceRow = new TableRow();
                TableCell headerCell = new TableCell("<h4 class=\"noUnderline\">" + province.getName() + "</h4>", 3);
                provinceRow.getCells().add(headerCell);
                tableRows.add(provinceRow);

                // Add cities
                City[] cities = City.getList(province.getId());
                TableRow cityRow = new TableRow();
                int currentCell = 0;
                for (City city : cities) {
                    TableCell cell = new TableCell();
                    cell.setHyperlink(city.getName(), request.getContextPath() + "/schools?cityID=" + city.getId());
                    cityRow.getCells().add(cell);

                    if (currentCell == 2) {
                        tableRows.add(cityRow);
                        cityRow = new TableRow();
                        currentCell = 0;
                    } else {
                        currentCell++;
                    }
                }
                if (!cityRow.getCells().isEmpty()) {
                    tableRows.add(cityRow);
                }

                // Add break
                TableRow breakRow = new TableRow();
                breakRow.getCells().add(new TableCell("<br/><br/>", 1));
                tableRows.add(breakRow);
            }
        } else if (request.getParameter("cityID") != null) {
            // List Schools
            int cityID = Integer.parseInt(request.getParameter("cityID"));
            City city = new City(cityID);
            pageTitle += " " + city.getName();

            SchoolType[] types = SchoolType.getList();
            for (SchoolType type : types) {
                // Add school type header
                TableRow typeRow = new TableRow();
                TableCell headerCell = new TableCell("<h4>" + type.getTypeName() + "</h4>", 3);
                typeRow.getCells().add(headerCell);
                tableRows.add(typeRow);

                // Add schools
                School[] schools = School.getList(cityID, type.getId());
                TableRow schoolRow = new TableRow();
                int currentCell = 0;
                for (School school : schools) {
                    TableCell cell = new TableCell();
                    cell.setHyperlink(school.getName(), request.getContextPath() + "/Schools?schoolID=" + school.getId());
                    schoolRow.getCells().add(cell);

                    if (currentCell == 1) {
                        tableRows.add(schoolRow);
                        schoolRow = new TableRow();
                        currentCell = 0;
                    } else {
                        currentCell++;
                    }
                }
                if (!schoolRow.getCells().isEmpty()) {
                    tableRows.add(schoolRow);
                }
            }
        } else if (request.getParameter("schoolID") != null) {
            // List Years
            int schoolID = Integer.parseInt(request.getParameter("schoolID"));
            School school = new School(schoolID);
            pageTitle = school.getName();

            Year[] years = Year.getList();
            for (Year year : years) {
                TableRow yearRow = new TableRow();
                TableCell cell = new TableCell();
                cell.setHyperlink("سال تحصیلی " + year.getAcademicYear(),
                        request.getContextPath() + "/sharing?schoolID=" + schoolID + "&yearID=" + year.getId());
                yearRow.getCells().add(cell);
                tableRows.add(yearRow);
            }
        }

        // Set attributes for JSP
        request.setAttribute("tableRows", tableRows);
        request.setAttribute("pageTitle", pageTitle);
        request.getRequestDispatcher("/WEB-INF/templates/schools.jsp").forward(request, response);
    }

    // Helper class to represent table rows
    public static class TableRow {
        private List<TableCell> cells = new ArrayList<>();

        public List<TableCell> getCells() {
            return cells;
        }
    }

    // Helper class to represent table cells
    public static class TableCell {
        private String content;
        private String hyperlink;
        private Integer colspan;

        public TableCell() {
            this.content = "";
        }

        public TableCell(String content, int colspan) {
            this.content = content;
            this.colspan = colspan;
        }

        public TableCell(String content) {
            this.content = content;
        }

        public void setHyperlink(String text, String url) {
            this.content = "<a href=\"" + url + "\">" + text + "</a>";
        }

        public String getContent() {
            return content;
        }

        public Integer getColspan() {
            return colspan;
        }
    }
}