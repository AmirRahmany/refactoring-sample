package com.hamkelasi.bll.refactored.registration;

import com.hamkelasi.bll.User;

public class RealUserService implements UserService {
    private final User user;
    private RegisterUserDTO registerUserDTO;

    public RealUserService(User user) {
        this.user = user;
    }

    public RealUserService() {
        this.user = new User();
    }

    @Override
    public int isValid(String username, String email, String website) {
        return user.isValid(username,email,website);
    }

    @Override
    public int getId() {
        return user.getId();
    }

    @Override
    public RegisterUserDTO getRegisteredDto() {
        return registerUserDTO;
    }

    @Override
    public int register(RegisterUserDTO dto) {
        registerUserDTO = dto;
        return user.add(dto.username,dto.password,dto.firstName, dto.lastName, dto.profilePicture,
                dto.email,dto.website, dto.permission,dto.registerDate, dto.isPmActive);
    }
}
