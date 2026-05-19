package edu.epicode.ticketing.controllers;

import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.payloads.users.NewUserDTO;
import edu.epicode.ticketing.payloads.users.UpdateUserProfileDTO;
import edu.epicode.ticketing.payloads.users.UserProfileDTO;
import edu.epicode.ticketing.payloads.users.UserProfileForAdminDTO;
import edu.epicode.ticketing.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import edu.epicode.ticketing.payloads.users.UserListDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/*
1. GET http://localhost:3001/users -> 200 OK
2. POST http://localhost:3001/users (+ payload) -> 201 CREATED
3. GET http://localhost:3001/users/{usersId}  -> 200 OK
4. PUT http://localhost:3001/users/{usersId} (+ payload) -> 200 OK
5. DELETE http://localhost:3001/users/{usersId} -> 204 NO CONTENT
 */

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UsersService usersService;

    //1.
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public Page<UserListDTO> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir,
            @RequestParam(required = false) String search,
            @AuthenticationPrincipal User currentUser
    ){
        return this.usersService.findAll(page, size, sortBy, sortDir, search, currentUser);
    }

    //2.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public UserProfileDTO createUser(@Validated @RequestBody UserProfileForAdminDTO body){
        return this.usersService.createUserAsAdmin(body);
    }

    //3.
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public UserProfileDTO getUserById(@PathVariable UUID userId){
        User user = this.usersService.findById(userId);
        return this.usersService.getProfile(user);
    }

    //4.
    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public UserProfileDTO updateUserById(@PathVariable UUID userId, @Validated @RequestBody UserProfileForAdminDTO body){
        return this.usersService.updateUserAsAdmin(userId, body);
    }

    //5.
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public void deleteUserById(@PathVariable UUID userId){
        this.usersService.findByIdAndDelete(userId);
    }


    @PatchMapping("/{userId}/avatar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public UserProfileDTO uploadImage(@PathVariable UUID userId, @RequestParam("avatar") MultipartFile file) throws IOException {
        return this.usersService.uploadProfilePicture(userId, file);
    }

    @PatchMapping("/me/avatar")
    public UserProfileDTO uploadOwnImage(@AuthenticationPrincipal User currentAuthenticateUser, @RequestParam("avatar") MultipartFile file) throws IOException {
        return this.usersService.uploadProfilePicture(currentAuthenticateUser.getId(), file);
    }

    @GetMapping("/me")
    public UserProfileDTO getOwnProfile(@AuthenticationPrincipal User currentAuthenticateUser){
        return this.usersService.getProfile(currentAuthenticateUser);
    }

    @PutMapping("/me")
    public UserProfileDTO updateOwnProfile(@AuthenticationPrincipal User currentAuthenticateUser, @Validated @RequestBody UpdateUserProfileDTO body){
        return this.usersService.updateProfile(currentAuthenticateUser, body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnProfile(@AuthenticationPrincipal User currentAuthenticateUser){
        this.usersService.findByIdAndDelete(currentAuthenticateUser.getId());
    }

}
