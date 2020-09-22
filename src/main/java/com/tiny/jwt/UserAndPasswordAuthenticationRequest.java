package com.tiny.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAndPasswordAuthenticationRequest {
    
    private String username;
    private String password;

}