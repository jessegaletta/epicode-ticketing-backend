package edu.epicode.ticketing.controllers;

import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.payloads.users.NewUserDTO;
import edu.epicode.ticketing.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public List<User> getUsers(){
        return this.usersService.findAll();
    }

    //3.
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId){
        return this.usersService.findById(userId);
    }

    //4.
    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public User updateUserById(@PathVariable UUID userId, @RequestBody NewUserDTO body){
        return this.usersService.findByIdAndUpdate(userId, body);
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
    public void uploadImage(@PathVariable UUID userId, @RequestParam("avatar") MultipartFile file) throws IOException {
        this.usersService.uploadProfilePicture(userId, file);
    }

    @PutMapping("/me")
    public User updateOwnProfile(@AuthenticationPrincipal User currentAuthenticateUser, @RequestBody NewUserDTO body){
        return this.usersService.findByIdAndUpdate(currentAuthenticateUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnProfile(@AuthenticationPrincipal User currentAuthenticateUser){
        this.usersService.findByIdAndDelete(currentAuthenticateUser.getId());
    }

}
