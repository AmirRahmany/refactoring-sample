package com.hamkelasi.dal.refactored;

import java.time.LocalDateTime;

public interface UserRepository {
    int getUserCountByUsername(String username);
    int getUserCountByEmail(String email);
    boolean add(String username, String password, String firstname, String lastname, String profilePicture,
        String email, String website, int permission, LocalDateTime registerDate, boolean isPMActive);

    int getId(String username);
}
