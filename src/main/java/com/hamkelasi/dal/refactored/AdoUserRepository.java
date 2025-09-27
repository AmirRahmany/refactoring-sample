package com.hamkelasi.dal.refactored;

import com.hamkelasi.dal.User;

import java.time.LocalDateTime;

public class AdoUserRepository implements UserRepository {
    @Override
    public int getUserCountByUsername(String username) {
        return new User().getUserCountByUsername(username);
    }

    @Override
    public int getUserCountByEmail(String email) {
        return new User().getUserCountByEmail(email);
    }

    @Override
    public boolean add(String username, String password, String firstname, String lastname, String profilePicture, String email, String website, int permission, LocalDateTime registerDate, boolean isPMActive) {
        return new User().add(username,password,firstname,lastname,profilePicture,email,website,permission,registerDate,isPMActive);
    }

    @Override
    public int getId(String username) {
        return new User().getID(username);
    }
}
