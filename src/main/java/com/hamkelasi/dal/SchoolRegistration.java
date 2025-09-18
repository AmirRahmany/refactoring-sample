package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class SchoolRegistration extends Base {
    public boolean add(int id, int userId, int schoolyearId) {
        return executeNonQuery(Types.NULL, "{ call schoolRegistration_Add(?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, id),
                SqlParameter.in(2, Types.INTEGER, userId),
                SqlParameter.in(3, Types.INTEGER, schoolyearId));
    }

    public int getLastID() {
        Object obj = executeScalar(Types.INTEGER, "{ call schoolRegistration_GetLastID() }");
        int last = (obj instanceof Number) ? ((Number) obj).intValue() : 0;
        return last + 1;
    }

    public List<Row> getUsersID(int schoolyearID) {
        return executeSelect(Types.NULL, "{ call schoolRegistration_GetUsersID(?) }",
                SqlParameter.in(1, Types.INTEGER, schoolyearID));
    }
}


