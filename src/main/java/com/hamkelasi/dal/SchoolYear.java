package com.hamkelasi.dal;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.List;

public class SchoolYear extends Base {

    public List<Row> getSchoolYear(int schoolID, int yearID) {
        return executeSelect(Types.NULL, "SELECT * FROM SchoolYear WHERE SchoolID = ? AND YearID = ?",
                SqlParameter.in(1, Types.INTEGER, schoolID),
                SqlParameter.in(2, Types.INTEGER, yearID));
    }

    public List<Row> getSchoolYear(int id) {
        return executeSelect(Types.NULL, "SELECT * FROM SchoolYear WHERE ID = ?",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public boolean add(int id, int schoolId, int yearId) {
        return jdbcTemplate.update("INSERT INTO SchoolYear (ID, SchoolID, YearID) VALUES (?, ?, ?)",
                id, schoolId, yearId) > 0;
    }

    public int getLastID() {
        Integer last = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM SchoolYear", Integer.class);
        return (last == null) ? 1 : last + 1;
    }
}