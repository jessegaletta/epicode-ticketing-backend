package edu.epicode.ticketing.services;

import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.entities.UserSettings;
import edu.epicode.ticketing.repositories.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {
    @Autowired
    private UserSettingsRepository userSettingsRepository;
//    @Autowired
//    private UsersService usersService;
//
//    public UserSettings save(UUID userId) {
//        // 1. find the user
//        User userFromDB = this.usersService.findById(userId);
//        // 2. create the settings object
//        UserSettings settings = new UserSettings(userFromDB);
//        // 3 save it
//        return this.userSettingsRepository.save(settings);
//    }

    public UserSettings save(User user){
        UserSettings settings = new UserSettings(user);
        return this.userSettingsRepository.save(settings);
    }
}
