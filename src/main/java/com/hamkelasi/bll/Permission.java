package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Permission {
    private int id;
    private String permissionName;


    

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionName(int id) {
        com.hamkelasi.dal.Permission permission = new com.hamkelasi.dal.Permission();
        return permission.getPermissionName(id);
    }

    public static List<Permission> getList() {
        com.hamkelasi.dal.Permission permission = new com.hamkelasi.dal.Permission();
        List<Base.Row> dt = permission.getList();
        List<Permission> permissionList = new ArrayList<>();

        for (int i = 0; i < dt.size(); i++) {
            Permission temp = new Permission();
            temp.id = Integer.parseInt(dt.get(i).get("id").toString());
            temp.permissionName = dt.get(i).get("userType").toString();
            permissionList.add(temp);
        }

        return permissionList;
    }
}