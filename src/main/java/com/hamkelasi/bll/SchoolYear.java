package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class SchoolYear {
    private int id;
    private int schoolId;
    private int yearId;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getYearId() {
        return yearId;
    }

    public void setYearId(int yearId) {
        this.yearId = yearId;
    }

    public SchoolYear() {
    }

    public SchoolYear(int id) {
        com.hamkelasi.dal.SchoolYear dalSchoolYear = new com.hamkelasi.dal.SchoolYear();
        List<Base.Row> dt = dalSchoolYear.getSchoolYear(id);

        if (!dt.isEmpty()) {
            this.id = id;
            this.schoolId = Integer.parseInt(dt.getFirst().get("schoolID").toString());
            this.yearId = Integer.parseInt(dt.getFirst().get("yearID").toString());
        }
    }

    public SchoolYear(int schoolId, int yearId) {
        com.hamkelasi.dal.SchoolYear dalSchoolYear = new com.hamkelasi.dal.SchoolYear();
        List<Base.Row> dt = dalSchoolYear.getSchoolYear(schoolId, yearId);

        this.schoolId = schoolId;
        this.yearId = yearId;

        if (!dt.isEmpty()) {
            this.id = Integer.parseInt(dt.getFirst().get("ID").toString());
        }
    }

    public int add() {
        com.hamkelasi.dal.SchoolYear schoolYear = new com.hamkelasi.dal.SchoolYear();
        this.id = schoolYear.getLastID();
        boolean retVal = schoolYear.add(this.id, this.schoolId, this.yearId);

        if (retVal) {
            return 1; // ثبت داده با موفقیت انجام شد
        } else {
            return 9; // اشکال در ثبت داده
        }
    }
}