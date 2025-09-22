package com.hamkelasi.dal;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.List;

public class Permission extends Base {

    public String getPermissionName(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT UserType FROM Permission WHERE ID = ?",
                    new Object[]{id}, new int[]{Types.INTEGER}, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Row> getList() {
        return executeSelect(Types.NULL, "SELECT * FROM Permission", null);
    }
}