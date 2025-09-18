package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class Province extends Base {
    public boolean add(String provinceName) {
        Object last = executeScalar(Types.INTEGER, "{ call province_GetLastID() }");
        int lastId = (last instanceof Number) ? ((Number) last).intValue() : 0;
        lastId++;
        return executeNonQuery(Types.NULL, "{ call province_AddProvince(?,?) }",
                SqlParameter.in(1, Types.INTEGER, lastId),
                SqlParameter.in(2, Types.VARCHAR, provinceName));
    }

    public boolean update(int id, String provinceName) {
        return executeNonQuery(Types.NULL, "{ call province_UpdateProvince(?,?) }",
                SqlParameter.in(1, Types.INTEGER, id),
                SqlParameter.in(2, Types.VARCHAR, provinceName));
    }

    public boolean delete(int id) {
        return executeNonQuery(Types.NULL, "{ call province_DeleteProvince(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public List<Row> getList() {
        return executeSelect(Types.NULL, "{ call province_GetList() }");
    }

    public int getCount() {
        Object count = executeScalar(Types.INTEGER, "{ call province_GetCount() }");
        return count == null ? 0 : ((Number) count).intValue();
    }

    public int getCountByName(String provinceName) {
        Object count = executeScalar(Types.INTEGER, "{ call province_GetProvinceCountByName(?) }",
                SqlParameter.in(1, Types.VARCHAR, provinceName));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public List<Row> get(int id) {
        return executeSelect(Types.NULL, "{ call province_Get(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }
}


