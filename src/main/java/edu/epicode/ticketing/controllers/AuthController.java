package edu.epicode.ticketing.controllers;

import edu.epicode.ticketing.payloads.users.LoginDTO;
import edu.epicode.ticketing.payloads.users.LoginRespDTO;
import edu.epicode.ticketing.payloads.users.NewUserDTO;
import edu.epicode.ticketing.payloads.users.NewUserResponseDTO;
import edu.epicode.ticketing.services.AuthService;
import edu.epicode.ticketing.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import edu.epicode.ticketing.payloads.users.ForgotPasswordDTO;
import edu.epicode.ticketing.payloads.users.ResetPasswordDTO;
import edu.epicode.ticketing.services.PasswordResetService;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UsersService usersService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, UsersService usersService, PasswordResetService passwordResetService){
        this.authService = authService;
        this.usersService = usersService;
        this.passwordResetService = passwordResetService;
    }

   @PostMapping("login")
    public LoginRespDTO login(@Validated @RequestBody LoginDTO payload){
        return new LoginRespDTO(this.authService.checkCredentialsAndGenerateToken(payload));
   }

    //2.
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserResponseDTO register(@Validated @RequestBody NewUserDTO body){
        return this.usersService.saveUser(body);
    }
    
    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(@Validated @RequestBody ForgotPasswordDTO payload) {
        passwordResetService.createPasswordResetTokenForUser(payload.email());
        // Return a generic message regardless of whether the email exists to prevent user enumeration
        return Map.of("message", "If the email address is registered, you will receive a link to reset your password.");
    }
    
    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@Validated @RequestBody ResetPasswordDTO payload) {
        passwordResetService.resetPassword(payload.token(), payload.newPassword());
        return Map.of("message", "Password successfully reset.");
    }
}
