package com.hamkelasi.dal;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class User extends Base{

    public boolean add(String username, String password, String firstname, String lastname, String profilePicture,
                       String email, String website, int permission, LocalDateTime registerDate, boolean isPMActive) {
        return executeNonQueryPrepared(
                "INSERT INTO Userlist (Username, Password, Firstname, Lastname, ProfilePicture, Email, Website, Permission, RegisterDate, IsPmActivate) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                SqlParameter.in(1, Types.VARCHAR, username),
                SqlParameter.in(2, Types.VARCHAR, password),
                SqlParameter.in(3, Types.VARCHAR, firstname),
                SqlParameter.in(4, Types.VARCHAR, lastname),
                SqlParameter.in(5, Types.VARCHAR, profilePicture),
                SqlParameter.in(6, Types.VARCHAR, email),
                SqlParameter.in(7, Types.VARCHAR, website),
                SqlParameter.in(8, Types.INTEGER, permission),
                SqlParameter.in(9, Types.TIMESTAMP, registerDate),
                SqlParameter.in(10, Types.BOOLEAN, isPMActive));
    }

    public boolean delete(int id) {
        return executeNonQuery(Types.NULL,
                "DELETE FROM Userlist WHERE ID = ?",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public boolean delete(String username) {
        return executeNonQuery(Types.NULL,
                "DELETE FROM Userlist WHERE Username = ?",
                SqlParameter.in(1, Types.VARCHAR, username));
    }

    public boolean update(int id, String username, String password, String firstname, String lastname,
                          String profilePicture, String email, String website, int permission, boolean isPmActive) {
        return executeNonQuery(Types.NULL,
                "UPDATE Userlist SET Username = ?, Password = ?, Firstname = ?, Lastname = ?, ProfilePicture = ?, " +
                        "Email = ?, Website = ?, Permission = ?, IsPmActivate = ? WHERE ID = ?",
                SqlParameter.in(1, Types.VARCHAR, username),
                SqlParameter.in(2, Types.VARCHAR, password),
                SqlParameter.in(3, Types.VARCHAR, firstname),
                SqlParameter.in(4, Types.VARCHAR, lastname),
                SqlParameter.in(5, Types.VARCHAR, profilePicture),
                SqlParameter.in(6, Types.VARCHAR, email),
                SqlParameter.in(7, Types.VARCHAR, website),
                SqlParameter.in(8, Types.INTEGER, permission),
                SqlParameter.in(9, Types.BOOLEAN, isPmActive),
                SqlParameter.in(10, Types.INTEGER, id));
    }

    public int getUserCountByEmail(String email) {
        Integer count = (Integer) executeScalar(Types.INTEGER,
                "SELECT COUNT(*) FROM Userlist WHERE Email = ?",
                List.of(SqlParameter.in(1, Types.VARCHAR, email)));
        return count == null ? 0 : count;
    }

    public int getUserCountByUsername(String username) {
        Integer count = (Integer) executeScalar(Types.INTEGER,
                "SELECT COUNT(*) FROM Userlist WHERE Username = ?",
                List.of(SqlParameter.in(1, Types.VARCHAR, username)));
        return count == null ? 0 : count;
    }

    public int getCount() {
        Integer count = (Integer) executeScalar(Types.INTEGER, "SELECT COUNT(*) FROM Userlist", null);
        return count == null ? 0 : count;
    }

    public String getPassword(String username) {
        return  executeScalar(Types.VARCHAR,
                "SELECT Password FROM Userlist WHERE Username = ?",
                List.of(SqlParameter.in(1, Types.VARCHAR, username))).toString();
    }

    public int getID(String username) {
        Integer id = (Integer) executeScalar(Types.INTEGER,
                "SELECT ID FROM Userlist WHERE Username = ?",
                List.of(SqlParameter.in(1, Types.VARCHAR, username)));
        return id == null ? 0 : id;
    }

    public List<Row> getUser(String username) {
        return executeQuery(
                "SELECT * FROM Userlist WHERE Username = ?",
                SqlParameter.in(1, Types.VARCHAR, username));
    }

    public List<Row> getUser(int id) {
        return executeQuery(
                "SELECT * FROM Userlist WHERE ID = ?",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public List<Row> getUsersByRowRange(int start, int last) {
        return executeQuery(
                "SELECT * FROM (" +
                        "  SELECT u.*, ROW_NUMBER() OVER (ORDER BY ID) AS row_num " +
                        "  FROM Userlist u " +
                        ") AS numbered WHERE row_num BETWEEN ? AND ?",
                SqlParameter.in(1, Types.INTEGER, start),
                SqlParameter.in(2, Types.INTEGER, last));
    }

    public List<Row> getListID() {
        return executeQuery("SELECT ID FROM Userlist");
    }

    public List<Row> getOrderedListID() {
        return executeSelect(Types.NULL, "SELECT ID FROM Userlist ORDER BY ID");
    }

    public List<Row> getListByType(int typeId) {
        return executeSelect(Types.NULL,
                "SELECT * FROM Userlist WHERE Permission = ?",
                SqlParameter.in(1, Types.INTEGER, typeId));
    }

    public int getTextPostCount(int userID) {
        Integer count = (Integer) executeScalar(Types.INTEGER,
                "SELECT COUNT(*) FROM TextShare WHERE UserID = ?",
                List.of(SqlParameter.in(1, Types.INTEGER, userID)));
        return count == null ? 0 : count;
    }

    public int getTextPostCount(int userID, Date start, Date end) {
        Integer count = (Integer) executeScalar(Types.INTEGER,
                "SELECT COUNT(*) FROM TextShare WHERE UserID = ? AND PostDate BETWEEN ? AND ?",
                List.of(
                        SqlParameter.in(1, Types.INTEGER, userID),
                        SqlParameter.in(2, Types.TIMESTAMP, start),
                        SqlParameter.in(3, Types.TIMESTAMP, end)));
        return count == null ? 0 : count;
    }

    public List<Row> getRegisteredList(Date startDate, Date endDate) {
        return executeSelect(Types.NULL,
                "SELECT * FROM Userlist WHERE RegisterDate BETWEEN ? AND ?",
                SqlParameter.in(1, Types.TIMESTAMP, startDate),
                SqlParameter.in(2, Types.TIMESTAMP, endDate));
    }
}