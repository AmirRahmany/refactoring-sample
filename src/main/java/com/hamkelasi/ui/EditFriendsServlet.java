package com.hamkelasi.ui;

import com.hamkelasi.bll.Friendship;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/edit-friends")
public class EditFriendsServlet extends HttpServlet {
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

        // Initialize table data
        List<TableRow> friendRows = new ArrayList<>();
        List<TableRow> waitingRows = new ArrayList<>();
        boolean tableFriendsVisible = true;
        boolean tableWaitingVisible = true;
        boolean headFriendsVisible = true;
        boolean headWaitingVisible = true;
        String labelError = null;

        if (session.getAttribute("UserID") != null) {
            int userID = Integer.parseInt((String) session.getAttribute("UserID"));
            String action = request.getParameter("action");
            String friendID = request.getParameter("friendID");

            if (action == null && friendID == null) {
                // List Friends
                List<User> friendList = List.of(new Friendship().getFriendList(userID));
                if (friendList.size() > 0) {
                    for (int i = 0; i < friendList.size(); i++) {
                        User friend = friendList.get(i);
                        TableRow row = new TableRow();
                        row.setBgColor((i % 2 == 0) ? "LightGrey" : "Silver");
                        row.getCells().add(new TableCell("<img src=\"Images/viewProfile.PNG\" width=\"50\" height=\"50\"/>"));
                        row.getCells().add(new TableCell(friend.getUsername()));
                        row.getCells().add(new TableCell(friend.getFirstname() + " " + friend.getLastname()));
                        row.getCells().add(new TableCell("<a href=\"Profile?ID=" + friend.getId() + "\"><img src=\"Images/viewProfile.PNG\"/></a>"));
                        row.getCells().add(new TableCell("<a href=\"EditFriends?action=delete&friendID=" + friend.getId() + "\"><img src=\"Images/delete-friendship.PNG\"/></a>"));
                        friendRows.add(row);
                    }
                } else {
                    tableFriendsVisible = false;
                    labelError = "هیچ کاربری در لیست دوستان شما موجود نمی باشد";
                }

                // List Waiting Users
                List<User> waitingUsers = new Friendship().getWaitingUsers(userID);
                if (waitingUsers.size() > 0) {
                    for (int i = 0; i < waitingUsers.size(); i++) {
                        User user = waitingUsers.get(i);
                        TableRow row = new TableRow();
                        row.setBgColor((i % 2 == 0) ? "LightGrey" : "Silver");
                        row.getCells().add(new TableCell("<img src=\"" + (user.getProfilePicture() != null ? user.getProfilePicture() : "") + "\" width=\"50\" height=\"50\"/>"));
                        row.getCells().add(new TableCell(user.getUsername()));
                        row.getCells().add(new TableCell(user.getFirstname() + " " + user.getLastname()));
                        row.getCells().add(new TableCell("<a href=\"Profile?ID=" + user.getId() + "\"><img src=\"Images/viewProfile.PNG\"/></a>"));
                        row.getCells().add(new TableCell("<a href=\"EditFriends?action=confirm&friendID=" + user.getId() + "\"><img src=\"Images/confirm-Friendship.PNG\"/></a>"));
                        waitingRows.add(row);
                    }
                } else {
                    tableWaitingVisible = false;
                    headWaitingVisible = false;
                }
            } else if (friendID != null) {
                int friendId = Integer.parseInt(friendID);
                Friendship friendClass = new Friendship();

                if ("delete".equals(action)) {
                    // Delete Friendship
                    int result = friendClass.delete(userID, friendId);
                    if (result == 0) {
                        labelError = "کاربر مورد نظر از لیست دوستان حذف گردید";
                        tableFriendsVisible = false;
                        headFriendsVisible = false;
                        tableWaitingVisible = false;
                        headWaitingVisible = false;
                    } else {
                        labelError = "خطا در حذف کاربر از لیست";
                    }
                } else if ("confirm".equals(action)) {
                    // Confirm Friendship
                    friendClass.confirmFriendship(userID, friendId);
                    labelError = "کاربر مورد نظر به لیست دوستان اضافه گردید";
                    tableFriendsVisible = false;
                    headFriendsVisible = false;
                    tableWaitingVisible = false;
                    headWaitingVisible = false;
                }
            }
        } else {
            tableFriendsVisible = false;
            tableWaitingVisible = false;
            headFriendsVisible = false;
            headWaitingVisible = false;
            labelError = "شما مجوز دسترسی به این صفحه را ندارید";
        }

        // Set attributes for JSP
        request.setAttribute("friendRows", friendRows);
        request.setAttribute("waitingRows", waitingRows);
        request.setAttribute("tableFriendsVisible", tableFriendsVisible);
        request.setAttribute("tableWaitingVisible", tableWaitingVisible);
        request.setAttribute("headFriendsVisible", headFriendsVisible);
        request.setAttribute("headWaitingVisible", headWaitingVisible);
        request.setAttribute("labelError", labelError);

        request.getRequestDispatcher("/WEB-INF/templates/edit-friends.jsp").forward(request, response);
    }

    // Helper class to represent table rows
    public static class TableRow {
        private List<TableCell> cells = new ArrayList<>();
        private String bgColor;

        public List<TableCell> getCells() {
            return cells;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }
    }

    // Helper class to represent table cells
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