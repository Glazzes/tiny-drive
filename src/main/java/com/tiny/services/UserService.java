package com.tiny.services;

import java.util.List;
import java.util.Optional;

import com.tiny.entities.User;
import com.tiny.exceptions.UsernameAlreadyExistsException;
import com.tiny.models.UserModel;
import com.tiny.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@NoArgsConstructor
public class UserService {
    
    private UserRepository userRepo;
    private BCryptPasswordEncoder bcrypt;

    @Autowired
    public UserService( UserRepository userRepo, BCryptPasswordEncoder bcrypt ){
        this.userRepo = userRepo;
        this.bcrypt = bcrypt;
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


    public User registerNewUser( UserModel userInfo ) throws UsernameAlreadyExistsException{
        String username = userInfo.getUsername();
        String password = userInfo.getPassword();
        String email = userInfo.getEmail();

        Optional<User> existingUser = userRepo.findByUsername(username);
        if( !existingUser.isPresent()){
            User newUser = new User( username, bcrypt.encode(password), email );
            userRepo.save(newUser);
            return newUser;
        }

        return null;
    }


    public User editUserProfile( UserModel userInfo, String userId )throws UsernameAlreadyExistsException{
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
