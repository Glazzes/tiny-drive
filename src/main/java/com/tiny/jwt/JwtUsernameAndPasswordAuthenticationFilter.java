package com.tiny.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager manager;
    private JwtConfigurationsProperties properties;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager manager,
            JwtConfigurationsProperties properties) {
        this.manager = manager;
        this.properties = properties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            UserAndPasswordAuthenticationRequest userRequest = new ObjectMapper()
            .readValue( request.getInputStream(), UserAndPasswordAuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userRequest.getUsername(),
                userRequest.getPassword()
            );
            
            Authentication authenticate = manager.authenticate(authentication);

            return authenticate;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
       
        String token = Jwts.builder()
                       .setSubject( authResult.getName() )
                       .setIssuedAt( new Date() )
                       .setExpiration( java.sql.Date.valueOf( LocalDate.now().plusDays(properties.getValidityInDays()) ))
                       .claim("authorities", authResult.getAuthorities())
                       .signWith( properties.getHashedSecretKey() )
                       .compact();

        response.addHeader("Authorization", String.join(" ", properties.getPrefix(), token));
    }

    

}