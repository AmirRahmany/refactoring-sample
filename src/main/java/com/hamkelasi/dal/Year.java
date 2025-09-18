package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class Year extends Base {
    public List<Row> getList() {
        return executeSelect(Types.NULL, "{ call year_GetList() }");
    }

    public List<Row> getYear(int id) {
        return executeSelect(Types.NULL, "{ call year_GetYear(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public int getCount(String year) {
        Object count = executeScalar(Types.INTEGER, "{ call year_GetCountByName(?) }",
                SqlParameter.in(1, Types.VARCHAR, year));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public boolean update(int id, String year) {
        return executeNonQuery(Types.NULL, "{ call year_Update(?,?) }",
                SqlParameter.in(1, Types.INTEGER, id),
                SqlParameter.in(2, Types.VARCHAR, year));
    }

    public boolean add(String year) {
        int lastID = getLastID() + 1;
        return executeNonQuery(Types.NULL, "{ call year_Add(?,?) }",
                SqlParameter.in(1, Types.INTEGER, lastID),
                SqlParameter.in(2, Types.VARCHAR, year));
    }

    private int getLastID() {
        Object last = executeScalar(Types.INTEGER, "{ call year_GetLastID() }");
        return (last instanceof Number) ? ((Number) last).intValue() : 0;
    }

    public boolean delete(int yearID) {
        return executeNonQuery(Types.NULL, "{ call year_Delete(?) }",
                SqlParameter.in(1, Types.INTEGER, yearID));
    }
}


