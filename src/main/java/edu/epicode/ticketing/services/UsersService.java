package edu.epicode.ticketing.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.entities.UserSettings;
import edu.epicode.ticketing.exceptions.NotFoundException;
import edu.epicode.ticketing.exceptions.ValidationException;
import edu.epicode.ticketing.payloads.users.NewUserDTO;
import edu.epicode.ticketing.payloads.users.NewUserResponseDTO;
import edu.epicode.ticketing.repositories.UsersRepository;
import edu.epicode.ticketing.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserSettingsService userSettingsService;
    @Autowired
    private Cloudinary cloudinaryUploader;
    @Autowired
    private MailgunSender mailgunSender;
    @Autowired
    private PasswordEncoder bcrypt;

    public List<User> findAll(){
        return this.usersRepository.findAll();
    }

    public NewUserResponseDTO saveUser(NewUserDTO body) {
        if (body.firstName().length() < 2) throw new ValidationException("Name cannot be shorter than 2 characters");

        if(usersRepository.existsByEmail(body.email())){
            throw new ValidationException("Email already in use");
        }

        User newUser = new User(body.firstName(), body.lastName(), body.email(), bcrypt.encode(body.password()));

        UserSettings settings = new UserSettings(newUser);
        newUser.setUserSettings(settings);
        mailgunSender.sendRegistrationEmail(newUser);

        User savedUser = this.usersRepository.save(newUser);

       //UserSettings settings = this.userSettingsService.save(savedUser);

        System.out.println("Created user with id: " + savedUser.getId());
        System.out.println("Created user's settings with id: " + settings.getId());
        return new NewUserResponseDTO(savedUser.getId());
    }

    public User findById(UUID userId){
        Optional<User> foundOrNot = this.usersRepository.findById(userId);

        if(foundOrNot.isPresent()) return foundOrNot.get();
        else {
            throw new NotFoundException(userId);
        }
    }

    public User findByIdAndUpdate(UUID userId, NewUserDTO body){
        User found = this.findById(userId);
        // TODO: Validate data
        found.setFirstName(body.firstName());
        found.setLastName(body.lastName());
        found.setEmail(body.email());
        found.setPassword(body.password());
        found.setAvatarURL("https://ui-avatars.com/api/?name=" + body.firstName() + "+" + body.lastName());

        return this.usersRepository.save(found);
    }

    public void findByIdAndDelete(UUID userId){
        User found = this.findById(userId);
        this.usersRepository.delete(found);
    }

    public void uploadProfilePicture(UUID userId, MultipartFile file) throws IOException {
        // Check if user exist
        // Validate (check size, check format)
        try{
            Map<?, ?> response = this.cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "eiot/avatars"));
            String imageURL = response.get("secure_url").toString();
            System.out.println(imageURL); // TODO: Store this url inside that user's profile
        } catch (IOException e) {
            throw new RuntimeException(""); // TODO: handle exception
        }
    }

    public User findByEmail(String email){
         return usersRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Users with email" + email + " not found" ));
    }
}
