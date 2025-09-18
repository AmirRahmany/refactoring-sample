package com.hamkelasi.dal;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class User extends Base {
    public boolean add(String username, String password, String firstname, String lastname, String profilePicture,
                       String email, String website, int permission, LocalDateTime registerDate, boolean isPMActive) {
        Object lastIdOb = executeScalar(Types.INTEGER, "{ call user_GetLastID() }");
        int lastId = (lastIdOb instanceof Number) ? ((Number) lastIdOb).intValue() : 0;
        lastId++;
        return executeNonQuery(Types.NULL, "{ call user_adduser(?,?,?,?,?,?,?,?,?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, lastId),
                SqlParameter.in(2, Types.VARCHAR, username),
                SqlParameter.in(3, Types.VARCHAR, password),
                SqlParameter.in(4, Types.VARCHAR, firstname),
                SqlParameter.in(5, Types.VARCHAR, lastname),
                SqlParameter.in(6, Types.VARCHAR, profilePicture),
                SqlParameter.in(7, Types.VARCHAR, email),
                SqlParameter.in(8, Types.VARCHAR, website),
                SqlParameter.in(9, Types.INTEGER, permission),
                SqlParameter.in(10, Types.TIMESTAMP, registerDate),
                SqlParameter.in(11, Types.BIT, isPMActive));
    }

    public boolean delete(int id) {
        return executeNonQuery(Types.NULL, "{ call user_DeleteUser(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public boolean delete(String username) {
        return executeNonQuery(Types.NULL, "{ call user_DeleteUserByUsername(?) }",
                SqlParameter.in(1, Types.VARCHAR, username));
    }

    public boolean update(int id, String username, String password, String firstname, String lastname,
                          String profilePicture, String email, String website, int permission, boolean isPmActive) {
        return executeNonQuery(Types.NULL, "{ call user_UpdateUser(?,?,?,?,?,?,?,?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, id),
                SqlParameter.in(2, Types.VARCHAR, username),
                SqlParameter.in(3, Types.VARCHAR, password),
                SqlParameter.in(4, Types.VARCHAR, firstname),
                SqlParameter.in(5, Types.VARCHAR, lastname),
                SqlParameter.in(6, Types.VARCHAR, profilePicture),
                SqlParameter.in(7, Types.VARCHAR, email),
                SqlParameter.in(8, Types.VARCHAR, website),
                SqlParameter.in(9, Types.INTEGER, permission),
                SqlParameter.in(10, Types.BIT, isPmActive));
    }

    public int getUserCountByEmail(String email) {
        Object value = executeScalar(Types.INTEGER, "{ call user_GetUserCountByEmail(?) }",
                SqlParameter.in(1, Types.VARCHAR, email));
        return value == null ? 0 : ((Number) value).intValue();
    }

    public int getUserCountByUsername(String username) {
        Object value = executeScalar(Types.INTEGER, "{ call user_GetUserCountByUsername(?) }",
                SqlParameter.in(1, Types.VARCHAR, username));
        return value == null ? 0 : ((Number) value).intValue();
    }

    public int getCount() {
        Object count = executeScalar(Types.INTEGER, "{ call user_GetCount() }");
        return count == null ? 0 : ((Number) count).intValue();
    }

    public String getPassword(String username) {
        Object pwd = executeScalar(Types.VARCHAR, "{ call user_GetPasswordByUsername(?) }",
                SqlParameter.in(1, Types.VARCHAR, username));
        return pwd == null ? null : String.valueOf(pwd);
    }

    public int getID(String username) {
        Object id = executeScalar(Types.INTEGER, "{ call user_GetIdByUsername(?) }",
                SqlParameter.in(1, Types.VARCHAR, username));
        return id == null ? 0 : ((Number) id).intValue();
    }

    public List<Row> getUser(String username) {
        return executeSelect(Types.NULL, "{ call user_GetUserByUsername(?) }",
                SqlParameter.in(1, Types.VARCHAR, username));
    }

    public List<Row> getUser(int id) {
        return executeSelect(Types.NULL, "{ call user_GetUserByID(?) }",
                SqlParameter.in(1, Types.INTEGER, id));
    }

    public List<Row> getUsersByRowRange(int start, int last) {
        return executeSelect(Types.NULL, "{ call user_GetUserByRowNumber(?,?) }",
                SqlParameter.in(1, Types.INTEGER, start),
                SqlParameter.in(2, Types.INTEGER, last));
    }

    public List<Row> getListID() {
        return executeSelect(Types.NULL, "{ call user_GetListID() }");
    }

    public List<Row> getOrderedListID() {
        return executeSelect(Types.NULL, "{ call user_GetOrderedListID() }");
    }

    public List<Row> getListByType(int typeId) {
        return executeSelect(Types.NULL, "{ call User_GetListByType(?) }",
                SqlParameter.in(1, Types.INTEGER, typeId));
    }

    public int getTextPostCount(int userID) {
        Object count = executeScalar(Types.INTEGER, "{ call textSharing_GetUserPostCount(?) }",
                SqlParameter.in(1, Types.INTEGER, userID));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public int getTextPostCount(int userID, Date start, Date end) {
        Object count = executeScalar(Types.INTEGER, "{ call textSharing_GetUserPostCountByDate(?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, userID),
                SqlParameter.in(2, Types.TIMESTAMP, start),
                SqlParameter.in(3, Types.TIMESTAMP, end));
        return count == null ? 0 : ((Number) count).intValue();
    }

    public List<Row> getRegisteredList(Date startDate, Date endDate) {
        return executeSelect(Types.NULL, "{ call user_GetRegisteredListByDate(?,?) }",
                SqlParameter.in(1, Types.TIMESTAMP, startDate),
                SqlParameter.in(2, Types.TIMESTAMP, endDate));
    }
}


