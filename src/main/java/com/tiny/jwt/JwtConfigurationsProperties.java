package com.tiny.jwt;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.jsonwebtoken.security.Keys;
import lombok.Data;

@ConfigurationProperties( prefix = "application.jwt.configuration" )
@Data
public class JwtConfigurationsProperties {
    private String prefix;
    private int validityInDays;
    private String secretKey;

    public SecretKey getHashedSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
