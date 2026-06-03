package edu.epicode.ticketing.services;

import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.exceptions.UnauthorizedException;
import edu.epicode.ticketing.payloads.users.LoginDTO;
import edu.epicode.ticketing.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.epicode.ticketing.exceptions.NotFoundException;

import edu.epicode.ticketing.repositories.UsersRepository;
import edu.epicode.ticketing.tools.MailgunSender;
import edu.epicode.ticketing.entities.Role;
import java.util.List;

@Service
public class AuthService {

    private final UsersService usersService;
    private final TokenTools tokenTools;
    private final PasswordEncoder bcrypt;
    private final UsersRepository usersRepository;
    private final MailgunSender mailgunSender;

    public AuthService(UsersService usersService, TokenTools tokenTools, PasswordEncoder bcrypt, UsersRepository usersRepository, MailgunSender mailgunSender){
        this.usersService = usersService;
        this.tokenTools = tokenTools;
        this.bcrypt = bcrypt;
        this.usersRepository = usersRepository;
        this.mailgunSender = mailgunSender;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body){
        try {
            User found = this.usersService.findByEmail(body.email());
            
            if (found.isAccountLocked()) {
                throw new UnauthorizedException("Your account is locked. Please reset your password to unlock it.");
            }

            if(bcrypt.matches(body.password(), found.getPassword())){
                if (found.getFailedLoginAttempts() > 0) {
                    found.setFailedLoginAttempts(0);
                    usersRepository.save(found);
                }
                return this.tokenTools.generateToken(found);
            } else {
                found.setFailedLoginAttempts(found.getFailedLoginAttempts() + 1);
                // After 5 failed attempts the account is locked and all admins are notified by email.
                if (found.getFailedLoginAttempts() >= 5) {
                    found.setAccountLocked(true);
                    List<User> admins = usersRepository.findByRole(Role.ADMIN);
                    for (User admin : admins) {
                        mailgunSender.sendAccountLockedAdminNotification(found, admin.getEmail());
                    }
                }
                usersRepository.save(found);
            }
        } catch (NotFoundException e) {
            // The same exception is thrown below in both cases to avoid revealing whether the email exists.
        }

        // 3. If anything goes wrong -> 401
        throw new UnauthorizedException("Wrong credentials!");
    }
}
