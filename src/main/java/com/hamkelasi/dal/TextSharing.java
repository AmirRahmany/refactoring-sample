package com.hamkelasi.dal;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

public class TextSharing extends Base {
    public boolean insert(int id, int userid, int schoolyearid, String postText, String url, LocalDateTime date) {
        return executeNonQuery(Types.NULL, "{ call textSharing_Add(?,?,?,?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, id),
                SqlParameter.in(2, Types.INTEGER, userid),
                SqlParameter.in(3, Types.INTEGER, schoolyearid),
                SqlParameter.in(4, Types.VARCHAR, postText),
                SqlParameter.in(5, Types.VARCHAR, url),
                SqlParameter.in(6, Types.TIMESTAMP, date));
    }

    public int getCount(int schoolYearID) {
        Object count = executeScalar(Types.INTEGER, "{ call textSharing_GetCount(?) }",
                SqlParameter.in(1, Types.INTEGER, schoolYearID));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public int getLastID() {
        Object obj = executeScalar(Types.INTEGER, "{ call textSharing_GetLastID() }");
        if (obj == null) return 1;
        int last = (obj instanceof Number) ? ((Number) obj).intValue() : 1;
        return last;
    }

    public List<Row> getList(int start, int finish, int schoolyearID) {
        return executeSelect(Types.NULL, "{ call textShare_GetRecordByRowNumber(?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, start),
                SqlParameter.in(2, Types.INTEGER, finish),
                SqlParameter.in(3, Types.INTEGER, schoolyearID));
    }

    public List<Row> getPostedUsers() {
        return executeSelect(Types.NULL, "{ call textShare_GetPostedUsers() }");
    }
}


