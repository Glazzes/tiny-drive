package com.tiny.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static com.tiny.configuration.ApplicationUserRoles.*;

import com.tiny.jwt.JwtConfigurationsProperties;
import com.tiny.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.tiny.jwt.JwtVerifier;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private JwtConfigurationsProperties properties;

    @Autowired
    public WebSecurityConfiguration(
        UserDetailsService userDetailsService,
        JwtConfigurationsProperties properties
    ){
        this.userDetailsService = userDetailsService;
        this.properties = properties;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable()
            .addFilter( new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), properties) )
            .addFilterAfter( new JwtVerifier(properties), JwtUsernameAndPasswordAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/actuator/**").hasRole(ADMIN.name())
            .antMatchers( HttpMethod.GET, "/").authenticated()
            .anyRequest().permitAll();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}