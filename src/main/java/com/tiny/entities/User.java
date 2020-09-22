package com.tiny.entities;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tiny.configuration.ApplicationUserRoles;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "users" )
@Data
@NoArgsConstructor
public class User {
    
    @Id
    @Column( name = "id" )
    private String id;

    @Column( name = "username", nullable = false, unique = true, length = 100 )
    private String username;

    @Column( name = "password", nullable = false, length = 150 )
    private String password;

    @Column( name = "memberSince", nullable = false )
    private LocalDate memberSince;

    @Column( name = "profilePicture", nullable = false )
    private String profilePicture;

    @Enumerated( EnumType.STRING )
    @Column( name = "roles", nullable = false)
    private ApplicationUserRoles roles;

    @Column( name = "editableUsername", nullable = false, length = 100 )
    private String editableUsername;

    @Column( name = "email", length = 150, nullable = false )
    private String email;

    public User( String username, String password, String email){
        this.id = UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "");
        this.username = username;
        this.editableUsername = username;
        this.password = password;
        this.memberSince = LocalDate.now();
        this.profilePicture = "default.png";
        this.roles = ApplicationUserRoles.USER;
        this.email = email;
    }

}