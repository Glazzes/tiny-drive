package com.tiny.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.tiny.entities.User;

import com.tiny.validators.UsernameAlreadyExists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    @UsernameAlreadyExists
    @NotBlank( message = "Username is required")
    @Size( min = 3, message = "Username must be at least 3 characters long" )
    private String username;

    @NotBlank( message = "Password is required" )
    @Size( min = 3, message = "Username must be at least 3 characters long" )
    private String password;

    @NotBlank( message = "Email is required" )
    @Email( message = "Email must be like \"example@example.com\" " )
    private String email;

}