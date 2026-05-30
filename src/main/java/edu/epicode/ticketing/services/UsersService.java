package edu.epicode.ticketing.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.entities.UserSettings;
import edu.epicode.ticketing.exceptions.NotFoundException;
import edu.epicode.ticketing.exceptions.ValidationException;
import edu.epicode.ticketing.payloads.users.NewUserDTO;
import edu.epicode.ticketing.payloads.users.NewUserResponseDTO;
import edu.epicode.ticketing.payloads.users.UpdateUserProfileDTO;
import edu.epicode.ticketing.payloads.users.UserProfileDTO;
import edu.epicode.ticketing.payloads.users.UserProfileForAdminDTO;
import edu.epicode.ticketing.payloads.users.UserListDTO;
import edu.epicode.ticketing.entities.Role;
import edu.epicode.ticketing.repositories.UsersRepository;
import edu.epicode.ticketing.repositories.TicketsRepository;
import edu.epicode.ticketing.repositories.TicketActivityRepository;
import edu.epicode.ticketing.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private TicketsRepository ticketsRepository;
    @Autowired
    private TicketActivityRepository ticketActivityRepository;
    @Autowired
    private Cloudinary cloudinaryUploader;
    @Autowired
    private MailgunSender mailgunSender;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private BachelorService bachelorService;

    public List<User> findAll() {
        return this.usersRepository.findAll();
    }

    public Page<UserListDTO> findAll(int page, int size, String sortBy, String sortDir, String search,
            User authenticatedUser) {
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortDir), sortBy);
        if (sortBy.equalsIgnoreCase("firstName") || sortBy.equalsIgnoreCase("lastName") || sortBy.equalsIgnoreCase("email")) {
            order = order.ignoreCase();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<User> usersPage;
        if (search != null && !search.trim().isEmpty()) {
            usersPage = this.usersRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            search, search, search, pageable);
        } else {
            usersPage = this.usersRepository.findAll(pageable);
        }

        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;

        return usersPage.map(user -> new UserListDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                isAdmin || user.getId().equals(authenticatedUser.getId())));
    }

    public NewUserResponseDTO saveUser(NewUserDTO body) {
        if (body.firstName().length() < 2)
            throw new ValidationException("Name cannot be shorter than 2 characters");

        if (usersRepository.existsByEmail(body.email())) {
            throw new ValidationException("Email already in use");
        }

        User newUser = new User(body.firstName(), body.lastName(), body.email(), bcrypt.encode(body.password()));
        newUser.setBachelor(bachelorService.findById(body.bachelorId()));

        UserSettings settings = new UserSettings(newUser);
        newUser.setUserSettings(settings);
        mailgunSender.sendRegistrationEmail(newUser);

        User savedUser = this.usersRepository.save(newUser);

        // UserSettings settings = this.userSettingsService.save(savedUser);

        System.out.println("Created user with id: " + savedUser.getId());
        System.out.println("Created user's settings with id: " + settings.getId());
        return new NewUserResponseDTO(savedUser.getId());
    }

    public User findById(UUID userId) {
        Optional<User> foundOrNot = this.usersRepository.findById(userId);

        if (foundOrNot.isPresent())
            return foundOrNot.get();
        else {
            throw new NotFoundException(userId);
        }
    }

    public UserProfileDTO createUserAsAdmin(UserProfileForAdminDTO body) {
        if (body.firstName().length() < 2)
            throw new ValidationException("Name cannot be shorter than 2 characters");

        if (usersRepository.existsByEmail(body.email())) {
            throw new ValidationException("Email already in use");
        }

        if (body.password() == null || body.password().trim().isEmpty()) {
            throw new ValidationException("Password is required for new users");
        }

        User newUser = new User(body.firstName(), body.lastName(), body.email(), bcrypt.encode(body.password()));
        if (body.role() != null) {
            newUser.setRole(body.role());
        }

        if (newUser.getRole() == Role.STUDENT && body.bachelorId() == null) {
            throw new ValidationException("Bachelor is required for STUDENT");
        }

        if (body.bachelorId() != null) {
            newUser.setBachelor(bachelorService.findById(body.bachelorId()));
        }



        UserSettings settings = new UserSettings(newUser);
        settings.setDarkMode(body.darkMode());
        settings.setTimezone(body.timezone() != null ? body.timezone() : "Europe/Belgrade");
        settings.setDateFormat(body.dateFormat() != null ? body.dateFormat() : "DD/MM/YYYY");
        settings.setTimeFormat(body.timeFormat() != null ? body.timeFormat() : "24h");

        newUser.setUserSettings(settings);
        mailgunSender.sendRegistrationEmail(newUser);

        User savedUser = this.usersRepository.save(newUser);
        return getProfile(savedUser);
    }

    public UserProfileDTO updateUserAsAdmin(UUID userId, UserProfileForAdminDTO body) {
        User found = this.findById(userId);

        found.setFirstName(body.firstName());
        found.setLastName(body.lastName());
        found.setEmail(body.email());
        if (body.role() != null) {
            found.setRole(body.role());
        }

        if (found.getRole() == Role.STUDENT && body.bachelorId() == null) {
            throw new ValidationException("Bachelor is required for STUDENT");
        }

        if (body.bachelorId() != null) {
            found.setBachelor(bachelorService.findById(body.bachelorId()));
        } else {
            found.setBachelor(null);
        }

        if (body.password() != null && !body.password().isEmpty()) {
            found.setPassword(bcrypt.encode(body.password()));
        }

        UserSettings settings = found.getUserSettings();
        if (settings == null) {
            settings = new UserSettings(found);
            found.setUserSettings(settings);
        }

        settings.setDarkMode(body.darkMode());
        settings.setTimezone(body.timezone());
        settings.setDateFormat(body.dateFormat());
        settings.setTimeFormat(body.timeFormat());

        User savedUser = this.usersRepository.save(found);
        return getProfile(savedUser);
    }

    public UserProfileDTO getProfile(User user) {
        UserSettings settings = user.getUserSettings();
        if (settings == null) {
            settings = new UserSettings(user);
        }
        Long bachelorId = user.getBachelor() != null ? user.getBachelor().getId() : null;
        String bachelorDescription = user.getBachelor() != null ? user.getBachelor().getDescription() : null;
        return new UserProfileDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAvatarURL(),
                user.getRole(),
                settings.isDarkMode(),
                settings.getTimezone(),
                settings.getDateFormat(),
                settings.getTimeFormat(),
                bachelorId,
                bachelorDescription);
    }

    public UserProfileDTO updateProfile(User user, UpdateUserProfileDTO body) {
        user.setFirstName(body.firstName());
        user.setLastName(body.lastName());
        user.setEmail(body.email());
        if (user.getRole() == Role.STUDENT && body.bachelorId() == null) {
            throw new ValidationException("Bachelor is required for STUDENT");
        }

        if (body.bachelorId() != null) {
            user.setBachelor(bachelorService.findById(body.bachelorId()));
        } else {
            user.setBachelor(null);
        }

        if (body.password() != null && !body.password().isEmpty()) {
            user.setPassword(bcrypt.encode(body.password()));
        }

        UserSettings settings = user.getUserSettings();
        if (settings == null) {
            settings = new UserSettings(user);
            user.setUserSettings(settings);
        }

        settings.setDarkMode(body.darkMode());
        settings.setTimezone(body.timezone());
        settings.setDateFormat(body.dateFormat());
        settings.setTimeFormat(body.timeFormat());

        User savedUser = this.usersRepository.save(user);
        return getProfile(savedUser);
    }

    @org.springframework.transaction.annotation.Transactional
    public void findByIdAndDelete(UUID userId) {
        User found = this.findById(userId);

        if (found.getRole() == Role.ADMIN) {
            long adminCount = this.usersRepository.countByRole(Role.ADMIN);
            if (adminCount <= 1) {
                throw new ValidationException("Cannot delete the only admin in the system.");
            }
        }

        this.ticketsRepository.detachUserFromTickets(found);
        this.ticketActivityRepository.detachUserFromActivities(found);
        this.usersRepository.delete(found);
    }

    public UserProfileDTO uploadProfilePicture(UUID userId, MultipartFile file) throws IOException {
        // Validate size (max 10MB for Cloudinary standard free tier)
        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new ValidationException("File size exceeds 10MB limit.");
        }

        // Validate format (Cloudinary supports standard image formats)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ValidationException("Invalid file format. Only images are allowed.");
        }

        // Check if user exist
        User user = this.findById(userId);

        try {
            Map<?, ?> response = this.cloudinaryUploader.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "eiot/avatars"));
            String imageURL = response.get("secure_url").toString();
            user.setAvatarURL(imageURL);
            this.usersRepository.save(user);
            return getProfile(user);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image");
        }
    }

    public User findByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Users with email " + email + " not found"));
    }
}
