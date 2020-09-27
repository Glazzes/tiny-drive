package com.tiny.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.tiny.entities.User;
import com.tiny.models.UserModel;
import com.tiny.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping( "/user" )
@CrossOrigin("*")
@Slf4j
public class UserController {

    private UserService userService;
    public UserController( UserService userService ){
        this.userService = userService;
    }

    @PostMapping( path = "/register", consumes = "application/json")
    @Transactional
    public ResponseEntity<?> registerNewUser(
        @Valid @RequestBody UserModel userModel
    ){
        User newUser = new User(userModel);
        userService.regitserUser(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /*  @PreAuthorize( "authentication.principal.id eq #id" ) */
    @PostMapping( path = "/{id}/edit/profile-picture", consumes = "multipart/form-data" )
    public ResponseEntity<?> submitProfilePicture(
        @RequestParam( name = "filename", required = true) MultipartFile picture
    ){

        try {
            Path outputFolder = Paths.get("someFolder", "profile-pcitures");
            Files.write(outputFolder, picture.getBytes());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            log.info( e.getMessage() );
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping( path="/{id}", produces = "application/json")
    public ResponseEntity<User> getUserById( @PathVariable( name = "id", required= true ) String id ){
        User foundUserById = userService.findUserByid(id);

        if( foundUserById != null ){
            return new ResponseEntity<>( foundUserById, HttpStatus.FOUND );
        }else{
            return ResponseEntity.notFound().build();
        }

    }


    @GetMapping( path = "/all", produces = "application/json" )
    public ResponseEntity<List<User>> retrieveAllUsers(){
        List<User> allUsers = userService.findAllUsers();
        return new ResponseEntity<List<User>>( allUsers, HttpStatus.OK);
    }


   /*  @PreAuthorize( "authentication.principal.id eq #id" ) */
    @PatchMapping( path = "/{id}/edit", consumes = "application/json" )
    @Transactional
    public ResponseEntity<User> editUserProfile( @Valid @RequestBody UserModel model, @PathVariable("id") String id ){
        try {
            User userToEdit = userService.editUserProfile( model, id);
            return new ResponseEntity<User>( userToEdit, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }


    /* @PreAuthorize( "authentication.principal.id eq #id" ) */
    @DeleteMapping( path = "/{id}/deleteAccount", consumes = "application/json" )
    @Transactional
    public ResponseEntity<?> deleteUserAccount( @PathVariable("id") String userId ){
        userService.deleteUserAccount(userId);
        return ResponseEntity.ok().build();
    }

}
