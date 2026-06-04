package edu.epicode.ticketing.services;

import edu.epicode.ticketing.entities.PasswordResetToken;
import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.exceptions.ValidationException;
import edu.epicode.ticketing.repositories.PasswordResetTokenRepository;
import edu.epicode.ticketing.repositories.UsersRepository;
import edu.epicode.ticketing.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MailgunSender mailgunSender;

    @Autowired
    private PasswordEncoder bcrypt;

    @Value("${cors.allowed.origins:http://localhost:5173}")
    private String frontendUrl;

    @Transactional
    public void createPasswordResetTokenForUser(String email) {
        // Clean up any expired tokens first
        tokenRepository.deleteAllExpiredSince(LocalDateTime.now());

        Optional<User> userOpt = usersRepository.findByEmail(email.toLowerCase());
        
        // No error is thrown if the user does not exist, to prevent user enumeration.
        if (userOpt.isEmpty()) {
            return;
        }
        
        User user = userOpt.get();
        
        String tokenStr = UUID.randomUUID().toString();
        Optional<PasswordResetToken> existingTokenOpt = tokenRepository.findByUser(user);
        PasswordResetToken myToken;
        
        // If a token already exists for this user, it is updated rather than creating a new one.
        if (existingTokenOpt.isPresent()) {
            myToken = existingTokenOpt.get();
            myToken.setToken(tokenStr);
            myToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        } else {
            myToken = new PasswordResetToken(tokenStr, user, LocalDateTime.now().plusMinutes(30));
        }
        
        tokenRepository.save(myToken);
        
        String resetUrl = frontendUrl + "/reset-password?token=" + tokenStr;
        mailgunSender.sendPasswordResetEmail(user, resetUrl);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ValidationException("Invalid or missing password reset token."));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new ValidationException("The password reset token has expired. Please request a new one.");
        }

        User user = resetToken.getUser();
        user.setPassword(bcrypt.encode(newPassword));
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        usersRepository.save(user);

        tokenRepository.delete(resetToken);
    }
}
