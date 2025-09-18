package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class School extends Base {
    public List<Row> getList(int cityID, int schoolType) {
        return executeSelect(Types.NULL, "{ call school_GetList(?,?) }",
                SqlParameter.in(1, Types.INTEGER, cityID),
                SqlParameter.in(2, Types.INTEGER, schoolType));
    }

    public List<Row> getSchool(int id) {
        return executeSelect(Types.NULL, "{ call school_GetSchool(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public int getCount() {
        Object count = executeScalar(Types.INTEGER, "{ call school_GetCount() }");
        return count == null ? 0 : ((Number) count).intValue();
    }

    public int getCountByProvince(int provinceID) {
        Object count = executeScalar(Types.INTEGER, "{ call school_GetCountByProvince(?) }",
                SqlParameter.in(1, Types.INTEGER, provinceID));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public int getCount(int provinceID, int cityID) {
        Object count = executeScalar(Types.INTEGER, "{ call school_GetCountByProvinceAndCity(?,?) }",
                SqlParameter.in(1, Types.INTEGER, provinceID),
                SqlParameter.in(2, Types.INTEGER, cityID));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public List<Row> getListByCity(int cityID) {
        return executeSelect(Types.NULL, "{ call school_GetListByCity(?) }",
                SqlParameter.in(1, Types.INTEGER, cityID));
    }

    public int getUserCount(int schoolID) {
        Object count = executeScalar(Types.INTEGER, "{ call school_GetUserCount(?) }",
                SqlParameter.in(1, Types.INTEGER, schoolID));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public int getCount(String schoolName, int cityID) {
        Object count = executeScalar(Types.INTEGER, "{ call school_GetCountByName(?,?) }",
                SqlParameter.in(1, Types.VARCHAR, schoolName),
                SqlParameter.in(2, Types.INTEGER, cityID));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public boolean add(String schoolName, int schoolType, int cityID) {
        int lastID = getLastID() + 1;
        return executeNonQuery(Types.NULL, "{ call school_Add(?,?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, lastID),
                SqlParameter.in(2, Types.VARCHAR, schoolName),
                SqlParameter.in(3, Types.INTEGER, schoolType),
                SqlParameter.in(4, Types.INTEGER, cityID));
    }

    private int getLastID() {
        Object last = executeScalar(Types.INTEGER, "{ call school_GetLastID() }");
        return (last instanceof Number) ? ((Number) last).intValue() : 0;
    }

    public boolean update(int schoolID, String schoolname, int schooltype, int cityid) {
        return executeNonQuery(Types.NULL, "{ call school_Update(?,?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, schoolID),
                SqlParameter.in(2, Types.VARCHAR, schoolname),
                SqlParameter.in(3, Types.INTEGER, schooltype),
                SqlParameter.in(4, Types.INTEGER, cityid));
    }

    public int getPostCount(int schoolID, java.util.Date startdate, java.util.Date enddate) {
        Object count = executeScalar(Types.INTEGER, "{ call school_GetPostCount(?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, schoolID),
                SqlParameter.in(2, Types.TIMESTAMP, startdate),
                SqlParameter.in(3, Types.TIMESTAMP, enddate));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public boolean delete(int schoolID) {
        return executeNonQuery(Types.NULL, "{ call school_Delete(?) }",
                SqlParameter.in(1, Types.INTEGER, schoolID));
    }
}


