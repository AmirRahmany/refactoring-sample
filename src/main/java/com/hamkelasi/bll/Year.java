package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class Year {
    private int id;
    private String academicYear;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Year() {
    }

    public Year(int id) {
        com.hamkelasi.dal.Year dalYear = new com.hamkelasi.dal.Year();
        List<Base.Row> dt = dalYear.getYear(id);

        this.id = id;
        this.academicYear = dt.get(0).get("AcademicYear").toString();
    }

    public static Year[] getList() {
        com.hamkelasi.dal.Year dalYear = new com.hamkelasi.dal.Year();
        List<Base.Row> dt = dalYear.getList();
        Year[] years = new Year[dt.size()];

        for (int i = 0; i < years.length; i++) {
            years[i] = new Year();
            years[i].id = Integer.parseInt(dt.get(i).get("id").toString());
            years[i].academicYear = dt.get(i).get("academicYear").toString();
        }

        return years;
    }

    public int update(int id, String year) {
        com.hamkelasi.dal.Year dalYear = new com.hamkelasi.dal.Year();
        int count = dalYear.getCount(year);

        if (count == 0) {
            boolean isUpdate = dalYear.update(id, year);

            if (isUpdate) {
                return 0; // تغییرات با موفقیت ذخیره گردید
            } else {
                return 9; // اشکال در ثبت تغییرات
            }
        } else {
            return 1; // سال تحصیلی تکراری می باشد
        }
    }

    public int add(String year) {
        com.hamkelasi.dal.Year dalYear = new com.hamkelasi.dal.Year();
        int count = dalYear.getCount(year);

        if (count == 0) {
            boolean isAdd = dalYear.add(year);

            if (isAdd) {
                return 0; // ثبت اطلاعات با موفقیت انجام گرفت
            } else {
                return 9; // اشکال در ثبت اطلاعات
            }
        } else {
            return 1; // نام سال تحصیلی تکراری است
        }
    }

    public int delete(int yearId) {
        com.hamkelasi.dal.Year dalYear = new com.hamkelasi.dal.Year();
        boolean isDelete = dalYear.delete(yearId);

        if (isDelete) {
            return 0; // سال تحصیلی با موفقیت حذف گردید
        } else {
            return 9; // اشکال در حذف سال تحصیلی
        }
    }
}
