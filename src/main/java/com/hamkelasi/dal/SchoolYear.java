package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class SchoolYear extends Base {
    public List<Row> getSchoolYear(int schoolID, int yearID) {
        return executeSelect(Types.NULL, "{ call schoolYear_Get(?,?) }",
                SqlParameter.in(1, Types.INTEGER, schoolID),
                SqlParameter.in(2, Types.INTEGER, yearID));
    }

    public List<Row> getSchoolYear(int id) {
        return executeSelect(Types.NULL, "{ call schoolYear_GetByID(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public boolean add(int id, int schoolId, int yearId) {
        return executeNonQuery(Types.NULL, "{ call schoolYear_Add(?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, id),
                SqlParameter.in(2, Types.INTEGER, schoolId),
                SqlParameter.in(3, Types.INTEGER, yearId));
    }

    public int getLastID() {
        Object obj = executeScalar(Types.INTEGER, "{ call schoolYear_GetLastID() }");
        int last = (obj instanceof Number) ? ((Number) obj).intValue() : 0;
        return last + 1;
    }
}


