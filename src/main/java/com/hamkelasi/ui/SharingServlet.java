package com.hamkelasi.ui;


import com.hamkelasi.bll.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/sharing")
public class SharingServlet extends HttpServlet {
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

        // Initialize variables
        int schoolID, yearID;
        String pageTitle = "";
        boolean panelFirstMemberVisible = false;
        boolean panelNotMemberVisible = false;
        boolean panelregisterUsersVisible = true;
        boolean panelPostsVisible = true;
        boolean panelMediaGalleryVisible = true;
        boolean panelPagingVisible = true;
        boolean panelNewPostVisible = false;
        boolean buttonRegisterVisible = false;
        String labelError = null;
        String labelPostError = null;
        List<String> comboPages = new ArrayList<>();
        List<TableRow> tableUsersRows = new ArrayList<>();
        List<TableRow> tablePostsRows = new ArrayList<>();
        String linkPictureListUrl = "";
        String linkVideoListUrl = "";
        String linkSoundListUrl = "";
        String labelPicGalleryInfo = "";
        String labelVideoGalleryInfo = "";
        String labelSoundGalleryInfo = "";

        // Validate query parameters
        String schoolIDStr = request.getParameter("schoolID");
        String yearIDStr = request.getParameter("yearID");
        if (schoolIDStr != null && yearIDStr != null) {
            try {
                schoolID = Integer.parseInt(schoolIDStr);
                yearID = Integer.parseInt(yearIDStr);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/schools");
                return;
            }

            School school = new School(schoolID);
            Year year = new Year(yearID);
            SchoolYear schoolYear = new SchoolYear(schoolID, yearID);

            pageTitle = school.getName() + "  |  " + " سال تحصیلی " + year.getAcademicYear();

            if (schoolYear.getId() != 0) {
                panelFirstMemberVisible = false;
                panelNotMemberVisible = false;

                // Registered users table
                User[] registeredUsers = SchoolRegistration.getUsers(schoolYear.getId());
                TableRow userRow = new TableRow();
                tableUsersRows.add(userRow);
                int lastRow = 0;
                int lastCell = 0;
                for (int i = 0; i < registeredUsers.length; i++) {
                    StringBuilder cellContent = new StringBuilder();
                    cellContent.append("<a href=\"Profile?ID=").append(registeredUsers[i].getId()).append("\">")
                            .append("<img src=\"").append(registeredUsers[i].getProfilePicture())
                            .append("\" width=\"50\" height=\"50\"/></a>");
                    userRow.getCells().add(new TableCell(cellContent.toString()));
                    lastCell++;
                    if (lastCell == 11) {
                        userRow = new TableRow();
                        tableUsersRows.add(userRow);
                        lastRow++;
                        lastCell = 0;
                    }
                }

                // Posts panel
                int postCount = TextSharing.getCount(schoolYear.getId());
                TextSharing[] posts = null;
                if (postCount == 0) {
                    labelPostError = "<h4 class=\"noUnderline\" align=\"right\"> پستی برای نمایش وجود ندارد </h4>";
                    panelPagingVisible = false;
                } else if (postCount <= 10) {
                    posts = TextSharing.getList(1, postCount, schoolYear.getId());
                    panelPagingVisible = false;
                } else {
                    int pageCount = postCount / 10;
                    if (postCount % 10 != 0) {
                        pageCount++;
                    }
                    if (!"true".equals(request.getAttribute("isPostBack"))) {
                        for (int i = 1; i <= pageCount; i++) {
                            comboPages.add(String.valueOf(i));
                        }
                    }
                    String pageStr = request.getParameter("Page");
                    int page = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;
                    if (!"true".equals(request.getAttribute("isPostBack"))) {
                        request.setAttribute("selectedPage", page);
                    }
                    if (page == pageCount) {
                        posts = TextSharing.getList((10 * (page - 1)) + 1, postCount, schoolYear.getId());
                    } else {
                        posts = TextSharing.getList((10 * (page - 1)) + 1, (10 * page), schoolYear.getId());
                    }
                }

                // Build posts table
                if (postCount != 0) {
                    for (int i = 0; i < posts.length; i++) {
                        User user = new User(posts[i].getUserId());
                        IranianCalendar shamsiDate = new IranianCalendar(toDate(posts[i].getPostDate()));

                        TableRow row1 = new TableRow();
                        StringBuilder pictureCellContent = new StringBuilder();
                        pictureCellContent.append("<img src=\"").append(user.getProfilePicture())
                                .append("\" width=\"50\" height=\"50\"/>");
                        StringBuilder nameCellContent = new StringBuilder();
                        nameCellContent.append("<a href=\"Profile?ID=").append(user.getId()).append("\">")
                                .append(user.getFirstname()).append(" ").append(user.getLastname()).append("</a>");
                        row1.getCells().add(new TableCell(pictureCellContent.toString(), 2));
                        row1.getCells().add(new TableCell(nameCellContent.toString()));
                        row1.getCells().add(new TableCell(shamsiDate.toString()));
                        tablePostsRows.add(row1);

                        TableRow row2 = new TableRow();
                        StringBuilder postCellContent = new StringBuilder();
                        postCellContent.append(posts[i].getText());
                        if (!posts[i].getUrl().isEmpty()) {
                            String url = posts[i].getUrl().startsWith("http://") ? posts[i].getUrl() : "http://" + posts[i].getUrl();
                            postCellContent.append("<br/><br/><a href=\"").append(url).append("\">لینک ضمیمه</a>");
                        }
                        row2.getCells().add(new TableCell(postCellContent.toString(), 2));
                        tablePostsRows.add(row2);
                    }
                }

                // New post panel
                if (session.getAttribute("UserID") != null) {
                    boolean isUserRegistered = false;
                    int userID = Integer.parseInt((String) session.getAttribute("UserID"));
                    for (User registeredUser : registeredUsers) {
                        if (userID == registeredUser.getId()) {
                            isUserRegistered = true;
                            break;
                        }
                    }
                    if (isUserRegistered) {
                        panelNewPostVisible = true;
                    } else {
                        buttonRegisterVisible = true;
                    }
                }

                // Gallery panel
                int pictureCount = new MediaShare().getPictureCount(schoolYear.getId());
                int videoCount = new MediaShare().getVideoCount(schoolYear.getId());
                int soundCount = new MediaShare().getSoundCount(schoolYear.getId());

                linkPictureListUrl = "Gallery?ID=" + schoolYear.getId() + "&Type=1";
                linkVideoListUrl = "Gallery?ID=" + schoolYear.getId() + "&Type=2";
                linkSoundListUrl = "Gallery?ID=" + schoolYear.getId() + "&Type=3";

                labelPicGalleryInfo = pictureCount == 0 ? "هیچ تصویری موجود نمی باشد" : "تعداد تصاویر : " + pictureCount + " فایل ";
                labelVideoGalleryInfo = videoCount == 0 ? "هیچ ویدئویی موجود نمی باشد" : "تعداد ویدئو ها : " + videoCount + " فایل ";
                labelSoundGalleryInfo = soundCount == 0 ? "هیچ فایل صوتی موجود نمی باشد" : "تعداد تصاویر : " + soundCount + " فایل ";
            } else {
                panelregisterUsersVisible = false;
                panelPostsVisible = false;
                panelMediaGalleryVisible = false;
                if (session.getAttribute("UserID") == null) {
                    panelNotMemberVisible = true;
                    panelFirstMemberVisible = false;
                } else {
                    panelFirstMemberVisible = true;
                    panelNotMemberVisible = false;
                }
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/schools");
            return;
        }

        // Set attributes for JSP
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("panelFirstMemberVisible", panelFirstMemberVisible);
        request.setAttribute("panelNotMemberVisible", panelNotMemberVisible);
        request.setAttribute("panelregisterUsersVisible", panelregisterUsersVisible);
        request.setAttribute("panelPostsVisible", panelPostsVisible);
        request.setAttribute("panelMediaGalleryVisible", panelMediaGalleryVisible);
        request.setAttribute("panelPagingVisible", panelPagingVisible);
        request.setAttribute("panelNewPostVisible", panelNewPostVisible);
        request.setAttribute("buttonRegisterVisible", buttonRegisterVisible);
        request.setAttribute("labelError", labelError);
        request.setAttribute("labelPostError", labelPostError);
        request.setAttribute("comboPages", comboPages);
        request.setAttribute("tableUsersRows", tableUsersRows);
        request.setAttribute("tablePostsRows", tablePostsRows);
        request.setAttribute("linkPictureListUrl", linkPictureListUrl);
        request.setAttribute("linkVideoListUrl", linkVideoListUrl);
        request.setAttribute("linkSoundListUrl", linkSoundListUrl);
        request.setAttribute("labelPicGalleryInfo", labelPicGalleryInfo);
        request.setAttribute("labelVideoGalleryInfo", labelVideoGalleryInfo);
        request.setAttribute("labelSoundGalleryInfo", labelSoundGalleryInfo);
        request.setAttribute("schoolID", schoolID);
        request.setAttribute("yearID", yearID);

        request.getRequestDispatcher("/WEB-INF/templates/sharing.jsp").forward(request, response);
    }

