package com.hamkelasi.bll.refactored;

import com.hamkelasi.bll.User;

public class RealUserService implements UserService {
    private User user;
    @Override
    public int isValid(String username, String email, String website) {
        return user.isValid(username,email,website);
    }

    @Override
    public void register(RegisterUserDto dto) {
        user.add(dto.username,dto.password,dto.firstName, dto.lastName, dto.profilePicture,
                dto.email,dto.website, dto.permission,dto.registerDate, dto.isPmActive);
    }
}
