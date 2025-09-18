package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class Permission extends Base {
    public String getPermissionName(int id) {
        Object name = executeScalar(Types.VARCHAR, "{ call permission_GetName(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
        return name == null ? null : String.valueOf(name);
    }

    public List<Row> getList() {
        return executeSelect(Types.NULL, "{ call permission_GetList() }");
    }
}


