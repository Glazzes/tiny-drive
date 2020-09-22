package com.tiny.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JwtVerifier extends OncePerRequestFilter {

    private JwtConfigurationsProperties properties;
    public JwtVerifier( JwtConfigurationsProperties properties ){
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authorizationHeader = request.getHeader("Authorization");
        if( Strings.isNullOrEmpty(authorizationHeader) || 
            !authorizationHeader.startsWith( properties.getPrefix() + " " )){
                filterChain.doFilter(request, response);
                return;
        }

        String actualToken = authorizationHeader.replace( properties.getPrefix() + " ", "");

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                          .setSigningKey( properties.getHashedSecretKey() )
                          .build()
                          .parseClaimsJws(actualToken);

            Claims body = jws.getBody();

            String username = body.getSubject();
            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map( map -> new SimpleGrantedAuthority( map.get("authority") ) )
                .collect( Collectors.toSet() );

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                grantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            throw new IllegalStateException("This token can not be trusted " + actualToken);
        }

        filterChain.doFilter(request, response);
    }

}
