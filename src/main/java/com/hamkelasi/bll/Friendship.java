package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Enum equivalent to C# FriendShipStatus
public class Friendship {
    public void add(int userId, int friendId, FriendShipStatus friendshipStatus) {
        com.hamkelasi.dal.Friendship friendship = new com.hamkelasi.dal.Friendship();
        int status = friendshipStatus.getValue();
        friendship.add(userId, friendId, status);
    }

    public void confirmFriendship(int userId, int friendId) {
        com.hamkelasi.dal.Friendship friendship = new com.hamkelasi.dal.Friendship();
        friendship.update(userId, friendId, FriendShipStatus.FRIEND.getValue());
        friendship.update(friendId, userId, FriendShipStatus.FRIEND.getValue());
    }

    public List<User> getWaitingUsers(int userId) {
        com.hamkelasi.dal.Friendship friendship = new com.hamkelasi.dal.Friendship();
        List<Base.Row> dt = friendship.getWaitingList(userId);
        List<User> users = new ArrayList<>();

        for (int i = 0; i < dt.size(); i++) {
            int id = Integer.parseInt(dt.get(i).get("userID").toString());
            User tempUser = new User(id);
            users.add(tempUser);
        }
        return users;
    }

    public User[] getFriendList(int userId) {
        com.hamkelasi.dal.Friendship friendship = new com.hamkelasi.dal.Friendship();
        List<Base.Row> dt = friendship.getFriendList(userId);
        User[] users = new User[dt.size()];

        for (int i = 0; i < dt.size(); i++) {
            users[i] = new User(Integer.parseInt(dt.get(i).get("friendId").toString()));
        }

        return users;
    }

    public FriendShipStatus getFriendshipStatus(int userId, int friendId) {
        com.hamkelasi.dal.Friendship friendship = new com.hamkelasi.dal.Friendship();
        int sts = friendship.getFriendshipStatus(userId, friendId);

        if (sts == 0) {
            return FriendShipStatus.NOT_FRIEND;
        } else if (sts == 1) {
            return FriendShipStatus.FRIEND;
        } else if (sts == 2) {
            return FriendShipStatus.WAITING_FOR_RESPONSE;
        } else {
            return FriendShipStatus.WAITING_FOR_CONFIRM;
        }
    }

    public int delete(int userId, int friendId) {
        com.hamkelasi.dal.Friendship dalFriendship = new com.hamkelasi.dal.Friendship();
        boolean isDelete = dalFriendship.delete(userId, friendId);
        boolean isSecDelete = dalFriendship.delete(friendId, userId);

        if (isDelete && isSecDelete) {
            return 0; // دوستی با موفقیت حذف شد
        } else {
            return 9; // اشکال در عملیات
        }
    }

    public enum FriendShipStatus {
        NOT_FRIEND(0),
        FRIEND(1),
        WAITING_FOR_RESPONSE(2),
        WAITING_FOR_CONFIRM(3);

        private final int value;

        FriendShipStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        // Convert integer to enum
        public static FriendShipStatus fromValue(int value) {
            for (FriendShipStatus status : FriendShipStatus.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid FriendShipStatus value: " + value);
        }
    }
}