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

@WebServlet("/outbox")
public class OutboxServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Initialize table data
        List<TableRow> tableRows = new ArrayList<>();
        boolean tableInboxVisible = true;
        String labelError = null;

        if (session.getAttribute("UserID") != null) {
            int userID = Integer.parseInt(session.getAttribute("UserID").toString());
            List<Message> outboxMessages = new Message().getOutbox(userID);

            if (outboxMessages.size() > 0) {
                for (int i = 0; i < outboxMessages.size(); i++) {
                    Message message = outboxMessages.get(i);
                    User receiverUser = new User(message.getReceiverUser());
                    IranianCalendar shamsiDate = new IranianCalendar(toDate(message.getDate()));

                    TableRow row = new TableRow();
                    row.setCssClass((i % 2 == 0) ? "oddRow" : "evenRow");

                    row.getCells().add(new TableCell(receiverUser.getUsername()));
                    row.getCells().add(new TableCell(message.getSubject()));
                    row.getCells().add(new TableCell(shamsiDate.toString()));
                    row.getCells().add(new TableCell("<a href=\"ShowMessage?ID=" + message.getId() + "\"><img src=\"../Images/message-view.PNG\"/></a>"));

                    tableRows.add(row);
                }
            } else {
                labelError = "هیچ پیامی در صندوق ارسال موجود نمی باشد";
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

        request.getRequestDispatcher("/WEB-INF/templates/outbox.jsp").forward(request, response);
    }

    private Date toDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.of("UTC")).toInstant());
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

        public TableCell(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}
