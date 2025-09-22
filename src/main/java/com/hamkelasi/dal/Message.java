package com.hamkelasi.dal;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

public class Message extends Base {

    public List<Row> get(int id) {
        return executeSelect(Types.NULL, "SELECT * FROM PM WHERE ID = ?",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public List<Row> getInboxListID(int userID) {
        return executeSelect(Types.NULL, "SELECT * FROM PM WHERE ToUser = ? ORDER BY SendDate DESC",
                SqlParameter.in(1, Types.INTEGER, userID));
    }

    public boolean markAsRead(int messageID) {
        return jdbcTemplate.update("UPDATE PM SET Status = 1 WHERE ID = ?",
                messageID) > 0;
    }

    public List<Row> getOutboxListID(int userID) {
        return executeSelect(Types.NULL, "SELECT * FROM PM WHERE FromUser = ? ORDER BY SendDate DESC",
                SqlParameter.in(1, Types.INTEGER, userID));
    }

    public boolean add(int receiverID, int senderID, LocalDateTime date, String text, String subject) {
        Integer lastID = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM PM", Integer.class);
        int id = (lastID == null) ? 1 : lastID + 1;
        return jdbcTemplate.update("INSERT INTO PM (ID, ToUser, FromUser, SendDate, PmText, Subject, Status) VALUES (?, ?, ?, ?, ?, ?, 0)",
                id, receiverID, senderID, date, text, subject) > 0;
    }

    private int getLastID() {
        Integer last = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM PM", Integer.class);
        return last == null ? 0 : last;
    }
}