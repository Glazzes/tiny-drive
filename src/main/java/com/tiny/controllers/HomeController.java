package com.tiny.controllers;

import com.tiny.entities.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class HomeController {

    @GetMapping
    public String home( @RequestParam(name = "string", defaultValue = "world") String param ){
        return String.format("Hello, %s", param);
    }

    @GetMapping( path = "/json", produces = "application/json" )
    public ResponseEntity<User> json(){
        return new ResponseEntity<User>( new User("glaze", "glaze", "glaze@demo.com"), HttpStatus.CREATED );
    }
}