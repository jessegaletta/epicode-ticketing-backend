package edu.epicode.ticketing.services;

import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.exceptions.UnauthorizedException;
import edu.epicode.ticketing.payloads.users.LoginDTO;
import edu.epicode.ticketing.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.epicode.ticketing.exceptions.NotFoundException;

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
        try {
            // 1.1. search for user by email
            User found = this.usersService.findByEmail(body.email());
            // 1.2 if user exists verify password
            if(bcrypt.matches(body.password(), found.getPassword())){
                return this.tokenTools.generateToken(found);
            }
        } catch (NotFoundException e) {
            // I do nothing here, I throw the exception below
        }
        
        // 3. If anything goes wrong -> 401
        throw new UnauthorizedException("Wrong credentials!");
    }
}
