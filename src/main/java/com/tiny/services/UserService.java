package com.tiny.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tiny.entities.User;
import com.tiny.models.UserModel;
import com.tiny.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private UserRepository userRepo;
    private BCryptPasswordEncoder bcrypt;
    private VerificationTokenService verificationTokenService;
    private EmailService emailService;

    @Autowired
    public UserService(
            UserRepository userRepo,
            BCryptPasswordEncoder bcrypt,
            VerificationTokenService verificationTokenService,
            EmailService emailService
    ){
        this.userRepo = userRepo;
        this.bcrypt = bcrypt;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }

    public User save(User user){
        user.setPassword( bcrypt.encode(user.getPassword()) );
        return userRepo.save(user);
    }

    public User regitserUser(User user){
        Optional<User> savedUser = Optional.of(save(user));
        savedUser.ifPresent( u -> {
            try{
                String token = UUID.randomUUID().toString();
                verificationTokenService.save(token, savedUser.get());

                emailService.sendVerificationEmail(savedUser.get());
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        return savedUser.get();
    }

    public List<User> findAllUsers(){
        List<User> allUsers = userRepo.findAll();
        return allUsers;
    }

    public User findUserByid( String id ){
        Optional<User> user = userRepo.findById(id);

        if( user.isPresent() ){
            User foundUser = user.get();
            return foundUser;
        }
        return null;
    }

    public User editUserProfile( UserModel userInfo, String userId ){
        String username = userInfo.getUsername();
        String password = userInfo.getPassword();
        String email = userInfo.getEmail();

        Optional<User> userFoundByid = userRepo.findById(userId);
        userFoundByid.orElseThrow( () -> new UsernameNotFoundException( "The user with id " + userId + " does not exist"));
        User userToEdit = userFoundByid.get();

        userToEdit.setEditableUsername(username);
        userToEdit.setPassword(password);
        userToEdit.setEmail(email);
        userRepo.save( userToEdit );

        return userToEdit;
    }

    public void deleteUserAccount( String userId ){
        Optional<User> userFoundById = userRepo.findById(userId);
        User userTodelete = userFoundById.get();

        userRepo.delete(userTodelete);
    }

}
