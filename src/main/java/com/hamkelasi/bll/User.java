package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    //regionClassProperties
    private int id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String profilePicture;
    private String email;
    private String website;
    private int permission;
    private LocalDateTime registerDate;
    private boolean isPmActivate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isPmActivate() {
        return isPmActivate;
    }

    public void setPmActivate(boolean pmActivate) {
        isPmActivate = pmActivate;
    }
    //endregion

    public User() {
    }

    public User(int id) {
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        // Assuming DAL returns a populated User object; adjust if DAL uses ResultSet or Map
        final List<Base.Row> dt = user.getUser(id);
        this.id = id;
        this.username = dt.getFirst().get("username").toString();
        this.password = dt.getFirst().get("password").toString();
        this.firstname = dt.getFirst().get("firstName").toString();
        this.lastname = dt.getFirst().get("lastName").toString();
        this.profilePicture = dt.getFirst().get("profilePicture").toString();
        this.permission = Integer.parseInt(dt.getFirst().get("permission").toString());
        this.email = dt.getFirst().get("email").toString();
        this.website = dt.getFirst().get("website").toString();
        this.registerDate = LocalDateTime.parse(dt.getFirst().get("registerDate").toString());
        this.isPmActivate = Boolean.parseBoolean(dt.getFirst().get("isPmActivate").toString());
    }

    public int add(String username, String password, String firstname, String lastname, String profilePicture, String email, String website, int permission, LocalDateTime registerDate, boolean isPmActivate) {
        final com.hamkelasi.dal.User newUser = new com.hamkelasi.dal.User();
        int retValue = 0;

        if (profilePicture == null || profilePicture.equals("")) {
            profilePicture = "/UserImages/default.png";
        }

        boolean inserted = newUser.add(username, password, firstname, lastname, profilePicture, email, website, permission, registerDate, isPmActivate);

        if (!inserted) {
            retValue = 9; //اشکال در ثبت داده
        }

        if (retValue == 0) {
            this.id = newUser.getID(username);
        }

        return retValue;
    }

    public int add() {
        return add(this.username, this.password, this.firstname, this.lastname, this.profilePicture, this.email, this.website, this.permission, this.registerDate, this.isPmActivate);
    }

    public int delete(int id) {
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        boolean isDeleted = user.delete(id);

        if (isDeleted) {
            return 0;
        } else {
            return 1; //اشکال در پاک کردن کاربر
        }
    }

    public int update(int id, String username, String password, String firstname, String lastname, String profilePicture, String email, String website, int permission, boolean isPmActive) {
        Validation validator = new Validation();
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        int retValue = 0;

        boolean updated = user.update(id, username, password, firstname, lastname, profilePicture, email, website, permission, isPmActive);

        if (!updated) {
            retValue = 9; //اشکال در ثبت داده
        }

        return retValue;
    }

    public static int getCount() {
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        return user.getCount();
    }

    public static User[] getUserByRowRange(int start, int last) {
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        int count = (last - start) + 1;
        User[] users = new User[count];

        // Assuming DAL returns List<User> with partial data; adjust if needed
        final List<Base.Row> retTable = user.getUsersByRowRange(start, last);
        for (int i = 0; i < count; i++) {
            users[i] = new User();
            users[i].id = Integer.parseInt(retTable.get(i).get("id").toString());
            users[i].username = retTable.get(i).get("username").toString();
            users[i].firstname = retTable.get(i).get("firstName").toString();
            users[i].lastname = retTable.get(i).get("lastName").toString();
            users[i].profilePicture = retTable.get(i).get("profilePicture").toString();
            users[i].permission = Integer.parseInt(retTable.get(i).get("permission").toString());
        }

        return users;
    }

    public List<User> getList() {
        List<User> userList = new ArrayList<User>();

        final List<Base.Row> dt = new com.hamkelasi.dal.User().getListID();// Assuming returns List<Integer>

        for (int i = 0; i < dt.size(); i++) {
            int id = Integer.parseInt(dt.get(i).get("id").toString());
            User tempUser = new User(id);
            userList.add(tempUser);
        }

        return userList;
    }

    public int login(String username, String password) {
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        int count = user.getUserCountByUsername(username);

        if (count == 0) {
            return 1; // کاربری با نام کاربری وارد شده وجود ندارد
        } else {
            if (password.equals(user.getPassword(username))) {
                // Assuming getUser returns populated User
                final List<Base.Row> dt = user.getUser(username);

                this.id = Integer.parseInt(dt.getFirst().get(0).toString());
                this.username = dt.getFirst().get(1).toString();
                this.password = dt.getFirst().get(2).toString();
                this.firstname = dt.getFirst().get(3).toString();
                this.lastname = dt.getFirst().get(4).toString();
                this.profilePicture = dt.getFirst().get(5).toString();
                this.email = dt.getFirst().get(6).toString();
                this.website = dt.getFirst().get(7).toString();
                this.permission = Integer.parseInt(dt.getFirst().get(8).toString());
                this.registerDate = LocalDateTime.parse(dt.getFirst().get("registerDate").toString());
                this.isPmActivate = Boolean.parseBoolean(dt.getFirst().get("isPmActivate").toString());

                return 0; //یوزرنیم و پسورد درست است
            } else {
                return 2; //پسورد اشتباه است
            }
        }
    }

    public int isValid(String username, String email, String website) {
        Validation validator = new Validation();
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        int retValue = 0;

        if (user.getUserCountByEmail(email) != 0) {
            retValue = 1; //ایمیل تکراری است
        }

        if (retValue == 0 && user.getUserCountByUsername(username) != 0) {
            retValue = 2; //نام کاربری تکراری است
        }

        if (retValue == 0 && !validator.emailValidator(email)) {
            retValue = 3; //فرمت ایمیل اشتباه است
        }

        if (retValue == 0) {
            if (website != null && !website.equals("")) {
                if (!validator.urlValidator(website)) {
                    retValue = 4; //فرمت وب سایت اشتباه است
                }
            }
        }

        return retValue;
    }

    public int isUpdateValid(String oldUsername, String newUsername, String oldEmail, String newEmail, String website) {
        Validation validator = new Validation();
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        int retValue = 0;

        if (!oldEmail.equals(newEmail)) {
            if (user.getUserCountByEmail(newEmail) != 0) {
                retValue = 1; //ایمیل تکراری است
            }
        }

        if (!oldUsername.equals(newUsername)) {
            if (retValue == 0 && user.getUserCountByUsername(newUsername) != 0) {
                retValue = 2; //نام کاربری تکراری است
            }
        }

        if (retValue == 0 && validator.emailValidator(newEmail) == false) {
            retValue = 3; //فرمت ایمیل اشتباه است
        }

        if (retValue == 0) {
            if (!website.equals("")) {
                if (validator.urlValidator(website) == false) {
                    retValue = 4; //فرمت وب سایت اشتباه است
                }
            }
        }

        return retValue;
    }

    public List<User> getOrderedList() {
        List<User> userList = new ArrayList<User>();

        final List<Base.Row> dt = new com.hamkelasi.dal.User().getOrderedListID();// Assuming returns List<Integer>

        for (int i = 0; i < dt.size(); i++) {
            int id = Integer.parseInt(dt.get(i).get("id").toString());
            User tempUser = new User(id);
            userList.add(tempUser);
        }

        return userList;
    }

    public List<User> getListByType(int typeId) {
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        // Assuming returns List<User> directly
        final List<Base.Row> dt = user.getListByType(typeId);
        List<User> userlist = new ArrayList<>();
        for (int i = 0; i < dt.size(); i++)
        {
            int tempid = Integer.parseInt(dt.get(i).get("id").toString());
            User temp = new User(tempid);
            userlist.add(temp);
        }
        return userlist;
    }

    public int getPostCount(int userID) {
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        int count = user.getTextPostCount(userID);
        return count;
    }

    public int getPostCount(int userID, Date start, Date end) {
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        int count = user.getTextPostCount(userID, start, end);
        return count;
    }

    public List<User> getRegisteredList(Date startDate, Date endDate) {
        final com.hamkelasi.dal.User user = new com.hamkelasi.dal.User();
        // Assuming returns List<Integer> IDs
        final List<Base.Row> dt = user.getRegisteredList(startDate, endDate);
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < dt.size(); i++) {
            int userId = Integer.parseInt(dt.get(i).get("id").toString());
            User tempUser = new User(userId);
            userList.add(tempUser);
        }
        return userList;
    }
}