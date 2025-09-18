package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class SchoolType extends Base {
    public List<Row> getList() {
        return executeSelect(Types.NULL, "{ call schoolType_GetList() }");
    }

    public List<Row> getSchoolType(int id) {
        return executeSelect(Types.NULL, "{ call schoolType_Get(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }
}


