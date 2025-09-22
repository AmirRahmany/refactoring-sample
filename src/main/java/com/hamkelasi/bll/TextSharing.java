package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextSharing {
    private int id;
    private int userId;
    private int schoolYearId;
    private String text;
    private String url;
    private LocalDateTime postDate;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public boolean isValid() {
        boolean valid = true;
        Validation validation = new Validation();

        if (this.text == null || this.text.isEmpty()) {
            valid = false;
        }

        if (this.url != null && !this.url.isEmpty()) {
            if (!validation.urlValidator(this.url)) {
                valid = false;
            }
        }

        return valid;
    }

    public int add() {
        return add(this.userId, this.schoolYearId, this.text, this.url, this.postDate);
    }

    public int add(int userId, int schoolYearId, String text, String url, LocalDateTime postDate) {
        com.hamkelasi.dal.TextSharing newPost = new com.hamkelasi.dal.TextSharing();
        int lastId = getLastId();
        lastId++;
        boolean isInsert = newPost.insert(userId, schoolYearId, text, url, postDate);

        if (isInsert) {
            return 1; // ثبت داده با موفقیت انجام شد
        } else {
            return 9; // اشکال در ثبت داده
        }
    }

    private int getLastId() {
        com.hamkelasi.dal.TextSharing dalTextSharing = new com.hamkelasi.dal.TextSharing();
        return dalTextSharing.getLastID();
    }

    public static TextSharing[] getList(int startNumber, int finishNumber, int schoolYearId) {
        com.hamkelasi.dal.TextSharing dalTextSharing = new com.hamkelasi.dal.TextSharing();
        List<Base.Row> dt = dalTextSharing.getList(startNumber, finishNumber, schoolYearId);
        TextSharing[] posts = new TextSharing[dt.size()];

        for (int i = 0; i < dt.size(); i++) {
            posts[i] = new TextSharing();
            posts[i].id = Integer.parseInt(dt.get(i).get("ID").toString());
            posts[i].userId = Integer.parseInt(dt.get(i).get("userID").toString());
            posts[i].schoolYearId = Integer.parseInt(dt.get(i).get("schoolYearID").toString());
            posts[i].text = dt.get(i).get("postText").toString();
            posts[i].url = dt.get(i).get("url").toString();
            posts[i].postDate = LocalDateTime.parse(dt.get(i).get("postDate").toString());
        }

        return posts;
    }

    public static int getCount(int schoolYearId) {
        com.hamkelasi.dal.TextSharing dalTextSharing = new com.hamkelasi.dal.TextSharing();
        return dalTextSharing.getCount(schoolYearId);
    }

    public List<User> getPostedUsers() {
        com.hamkelasi.dal.TextSharing dalTextSharing = new com.hamkelasi.dal.TextSharing();
        List<Base.Row> dt = dalTextSharing.getPostedUsers();
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < dt.size(); i++) {
            int userId = Integer.parseInt(dt.get(i).get("userID").toString());
            User tempUser = new User(userId);
            userList.add(tempUser);
        }
        return userList;
    }
}