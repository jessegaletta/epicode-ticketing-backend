package edu.epicode.ticketing.runner;

import edu.epicode.ticketing.entities.Role;
import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @Override
    public void run(String... args) throws Exception {
        if (usersRepository.count() == 0) {
            User rootUser = new User("Root", "Admin", "root@admin.com", bcrypt.encode("root"));
            // Depending on the available roles, set it to ADMIN or USER
            // rootUser.setRole(Role.ADMIN); // Let's set ADMIN if Role.ADMIN exists, else Role.USER
            // The User constructor sets role to Role.USER. Let's see if Role.ADMIN exists.
            rootUser.setRole(Role.ADMIN);
            usersRepository.save(rootUser);
            System.out.println("Root user created: root@admin.com / root");
        }
    }
}
