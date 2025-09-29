package com.hamkelasi.dal;


import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

public class School extends Base {

    public List<Row> getList(int cityID, int schoolType) {
        return executeSelect(Types.NULL, "SELECT * FROM School WHERE CityID = ? AND SchoolType = ?",
                SqlParameter.in(1, Types.INTEGER, cityID),
                SqlParameter.in(2, Types.INTEGER, schoolType));
    }

    public List<Row> getSchool(int id) {
        return executeQuery("SELECT * FROM School WHERE ID = ?",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public int getCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM School", Integer.class);
    }

    public int getCountByProvince(int provinceID) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM School s JOIN City c ON s.CityID = c.ID WHERE c.ProvinceID = ?",
                new Object[]{provinceID}, new int[]{Types.INTEGER}, Integer.class);
    }

    public int getCount(int provinceID, int cityID) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM School s JOIN City c ON s.CityID = c.ID WHERE c.ProvinceID = ? AND s.CityID = ?",
                new Object[]{provinceID, cityID}, new int[]{Types.INTEGER, Types.INTEGER}, Integer.class);
    }

    public List<Row> getListByCity(int cityID) {
        return executeQuery("SELECT * FROM School WHERE CityID = ?",
                SqlParameter.in(1, Types.INTEGER, cityID));
    }

    public int getUserCount(int schoolID) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM SchoolRegistration sr JOIN SchoolYear sy ON sr.SchoolYearID = sy.ID WHERE sy.SchoolID = ?",
                new Object[]{schoolID}, new int[]{Types.INTEGER}, Integer.class);
    }

    public int getCount(String schoolName, int cityID) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM School WHERE SchoolName = ? AND CityID = ?",
                new Object[]{schoolName, cityID}, new int[]{Types.VARCHAR, Types.INTEGER}, Integer.class);
    }

    public boolean add(String schoolName, int schoolType, int cityID) {
        Integer lastID = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM School", Integer.class);
        int id = (lastID == null) ? 1 : lastID + 1;
        return jdbcTemplate.update("INSERT INTO School (ID, SchoolName, SchoolType, CityID) VALUES (?, ?, ?, ?)",
                id, schoolName, schoolType, cityID) > 0;
    }

    public boolean update(int schoolID, String schoolName, int schoolType, int cityID) {
        return jdbcTemplate.update("UPDATE School SET SchoolName = ?, SchoolType = ?, CityID = ? WHERE ID = ?",
                schoolName, schoolType, cityID, schoolID) > 0;
    }

    public int getPostCount(int schoolID, java.util.Date startDate, java.util.Date endDate) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TextShare ts JOIN SchoolYear sy ON ts.SchoolYearID = sy.ID WHERE sy.SchoolID = ? AND ts.PostDate BETWEEN ? AND ?",
                new Object[]{schoolID, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime())},
                new int[]{Types.INTEGER, Types.TIMESTAMP, Types.TIMESTAMP}, Integer.class);
    }

    public boolean delete(int schoolID) {
        return jdbcTemplate.update("DELETE FROM School WHERE ID = ?",
                schoolID) > 0;
    }
}