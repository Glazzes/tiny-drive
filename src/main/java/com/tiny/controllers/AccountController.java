package com.tiny.controllers;

import com.tiny.entities.User;
import com.tiny.entities.VerificationToken;
import com.tiny.exceptions.InvalidVerificationTokenException;
import com.tiny.services.UserService;
import com.tiny.services.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;

@RestController
@RequestMapping("/account")
@CrossOrigin("*")
@Slf4j
public class AccountController {

    private final VerificationTokenService verificationTokenService;
    private final UserService userService;
    public AccountController(
            VerificationTokenService verificationTokenService,
            UserService userService
    ) {
        this.verificationTokenService = verificationTokenService;
        this.userService = userService;
    }

    @GetMapping("/verify")
    public void verifyNewAccount(@RequestParam(name = "token") String token){
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        log.info( verificationToken.getUser().getUsername() );

        if(verificationToken == null){
            throw new InvalidVerificationTokenException("Verification token is invalid");
        }else{
            User user = verificationToken.getUser();
            if(!user.isEnabled()){
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                if(verificationToken.getExpireDate().before(currentTime)){
                    throw new InvalidVerificationTokenException("Verification token has expired");
                }else{
                    user.setEnabled(true);
                    userService.save(user);
                }
            }
        }
    }

}
