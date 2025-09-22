package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public  class SchoolType {
    private int id;
    private String typeName;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public SchoolType() {
    }

    public SchoolType(int id) {
        com.hamkelasi.dal.SchoolType dalSchoolType = new com.hamkelasi.dal.SchoolType();
        List<Base.Row> dt = dalSchoolType.getSchoolType(id);

        this.id = id;
        this.typeName = (String) dt.get(0).get("Type");
    }

    public static SchoolType[] getList() {
        com.hamkelasi.dal.SchoolType dalSchoolType = new com.hamkelasi.dal.SchoolType();
        List<Base.Row> dt = dalSchoolType.getList();
        SchoolType[] types = new SchoolType[dt.size()];

        for (int i = 0; i < types.length; i++) {
            types[i] = new SchoolType();
            types[i].id = (Integer) dt.get(i).get("id");
            types[i].typeName = (String) dt.get(i).get("Type");
        }

        return types;
    }
}