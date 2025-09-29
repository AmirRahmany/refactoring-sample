package com.hamkelasi.ui;


import com.hamkelasi.bll.IranianCalendar;
import com.hamkelasi.bll.Message;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/inbox")
public class InboxServlet extends HttpServlet {
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
        List<TableRow> tableRows = new ArrayList<>();
        boolean tableInboxVisible = true;
        String labelError = null;

        if (session.getAttribute("UserID") != null) {
            int userID = Integer.parseInt(session.getAttribute("UserID").toString());
            List<Message> inboxMessages = new Message().getInbox(userID);

            if (inboxMessages.size() > 0) {
                for (int i = 0; i < inboxMessages.size(); i++) {
                    Message message = inboxMessages.get(i);
                    User senderUser = new User(message.getSenderUser());
                    IranianCalendar shamsiDate = new IranianCalendar(toDate(message.getDate()));

                    TableRow row = new TableRow();
                    row.setCssClass((i % 2 == 0) ? "oddRow" : "evenRow");

                    String imageStatusUrl = message.getStatus() == Message.MessageStatus.READ ?
                            "../Images/message-read.PNG" : "../Images/message-unread.PNG";
                    boolean isBold = message.getStatus() != Message.MessageStatus.READ;

                    row.getCells().add(new TableCell("<img src=\"" + imageStatusUrl + "\"/>"));
                    row.getCells().add(new TableCell(senderUser.getUsername(), isBold));
                    row.getCells().add(new TableCell(message.getSubject(), isBold));
                    row.getCells().add(new TableCell(shamsiDate.toString(), isBold));
                    row.getCells().add(new TableCell("<a href=\"ShowMessage?ID=" + message.getId() + "\"><img src=\"../Images/message-view.PNG\"/></a>"));

                    tableRows.add(row);
                }
            } else {
                labelError = "هیچ پیامی در صندوق دریافت موجود نمی باشد";
                tableInboxVisible = false;
            }
        } else {
            tableInboxVisible = false;
            labelError = "شما مجوز دسترسی به این صفحه را ندارید";
        }

        // Set attributes for JSP
        request.setAttribute("tableRows", tableRows);
        request.setAttribute("tableInboxVisible", tableInboxVisible);
        request.setAttribute("labelError", labelError);

        request.getRequestDispatcher("/WEB-INF/templates/inbox.jsp").forward(request, response);
    }

    private Date toDate(LocalDateTime date) {
        return  Date.from(date.atZone(ZoneId.of("UTC")).toInstant());
    }

    // Helper class to represent table rows
    public static class TableRow {
        private List<TableCell> cells = new ArrayList<>();
        private String cssClass;

        public List<TableCell> getCells() {
            return cells;
        }

        public String getCssClass() {
            return cssClass;
        }

        public void setCssClass(String cssClass) {
            this.cssClass = cssClass;
        }
    }

    // Helper class to represent table cells
    public static class TableCell {
        private String content;
        private boolean bold;

        public TableCell(String content) {
            this(content, false);
        }

        public TableCell(String content, boolean bold) {
            this.content = content;
            this.bold = bold;
        }

        public String getContent() {
            return content;
        }

        public boolean isBold() {
            return bold;
        }
    }
}
