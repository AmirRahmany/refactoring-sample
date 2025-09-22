package com.hamkelasi.dal;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

public class MediaShare extends Base {
    public List<Row> getRecord(int id) {
        return executeSelect(Types.NULL, "SELECT * FROM MediaShare WHERE ID = ?",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public List<Row> getListID(int type, int schoolyearID) {
        return executeSelect(Types.NULL, "SELECT * FROM MediaShare WHERE ShareType = ? AND SchoolYearID = ? ORDER BY ID DESC",
                SqlParameter.in(1, Types.INTEGER, type),
                SqlParameter.in(2, Types.INTEGER, schoolyearID));
    }

    public int getCount(int type, int schoolyearID) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MediaShare WHERE ShareType = ? AND SchoolYearID = ?",
                new Object[]{type, schoolyearID}, new int[]{Types.INTEGER, Types.INTEGER}, Integer.class);
    }

    public boolean add(int userID, int schoolYearID, String subject, String description, String URL, String thumbURL,
                       LocalDateTime postDate, int shareType) {
        Integer lastID = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM MediaShare", Integer.class);
        int id = (lastID == null) ? 1 : lastID + 1;
        return jdbcTemplate.update("INSERT INTO MediaShare (ID, UserID, SchoolYearID, Subject, Description, URL, ThumbURL, PostDate, ShareType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, userID, schoolYearID, subject, description, URL, thumbURL, postDate, shareType) > 0;
    }

    private int getLastID() {
        Integer last = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM MediaShare", Integer.class);
        if (last == null) return 1;
        int id = last.intValue();
        return id + 1;
    }
}