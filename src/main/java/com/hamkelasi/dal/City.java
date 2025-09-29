package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class City extends Base {

    public boolean add(String cityName, int provinceID) {
        Object lastIdOb = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM city", Integer.class);
        int lastId = (lastIdOb instanceof Number) ? ((Number) lastIdOb).intValue() : 0;
        lastId++;
        return jdbcTemplate.update("INSERT INTO city (ID, CityName, ProvinceID) VALUES (?, ?, ?)",
                lastId, cityName, provinceID) > 0;
    }

    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM city WHERE ID = ?", id) > 0;
    }

    public boolean update(int id, String cityName, int provinceID) {
        return jdbcTemplate.update("UPDATE city SET CityName = ?, ProvinceID = ? WHERE ID = ?",
                cityName, provinceID, id) > 0;
    }

    public List<Row> getList(int provinceID) {
        return executeQuery("SELECT * FROM city WHERE ProvinceID = ?",
                SqlParameter.in(1, Types.INTEGER, provinceID));
    }

    public Row get(int id) {
        List<Row> rows = executeSelect(Types.NULL, "SELECT * FROM city WHERE ID = ?",
                SqlParameter.in(1, Types.INTEGER, id));
        return (rows == null || rows.isEmpty()) ? null : rows.get(0);
    }

    public int getCount(String cityName, int provinceID) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM city WHERE CityName = ? AND ProvinceID = ?",
                new Object[]{cityName, provinceID}, new int[]{Types.VARCHAR, Types.INTEGER}, Integer.class);
        return count == null ? 0 : count;
    }

    public int getCount() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM city", Integer.class);
        return count == null ? 0 : count;
    }

    public int getCountByProvince(int provinceID) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM city WHERE ProvinceID = ?",
                new Object[]{provinceID}, new int[]{Types.INTEGER}, Integer.class);
        return count == null ? 0 : count;
    }
}