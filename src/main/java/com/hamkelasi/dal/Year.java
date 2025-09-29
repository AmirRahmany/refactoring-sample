package com.hamkelasi.dal;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.List;

public class Year extends Base {

    public List<Row> getList() {
        return executeQuery( "select * from academicyear", null);
    }

    public List<Row> getYear(int id) {
        return executeQuery( "select * from academicyear where ID=?",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public int getCount(String year) {
        Object count = executeScalar(Types.INTEGER, "select count(*) from academicyear where ID=?",
                List.of(SqlParameter.in(1, Types.VARCHAR, year)));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public boolean update(int id, String year) {
        return executeNonQuery(Types.NULL, "CALL year_Update(?,?)",
                SqlParameter.in(1, Types.INTEGER, id),
                SqlParameter.in(2, Types.VARCHAR, year));
    }

    public boolean add(String year) {
        int lastID = getLastID() + 1;
        return executeNonQuery(Types.NULL, "CALL year_Add(?,?)",
                SqlParameter.in(1, Types.INTEGER, lastID),
                SqlParameter.in(2, Types.VARCHAR, year));
    }

    private int getLastID() {
        Object last = executeScalar(Types.INTEGER, "CALL year_GetLastID()", null);
        return last == null ? 0 : ((Number) last).intValue();
    }

    public boolean delete(int yearID) {
        return executeNonQuery(Types.NULL, "CALL year_Delete(?)",
                SqlParameter.in(1, Types.INTEGER, yearID));
    }
}