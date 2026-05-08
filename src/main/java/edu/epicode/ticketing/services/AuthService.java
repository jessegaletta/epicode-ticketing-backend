package edu.epicode.ticketing.services;

import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.exceptions.UnauthorizedException;
import edu.epicode.ticketing.payloads.users.LoginDTO;
import edu.epicode.ticketing.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersService usersService;
    private final TokenTools tokenTools;
    private final PasswordEncoder bcrypt;

    public AuthService(UsersService usersService, TokenTools tokenTools, PasswordEncoder bcrypt){
        this.usersService = usersService;
        this.tokenTools = tokenTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body){
        // 1. Check credentials

        // 1.1. search for user by email
        User found = this.usersService.findByEmail(body.email());
        // 1.2 if user exists verify password
        if(bcrypt.matches(body.password(), found.getPassword())){
            return this.tokenTools.generateToken(found);
        } else {
            throw new UnauthorizedException("Wrong credentials!");
        }
        // 2. If everything is fine -> generate the access token
        // 3.  If anything goes wrong -> 401
    }
}
