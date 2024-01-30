package com.example.demo.registration;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRole;
import com.example.demo.appUser.AppUserService;
import com.example.demo.registration.api_schemas.RegistrationRequest;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    public RegistrationService(EmailValidator emailValidator,
                               AppUserService appUserService,
                               ConfirmationTokenService confirmationTokenService) {
        this.emailValidator = emailValidator;
        this.appUserService = appUserService;
        this.confirmationTokenService = confirmationTokenService;
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

    public String confirmToken(String token) throws IllegalStateException {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException(String.format("token %s not found", token)));

        if (confirmationToken.getConfirmDate() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiryDate = confirmationToken.getExpiryDate();

        if (expiryDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token is expired");
        }

        confirmationTokenService.setConfirmDate(confirmationToken);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

        return "confirmed";
    }
}
