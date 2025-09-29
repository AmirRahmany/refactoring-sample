package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class SchoolType extends Base {

    public List<Row> getList() {
        return executeQuery("SELECT * FROM SchoolType");
    }

    public List<Row> getSchoolType(int id) {
        return executeQuery("SELECT * FROM SchoolType WHERE ID = ?",
                SqlParameter.in(1, Types.INTEGER, id));
    }
}