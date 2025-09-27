package com.hamkelasi.ui;

import com.hamkelasi.bll.Friendship;
import com.hamkelasi.bll.Friendship.FriendshipStatus;
import com.hamkelasi.bll.Permission;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
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
        String queryString = request.getParameter("ID");
        String action = request.getParameter("action");
        int userID;
        User user = new User();
        String labelNotification = null;
        boolean labelNotificationVisible = false;
        String labelUsername ;
        String labelFirstName = "";
        String labelLastName = "";
        String labelEmail = "";
        String labelWebsite = "";
        String labelPermission = "";
        String usernameCssClass = "";
        String imageProfileUrl = "";
        List<TableRow> tableRows = new ArrayList<>();
        boolean buttonAddFriendVisible = false;
        boolean buttonConfirmFriendVisible = false;
        boolean buttonRejectFriendVisible = false;
        boolean buttonWaitingForConfirmVisible = false;

        // Validate query string
        if (queryString != null && !queryString.isEmpty()) {
            try {
                userID = Integer.parseInt(queryString);
                user = new User(userID);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/members");
                return;
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/members");
            return;
        }

        // Handle action notifications
        if ("requestSend".equals(action)) {
            labelNotification = "درخواست دوستی با موفقیت ارسال شد";
            labelNotificationVisible = true;
        } else if ("confirmed".equals(action)) {
            labelNotification = "درخواست دوستی با موفقیت تایید شد";
            labelNotificationVisible = true;
        } else if ("rejected".equals(action)) {
            labelNotification = "درخواست دوستی با موفقیت رد شد";
            labelNotificationVisible = true;
        } else if ("fail".equals(action)) {
            labelNotification = "اشکال در ثبت اطلاعات";
            labelNotificationVisible = true;
        }

        // Set user details
        
        Permission permission = new Permission();
        String strPer = permission.getPermissionName(user.getPermission());
        labelUsername = user.getUsername();
        labelFirstName = user.getFirstname();
        labelLastName = user.getLastname();
        labelEmail = user.getEmail();
        labelWebsite = user.getWebsite();
        imageProfileUrl = user.getProfilePicture();

        switch (strPer) {
            case "Administrator":
                labelPermission = "مدیر کل سایت";
                usernameCssClass = "Administrator";
                break;
            case "Moderator":
                labelPermission = "مدیر سایت";
                break;
            case "User":
                labelPermission = "کاربر معمولی";
                break;
            case "Banned":
                labelPermission = "کاربر اخراج شده";
                usernameCssClass = "BannedUser";
                break;
        }

        // Build friends table
        Friendship friendship = new Friendship();
        User[] friends = friendship.getFriendList(userID);
        if (friends.length > 0) {
            int currentRow = 0;
            int currentCell = 0;
            TableRow row = new TableRow();
            tableRows.add(row);
            for (int i = 0; i < friends.length; i++) {
                if (currentCell % 5 == 0 && currentCell != 0) {
                    currentCell = 0;
                    currentRow++;
                    row = new TableRow();
                    tableRows.add(row);
                }
                StringBuilder cellContent = new StringBuilder();
                cellContent.append("<a href=\"Profile?ID=").append(friends[i].getId()).append("\">")
                        .append("<img src=\"").append(friends[i].getProfilePicture())
                        .append("\" width=\"50\" height=\"50\"/></a>");
                row.getCells().add(new TableCell(cellContent.toString()));
                currentCell++;
            }
        }

        // Handle friendship status and button visibility
        if (session.getAttribute("UserID") != null) {
            int sessionUserId = Integer.parseInt(session.getAttribute("UserID").toString());
            if (sessionUserId != user.getId()) {
                FriendshipStatus status = friendship.getFriendshipStatus(sessionUserId, user.getId());
                if (status == Friendship.FriendshipStatus.NOT_FRIEND) {
                    buttonAddFriendVisible = true;
                } else if (status == FriendshipStatus.WAITING_FOR_CONFIRM) {
                    buttonConfirmFriendVisible = true;
                    buttonRejectFriendVisible = true;
                } else if (status == FriendshipStatus.WAITING_FOR_RESPONSE) {
                    buttonWaitingForConfirmVisible = true;
                    buttonAddFriendVisible = false;
                }
            }
        }

        // Set attributes for JSP
        request.setAttribute("labelNotification", labelNotification);
        request.setAttribute("labelNotificationVisible", labelNotificationVisible);
        request.setAttribute("labelUsername", labelUsername);
        request.setAttribute("labelFirstName", labelFirstName);
        request.setAttribute("labelLastName", labelLastName);
        request.setAttribute("labelEmail", labelEmail);
        request.setAttribute("labelWebsite", labelWebsite);
        request.setAttribute("labelPermission", labelPermission);
        request.setAttribute("usernameCssClass", usernameCssClass);
        request.setAttribute("imageProfileUrl", imageProfileUrl);
        request.setAttribute("tableRows", tableRows);
        request.setAttribute("buttonAddFriendVisible", buttonAddFriendVisible);
        request.setAttribute("buttonConfirmFriendVisible", buttonConfirmFriendVisible);
        request.setAttribute("buttonRejectFriendVisible", buttonRejectFriendVisible);
        request.setAttribute("buttonWaitingForConfirmVisible", buttonWaitingForConfirmVisible);
        request.setAttribute("userID", userID);

        request.getRequestDispatcher("/WEB-INF/templates/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int userID = Integer.parseInt(request.getParameter("userID"));
        HttpSession session = request.getSession();
        int sessionUserId = Integer.parseInt((String) session.getAttribute("UserID"));
        Friendship friendship = new Friendship();
        String redirectUrl = request.getRequestURI() + "?ID=" + userID;

        if ("addFriend".equals(action)) {
            friendship.add(sessionUserId, userID, FriendshipStatus.WAITING_FOR_RESPONSE);
            friendship.add(userID, sessionUserId, FriendshipStatus.WAITING_FOR_CONFIRM);
            response.sendRedirect(redirectUrl + "&action=requestSend");
        } else if ("confirmFriend".equals(action)) {
            friendship.confirmFriendship(userID, sessionUserId);
            response.sendRedirect(redirectUrl + "&action=confirmed");
        } else if ("rejectFriend".equals(action)) {
            int result = friendship.delete(userID, sessionUserId);
            if (result == 0) {
                response.sendRedirect(redirectUrl + "&action=rejected");
            } else {
                response.sendRedirect(redirectUrl + "&action=fail");
            }
        } else {
            doGet(request, response);
        }
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
