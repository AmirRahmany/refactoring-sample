package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class MediaShare extends Base {
    public List<Row> getRecord(int id) {
        return executeSelect(Types.NULL, "{ call mediaShare_GetByID(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public List<Row> getListID(int type, int schoolyearID) {
        return executeSelect(Types.NULL, "{ call mediaShare_GetListByType(?,?) }",
                SqlParameter.in(1, Types.INTEGER, type),
                SqlParameter.in(2, Types.INTEGER, schoolyearID));
    }

    public int getCount(int type, int schoolyearID) {
        Object count = executeScalar(Types.INTEGER, "{ call mediaShare_GetCount(?,?) }",
                SqlParameter.in(1, Types.INTEGER, type),
                SqlParameter.in(2, Types.INTEGER, schoolyearID));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public boolean add(int userID, int schoolYearID, String subject, String description, String URL, String thumbURL,
                       java.util.Date postDate, int shareType) {
        int lastID = getLastID();
        return executeNonQuery(Types.NULL, "{ call mediaShare_Add(?,?,?,?,?,?,?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, lastID),
                SqlParameter.in(2, Types.INTEGER, userID),
                SqlParameter.in(3, Types.INTEGER, schoolYearID),
                SqlParameter.in(4, Types.VARCHAR, subject),
                SqlParameter.in(5, Types.VARCHAR, description),
                SqlParameter.in(6, Types.VARCHAR, URL),
                SqlParameter.in(7, Types.VARCHAR, thumbURL),
                SqlParameter.in(8, Types.TIMESTAMP, postDate),
                SqlParameter.in(9, Types.INTEGER, shareType));
    }

    private int getLastID() {
        Object last = executeScalar(Types.INTEGER, "{ call mediaShare_GetLastID() }");
        if (last == null) return 1;
        int id = (last instanceof Number) ? ((Number) last).intValue() : 1;
        return id + 1;
    }
}


