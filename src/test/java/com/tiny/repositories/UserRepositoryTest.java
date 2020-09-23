package com.tiny.repositories;

import java.util.Arrays;
import java.util.List;

import java.util.Optional;

import com.tiny.entities.User;
import com.tiny.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith( SpringExtension.class )
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepo;

    List<User> users;

    @BeforeEach
    public void init(){
        List<User> users = Arrays.asList(
            new User( "delta", "deata", "delta@demo.com" ),
            new User( "tango", "tango", "tango@demo.com" ),
            new User( "foxtrot", "foxtrot", "foxtrot@demo.com" )
        );

        users.stream().forEach( userRepo::save );

        this.users = users;
    }

    @AfterEach
    public void finish(){
        users.stream().forEach(userRepo::delete);
        this.users = null;
    }

    @Test
    public void test(){
        String deltaId = users.get(0).getId();
        String tangoId = users.get(1).getId();
        String foxtrotId = users.get(2).getId();
        String invalidId = "Guess what? A non valid id";

        User delta = userRepo.findById(deltaId).get();
        User tango = userRepo.findById(tangoId).get();
        User foxtrot = userRepo.findById(foxtrotId).get();
        Optional<User> notExistingUser = userRepo.findById(invalidId);

        assertThat( delta.getId() ).isEqualTo( deltaId );
        assertThat( tango.getId() ).isEqualTo( tangoId );
        assertThat( foxtrot.getId() ).isEqualTo( foxtrotId );
        assertFalse( notExistingUser.isPresent() );
    }

    @Test
    public void findAllUsers_hasSize_Integer(){
        assertThat( userRepo.findAll() ).isNotNull();
        assertThat( userRepo.findAll() ).isNotEmpty();
        assertThat( userRepo.findAll()).hasSize(3);
        userRepo.save(new User( "bravo", "bravo", "bravo@demo.com" ));
        assertThat( userRepo.findAll() ).hasSize(4);
    }

    @Test
    public void save_vpdateUser_void(){
        String deltaId = users.get(0).getId();
        String changedUsername = "evergreen";
        String changedEmail = "evergreen@demo.com";

        User delta = userRepo.findById(deltaId).get();
        delta.setUsername( changedUsername );
        delta.setEmail( changedEmail );

        userRepo.save(delta);

        User editedDelta = userRepo.findById(deltaId).get();

        assertTrue( userRepo.findById(deltaId).isPresent() );
        assertThat( userRepo.findById(deltaId).get() ).isNotNull();
        assertThat( editedDelta.getUsername() ).isEqualTo(changedUsername);
        assertThat( editedDelta.getEmail()).isEqualTo(changedEmail);
    }

    @Test
    public void deleteUser(){
        String deltaId = users.get(0).getId();
        Optional<User>delta = userRepo.findById(deltaId);
        assertTrue( delta.isPresent() );

        userRepo.delete(delta.get());
        assertThat( userRepo.findAll() ).hasSize(2);
    }

}
