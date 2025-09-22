package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;

import java.util.List;

public class SchoolRegistration {
    private int id;
    private int userId;
    private int schoolYearId;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSchoolYearId() {
        return schoolYearId;
    }

    public void setSchoolYearId(int schoolYearId) {
        this.schoolYearId = schoolYearId;
    }

    public SchoolRegistration() {

    }

    public SchoolRegistration(int id, int userId, int schoolYearId) {
        this.id = id;
        this.userId = userId;
        this.schoolYearId = schoolYearId;
    }

    public int add() {
        com.hamkelasi.dal.SchoolRegistration registration = new com.hamkelasi.dal.SchoolRegistration();
        this.id = registration.getLastID();

        boolean retVal = registration.add(this.userId, this.schoolYearId);

        if (retVal) {
            return 1; // ثبت داده با موفقیت انجام گردید
        } else {
            return 9; // اشکال در ثبت داده
        }
    }

    public static User[] getUsers(int schoolYearId) {
        com.hamkelasi.dal.SchoolRegistration registration = new com.hamkelasi.dal.SchoolRegistration();
        List<Base.Row> dt = registration.getUsersID(schoolYearId);
        User[] users = new User[dt.size()];

        for (int i = 0; i < dt.size(); i++) {
            int userId = Integer.parseInt(dt.get(i).get("userID").toString());
            users[i] = new User(userId);
        }

        return users;
    }
}
