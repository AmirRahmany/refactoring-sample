package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class School {
    private int id;
    private String name;
    private int type;
    private int cityId;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public School() {
    }

    public School(int id) {
        com.hamkelasi.dal.School school = new com.hamkelasi.dal.School();
        // Assuming DAL returns a School object; adjust if DAL uses a different structure
        final List<Base.Row> dr = school.getSchool(id);

        this.id = id;
        this.name = dr.getFirst().get("schoolName").toString();
        this.type = Integer.parseInt(dr.getFirst().get("schoolType").toString());
        this.cityId = Integer.parseInt(dr.getFirst().get("cityId").toString());
    }

    public static School[] getList(int cityId, int schoolType) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        // Assuming DAL returns List<School>
        final List<Base.Row> dr = dalSchool.getList(cityId, schoolType);
        School[] schools = new School[dr.size()];

        for (int i = 0; i < schools.length; i++) {
            schools[i] = new School();
            schools[i].id = Integer.parseInt(dr.get(i).get("id").toString());
            schools[i].name = dr.get(i).get("schoolName").toString();
            schools[i].cityId = cityId;
            schools[i].type = schoolType;
        }

        return schools;
    }

    public static int getCount() {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        return dalSchool.getCount();
    }

    public int getCount(int provinceId) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        return dalSchool.getCountByProvince(provinceId);
    }

    public int getCount(int provinceId, int cityId) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        return dalSchool.getCount(provinceId, cityId);
    }

    public static List<School> getList(int cityId) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        final List<Base.Row> dt = dalSchool.getListByCity(cityId);
        List<School> schoolList = new ArrayList<>();

        for (int i = 0; i < dt.size(); i++) {
            int id = Integer.parseInt(dt.get(i).get("id").toString());
            School temp = new School(id);
            schoolList.add(temp);
        }
        return schoolList;
    }

    public int getUserCount() {
        return getUserCount(this.id);
    }

    private int getUserCount(int schoolId) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        return dalSchool.getUserCount(schoolId);
    }

    public int add(String schoolName, int schoolType, int cityId) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        int count = dalSchool.getCount(schoolName, cityId);

        if (count == 0) {
            boolean isAdded = dalSchool.add(schoolName, schoolType, cityId);

            if (isAdded) {
                return 0; // ثبت داده با موفقیت انجام گرفت
            } else {
                return 9; // اشکال در ثبت داده
            }
        } else {
            return 1; // مدرسه ای با این نام در شهر مربوط وجود دارد
        }
    }

    public int update(int schoolId, String schoolName, int schoolType, int cityId) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        int retVal = 0;

        boolean isUpdated = dalSchool.update(schoolId, schoolName, schoolType, cityId);

        if (!isUpdated) {
            retVal = 9; // اشکال در تغییر اطلاعات
        }

        return retVal;
    }

    public boolean isUpdateValid(String schoolName, int cityId) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        int count = dalSchool.getCount(schoolName, cityId);

        return count == 0;
    }

    public int getPostCount(int schoolId, Date startDate, Date endDate) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        return dalSchool.getPostCount(schoolId, startDate, endDate);
    }

    public int delete(int schoolId) {
        com.hamkelasi.dal.School dalSchool = new com.hamkelasi.dal.School();
        boolean isDeleted = dalSchool.delete(schoolId);

        if (isDeleted) {
            return 0;
        } else {
            return 9; // اشکال در حذف
        }
    }
}