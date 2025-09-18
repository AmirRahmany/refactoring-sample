package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class City extends Base {

    public boolean add(String cityName, int provinceID) {
        Object lastIdOb = executeScalar(Types.INTEGER, "{ call city_GetLastID() }");
        int lastId = (lastIdOb instanceof Number) ? ((Number) lastIdOb).intValue() : 0;
        lastId++;
        return executeNonQuery(Types.NULL, "{ call city_AddCity(?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, lastId),
                SqlParameter.in(2, Types.VARCHAR, cityName),
                SqlParameter.in(3, Types.INTEGER, provinceID));
    }

    public boolean delete(int id) {
        return executeNonQuery(Types.NULL, "{ call city_DeleteCity(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public boolean update(int id, String cityName, int provinceID) {
        return executeNonQuery(Types.NULL, "{ call city_UpdateCity(?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, id),
                SqlParameter.in(2, Types.VARCHAR, cityName),
                SqlParameter.in(3, Types.INTEGER, provinceID));
    }

    public List<Row> getList(int provinceID) {
        return executeSelect(Types.NULL, "{ call city_GetListByProvinceID(?) }",
                SqlParameter.in(1, Types.INTEGER, provinceID));
    }

    public Row get(int id) {
        List<Row> rows = executeSelect(Types.NULL, "{ call city_GetCity(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
        return (rows == null || rows.isEmpty()) ? null : rows.get(0);
    }

    public int getCount(String cityName, int provinceID) {
        Object count = executeScalar(Types.INTEGER, "{ call city_GetCountInProvince(?,?) }",
                SqlParameter.in(1, Types.VARCHAR, cityName),
                SqlParameter.in(2, Types.INTEGER, provinceID));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public int getCount() {
        Object count = executeScalar(Types.INTEGER, "{ call city_GetCount() }");
        return count == null ? 0 : ((Number) count).intValue();
    }

    public int getCountByProvince(int provinceID) {
        Object count = executeScalar(Types.INTEGER, "{ call city_GetCountByProvince(?) }",
                SqlParameter.in(1, Types.INTEGER, provinceID));
        return count == null ? 0 : ((Number) count).intValue();
    }
}


