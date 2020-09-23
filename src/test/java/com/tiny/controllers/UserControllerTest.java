package com.tiny.controllers;

import com.tiny.services.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith( SpringExtension.class )
@WebMvcTest( UserController.class )
public class UserControllerTest {
    
    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        mockMvc.perform( get("/{id}", "d2411eaf5c6748178ee416e6d4a35186") )
            .andExpect( status().isUnauthorized() );
    }

}
