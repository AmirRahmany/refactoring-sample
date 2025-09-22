package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class Province extends Base {

    public boolean add(String provinceName) {
        Integer lastId = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM Province", Integer.class);
        int id = (lastId == null) ? 1 : lastId + 1;
        return jdbcTemplate.update("INSERT INTO Province (ID, ProvinceName) VALUES (?, ?)",
                id, provinceName) > 0;
    }

    public boolean update(int id, String provinceName) {
        return jdbcTemplate.update("UPDATE Province SET ProvinceName = ? WHERE ID = ?",
                provinceName, id) > 0;
    }

    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM Province WHERE ID = ?",
                id) > 0;
    }

    public List<Row> getList() {
        return executeQuery("SELECT * FROM Province", null);
    }

    public int getCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Province", Integer.class);
    }

    public int getCountByName(String provinceName) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Province WHERE ProvinceName = ?",
                new Object[]{provinceName}, new int[]{Types.VARCHAR}, Integer.class);
    }

    public List<Row> get(int id) {
        return executeSelect(Types.NULL, "SELECT * FROM Province WHERE ID = ?",
                SqlParameter.in(1, Types.INTEGER, id));
    }
}