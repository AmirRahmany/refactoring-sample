package com.hamkelasi.dal;

import java.sql.Types;
import java.util.List;

public class Friendship extends Base {
    public boolean add(int userID, int friendID, int friendshipStatus) {
        return executeNonQuery(Types.NULL, "{ call friendship_Add(?,?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, getLastID()),
                SqlParameter.in(2, Types.INTEGER, userID),
                SqlParameter.in(3, Types.INTEGER, friendID),
                SqlParameter.in(4, Types.INTEGER, friendshipStatus));
    }

    public boolean update(int userID, int friendID, int friendshipStatus) {
        return executeNonQuery(Types.NULL, "{ call friendship_update(?,?,?) }",
                SqlParameter.in(1, Types.INTEGER, userID),
                SqlParameter.in(2, Types.INTEGER, friendID),
                SqlParameter.in(3, Types.INTEGER, friendshipStatus));
    }

    public boolean delete(int userID, int friendID) {
        return executeNonQuery(Types.NULL, "{ call friendship_Delete(?,?) }",
                SqlParameter.in(1, Types.INTEGER, userID),
                SqlParameter.in(2, Types.INTEGER, friendID));
    }

    private int getLastID() {
        Object last = executeScalar(Types.INTEGER, "{ call friendship_GetLastID() }");
        if (last == null) return 1;
        int id = (last instanceof Number) ? ((Number) last).intValue() : 1;
        return id + 1;
    }

    public List<Row> getWaitingList(int userID) {
        return executeSelect(Types.NULL, "{ call friendship_GetWaitingList(?) }",
                SqlParameter.in(1, Types.INTEGER, userID));
    }

    public List<Row> getFriendList(int userID) {
        return executeSelect(Types.NULL, "{ call friendship_GetFriendList(?) }",
                SqlParameter.in(1, Types.INTEGER, userID));
    }

    public int getFriendshipStatus(int userID, int friendID) {
        Object obj = executeScalar(Types.INTEGER, "{ call friendship_GetFriendshipStatus(?,?) }",
                SqlParameter.in(1, Types.INTEGER, userID),
                SqlParameter.in(2, Types.INTEGER, friendID));
        return obj == null ? 0 : ((Number) obj).intValue();
    }
}


