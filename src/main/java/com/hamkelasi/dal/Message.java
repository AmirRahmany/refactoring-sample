package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class Message extends Base {
    public List<Row> get(int id) {
        return executeSelect(Types.NULL, "{ call PM_Get(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public List<Row> getInboxListID(int userID) {
        return executeSelect(Types.NULL, "{ call PM_GetInbox(?) }",
                SqlParameter.in(1, Types.INTEGER, userID));
    }

    public boolean markAsRead(int messageID) {
        return executeNonQuery(Types.NULL, "{ call PM_MarkAsRead(?) }",
                SqlParameter.in(1, Types.INTEGER, messageID));
    }

    public List<Row> getOutboxListID(int userID) {
        return executeSelect(Types.NULL, "{ call PM_GetOutbox(?) }",
                SqlParameter.in(1, Types.INTEGER, userID));
    }

    public boolean add(int receiverID, int senderID, java.util.Date date, String text, String subject) {
        int lastid = getLastID() + 1;
        return executeNonQuery(Types.NULL, "{ call PM_Add(?,?,?,?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, lastid),
                SqlParameter.in(2, Types.INTEGER, receiverID),
                SqlParameter.in(3, Types.INTEGER, senderID),
                SqlParameter.in(4, Types.TIMESTAMP, date),
                SqlParameter.in(5, Types.VARCHAR, text),
                SqlParameter.in(6, Types.VARCHAR, subject));
    }

    private int getLastID() {
        Object last = executeScalar(Types.INTEGER, "{ call PM_GetLastID() }");
        return (last instanceof Number) ? ((Number) last).intValue() : 0;
    }
}


