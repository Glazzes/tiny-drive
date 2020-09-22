package com.tiny.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.tiny.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    
    @NotBlank( message = "Username is reuired")
    @Size( min = 3, message = "Username must be at least 3 characters long" )
    private String username;

    @NotBlank( message = "Password is required" )
    @Size( min = 3, message = "Username must be at least 3 characters long" )
    private String password;

    @NotBlank( message = "Email is required" )
    @Email( message = "Email must be like \"example@example.com\" " )
    private String email;

    public User toUser( String username, String password, String email){
        return new User( username, password, email);
    }

}