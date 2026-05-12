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

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UsersService usersService;

    public AuthController(AuthService authService, UsersService usersService){
        this.authService = authService;
        this.usersService = usersService;
    }

   @PostMapping("login")
    public LoginRespDTO login(@Validated @RequestBody LoginDTO payload){
        return new LoginRespDTO(this.authService.checkCredentialsAndGenerateToken(payload));
   }

    //2.
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserResponseDTO NewUserResponseDTO(@Validated @RequestBody NewUserDTO body){
        return this.usersService.saveUser(body);
    }
}
