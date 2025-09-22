package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class SchoolRegistration extends Base {

    public boolean add(int userId, int schoolYearId) {
        return jdbcTemplate.update("INSERT INTO SchoolRegistration (UserID, SchoolYearID) VALUES (?, ?)",
                userId, schoolYearId) > 0;
    }

    public int getLastID() {
        Integer last = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM SchoolRegistration", Integer.class);
        return (last == null) ? 1 : last + 1;
    }

    public List<Row> getUsersID(int schoolYearID) {
        return executeSelect(Types.NULL, "SELECT UserID FROM SchoolRegistration WHERE SchoolYearID = ?",
                SqlParameter.in(1, Types.INTEGER, schoolYearID));
    }
}