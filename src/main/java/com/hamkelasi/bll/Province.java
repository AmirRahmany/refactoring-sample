package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class Province {
    private int id;
    private String name;


    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province() {
    }

    public Province(int id) {
        com.hamkelasi.dal.Province dalProvince = new com.hamkelasi.dal.Province();
        List<Base.Row> dt = dalProvince.get(id);

        if (!dt.isEmpty()) {
            this.id = id;
            this.name = dt.getFirst().get("provinceName").toString();
        }
    }

    public int add(String provinceName) {
        com.hamkelasi.dal.Province province = new com.hamkelasi.dal.Province();
        int count = province.getCountByName(provinceName);
        int retVal = 0;
        boolean isInserted = false;

        if (count > 0) {
            retVal = 1; // نام استان تکراری است
            return retVal;
        }

        if (retVal == 0) {
            isInserted = province.add(provinceName);
        }

        if (!isInserted) {
            retVal = 9; // اشکال در ثبت داده
        }
        return retVal;
    }

    public int update(int id, String provinceName) {
        com.hamkelasi.dal.Province province = new com.hamkelasi.dal.Province();
        int count = province.getCountByName(provinceName);
        int retVal = 0;
        boolean isInserted = false;

        if (count > 0) {
            retVal = 1; // نام استان تکراری است
            return retVal;
        }

        if (retVal == 0) {
            isInserted = province.update(id, provinceName);
        }

        if (!isInserted) {
            retVal = 9; // اشکال در عملیات
        }
        return retVal;
    }

    public int delete(int id) {
        com.hamkelasi.dal.Province province = new com.hamkelasi.dal.Province();
        boolean isDeleted = province.delete(id);

        if (isDeleted) {
            return 0;
        } else {
            return 9; // اشکال در حذف استان
        }
    }

    public static Province[] getList() {
        com.hamkelasi.dal.Province province = new com.hamkelasi.dal.Province();
        List<Base.Row> dt = province.getList();
        Province[] provinceList = new Province[dt.size()];

        for (int i = 0; i < dt.size(); i++) {
            provinceList[i] = new Province();
            provinceList[i].id = Integer.parseInt(dt.get(i).get("id").toString());
            provinceList[i].name = dt.get(i).get("provinceName").toString();
        }

        return provinceList;
    }

    public static int getCount() {
        com.hamkelasi.dal.Province province = new com.hamkelasi.dal.Province();
        return province.getCount();
    }
}