package com.tiny.services;

import com.tiny.entities.User;
import com.tiny.models.UserModel;
import com.tiny.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith( MockitoExtension.class )
@ExtendWith( SpringExtension.class )
public class UserSerivceTest {
    
    @MockBean
    private UserRepository userRepo;

    @MockBean
    BCryptPasswordEncoder bcrypt;

    @InjectMocks
    private UserService userService;

    User user;

    UserModel model;

    @BeforeEach
    public void init(){
        this.model = new UserModel("glaze", "glaze", "glaze@demo.com");

        this.user = new User(
            model.getUsername(),
            model.getPassword(),
            model.getEmail()
        );
    }

    @Test
    public void testing(){
        Mockito.when( userService.registerNewUser(model)).thenReturn( user );
    }

    @Test
    public void findById(){
        Optional<User> optional = Optional.of(user);
        Mockito.when( userService.findUserByid(user.getId()) ).thenReturn(user); 
        Mockito.when( userService.findUserByid( "NonValidId" ) ).thenReturn(null);
    }

}