    private Date toDate(LocalDateTime postDate) {
        return Date.from(postDate.atZone(ZoneId.of("UTC")).toInstant());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int schoolID = Integer.parseInt(request.getParameter("schoolID"));
        int yearID = Integer.parseInt(request.getParameter("yearID"));
        HttpSession session = request.getSession();
        String redirectUrl = request.getRequestURI() + "?schoolID=" + schoolID + "&yearID=" + yearID;

        if ("firstRegister".equals(action)) {
            SchoolYear schoolYear = new SchoolYear();
            schoolYear.setSchoolId(schoolID);
            schoolYear.setYearId(yearID);
            schoolYear.add();
            SchoolRegistration registration = new SchoolRegistration();
            registration.setSchoolYearId(schoolYear.getId());
            registration.setUserId(Integer.parseInt((String) session.getAttribute("UserID")));
            registration.add();

            request.setAttribute("labelError", "ثبت داده با موفقیت انجام شد");
            request.setAttribute("panelFirstMemberVisible", false);
            request.setAttribute("panelNotMemberVisible", false);
            doGet(request, response);
        } else if ("gotoPage".equals(action)) {
            String page = request.getParameter("comboPages");
            String currentPage = request.getParameter("Page") != null ? request.getParameter("Page") : "1";
            if (!page.equals(currentPage)) {
                redirectUrl += page.equals("1") ? "" : "&Page=" + page;
                response.sendRedirect(redirectUrl);
            } else {
                doGet(request, response);
            }
        } else if ("sendPost".equals(action)) {
            if (isValidForm(request)) {
                TextSharing newPost = new TextSharing();
                newPost.setText(request.getParameter("textPost"));
                newPost.setUrl(request.getParameter("textPostLink"));
                if (newPost.isValid()) {
                    SchoolYear schoolYear = new SchoolYear(schoolID, yearID);
                    newPost.setPostDate(LocalDateTime.now());
                    newPost.setUserId(Integer.parseInt((String) session.getAttribute("UserID")));
                    newPost.setSchoolYearId(schoolYear.getId());
                    int result = newPost.add();
                    if (result == 1) {
                        response.sendRedirect(redirectUrl);
                    } else {
                        request.setAttribute("labelPostError", "خطا در ثبت داده");
                        doGet(request, response);
                    }
                } else {
                    request.setAttribute("labelPostError", "فرمت لینک وارد شده نادرست است");
                    doGet(request, response);
                }
            } else {
                request.setAttribute("labelPostError", "لطفا متن پست را وارد کنید");
                doGet(request, response);
            }
        } else if ("register".equals(action)) {
            SchoolYear schoolYear = new SchoolYear(schoolID, yearID);
            SchoolRegistration registration = new SchoolRegistration();
            registration.setSchoolYearId(schoolYear.getId());
            registration.setUserId(Integer.parseInt((String) session.getAttribute("UserID")));
            registration.add();
            request.setAttribute("labelError", "ثبت داده با موفقیت انجام شد");
            doGet(request, response);
        } else {
            doGet(request, response);
        }
    }

    private boolean isValidForm(HttpServletRequest request) {
        return request.getParameter("textPost") != null && !request.getParameter("textPost").isEmpty();
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
        private int rowSpan;
        private int colSpan;

        public TableCell(String content) {
            this(content, 1);
        }

        public TableCell(String content, int rowSpan) {
            this.content = content;
            this.rowSpan = rowSpan;
            this.colSpan = 1;
        }

        public String getContent() {
            return content;
        }

        public int getRowSpan() {
            return rowSpan;
        }

        public int getColSpan() {
            return colSpan;
        }

        public void setColSpan(int colSpan) {
            this.colSpan = colSpan;
        }
    }
}