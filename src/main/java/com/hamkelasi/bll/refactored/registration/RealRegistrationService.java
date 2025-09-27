package com.hamkelasi.bll.refactored.registration;

import com.hamkelasi.bll.User;
import com.hamkelasi.dal.refactored.AdoUserRepository;
import com.hamkelasi.dal.refactored.UserRepository;

public class RealRegistrationService implements RegistrationService {
    private RegisterUserDTO registerUserDTO;
    private final UserRepository repository;

    public RealRegistrationService(UserRepository repository) {
        this.repository = repository;
    }


    public RealRegistrationService() {
        this.repository = new AdoUserRepository();
    }

    @Override
    public int isValid(String username, String email, String website) {
        return new User().isValid(username, email, website);
    }

    @Override
    public RegisterUserDTO getRegisteredDto() {
        return registerUserDTO;
    }

    @Override
    public RegistrationResult register(RegisterUserDTO dto) {
        registerUserDTO = dto;
        var result = new RegistrationResult();
        result.resultCode = 0;

        if (dto.profilePicture == null || dto.profilePicture.isEmpty()) {
            dto.profilePicture = "/UserImages/default.png";
        }

        boolean inserted = repository.add(dto.username, dto.password, dto.firstName, dto.lastName, dto.profilePicture, dto.email, dto.website, dto.permission, dto.registerDate, dto.isPmActive);

        if (!inserted) {
            result.resultCode = 9; //اشکال در ثبت داده
        }

        if (result.resultCode == 0) {
            result.userId = repository.getId(dto.username);
        }

        return result;
    }
}
