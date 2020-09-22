package com.tiny;

import com.tiny.jwt.JwtConfigurationsProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity( prePostEnabled = true )
@EnableConfigurationProperties( value = {JwtConfigurationsProperties.class} )
public class TinyDriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyDriveApplication.class, args);
	}

}
