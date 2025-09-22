package com.hamkelasi.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Friendship extends Base {

    public boolean add(int userID, int friendID, int friendshipStatus) {
        Integer lastId = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM Friendship", Integer.class);
        int id = (lastId == null) ? 1 : lastId + 1;
        return jdbcTemplate.update("INSERT INTO Friendship (ID, UserID, FriendID, FriendshipStatus) VALUES (?, ?, ?, ?)",
                id, userID, friendID, friendshipStatus) > 0;
    }

    public boolean update(int userID, int friendID, int friendshipStatus) {
        return jdbcTemplate.update("UPDATE Friendship SET FriendshipStatus = ? WHERE UserID = ? AND FriendID = ?",
                friendshipStatus, userID, friendID) > 0;
    }

    public boolean delete(int userID, int friendID) {
        return jdbcTemplate.update("DELETE FROM Friendship WHERE UserID = ? AND FriendID = ?",
                userID, friendID) > 0;
    }

    private int getLastID() {
        Integer last = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM Friendship", Integer.class);
        return (last == null) ? 1 : last + 1;
    }

    public List<Row> getWaitingList(int userID) {
        return executeSelect(Types.NULL, "SELECT * FROM Friendship WHERE UserID = ? AND FriendshipStatus = 0",
                SqlParameter.in(1, Types.INTEGER, userID));
    }

    public List<Row> getFriendList(int userID) {
        return executeSelect(Types.NULL, "SELECT * FROM Friendship WHERE UserID = ? AND FriendshipStatus = 1",
                SqlParameter.in(1, Types.INTEGER, userID));
    }

    public int getFriendshipStatus(int userID, int friendID) {
        Integer status = jdbcTemplate.queryForObject("SELECT FriendshipStatus FROM Friendship WHERE UserID = ? AND FriendID = ?",
                new Object[]{userID, friendID}, new int[]{Types.INTEGER, Types.INTEGER}, Integer.class);
        return status == null ? 0 : status;
    }
}