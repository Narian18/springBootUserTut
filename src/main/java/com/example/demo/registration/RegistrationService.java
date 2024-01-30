package com.example.demo.registration;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRole;
import com.example.demo.appUser.AppUserService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    public RegistrationService(EmailValidator emailValidator, AppUserService appUserService) {
        this.emailValidator = emailValidator;
        this.appUserService = appUserService;
    }

    public String register(RegistrationRequest request) throws IllegalStateException {
        boolean isValid = emailValidator.test(request.getEmail());
        if (!isValid) {
            throw new IllegalStateException("Not a valid email");
        }
        return appUserService.signUpUser(
                new AppUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    AppUserRole.USER
            )
        );
    }
}
