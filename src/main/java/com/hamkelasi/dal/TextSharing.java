package com.hamkelasi.dal;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

public class TextSharing extends Base {

    public boolean insert(int userid, int schoolyearid, String postText, String url, LocalDateTime date) {
        return jdbcTemplate.update("INSERT INTO TextShare (UserID, SchoolYearID, PostText, URL, PostDate) VALUES (?, ?, ?, ?, ?)",
                userid, schoolyearid, postText, url, date) > 0;
    }

    public int getCount(int schoolYearID) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TextShare WHERE SchoolYearID = ?", Integer.class, schoolYearID);
        return count == null ? 0 : count;
    }

    public int getLastID() {
        Integer last = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM TextShare", Integer.class);
        return last == null ? 0 : last;
    }

    public List<Row> getList(int start, int finish, int schoolyearID) {
        return executeQuery(
                "SELECT * FROM (" +
                        "  SELECT t.*, ROW_NUMBER() OVER (ORDER BY PostDate DESC) AS row_num" +
                        "  FROM TextShare t WHERE t.SchoolYearID = ?" +
                        ") AS numbered WHERE row_num BETWEEN ? AND ?",
                SqlParameter.in(1, Types.INTEGER, schoolyearID),
                SqlParameter.in(2, Types.INTEGER, start),
                SqlParameter.in(3, Types.INTEGER, finish));
    }

    public List<Row> getPostedUsers() {
        return executeQuery("SELECT DISTINCT UserID FROM TextShare");
    }
}