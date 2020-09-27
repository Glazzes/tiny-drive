package com.tiny.services;

import com.tiny.entities.User;
import com.tiny.entities.VerificationToken;
import com.tiny.repository.VerificationTokenRepository;
import org.aspectj.weaver.ast.Call;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class VerificationTokenService  {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken findByToken(String token){
        return verificationTokenRepository.findByToken(token);
    }

    public VerificationToken findByUser(User user){
        return verificationTokenRepository.findByUser(user);
    }

    public void save(String token, User user){
        VerificationToken newToken = new VerificationToken(token, user);
        newToken.setExpireDate( calculateExpireDate(24*60) );
        verificationTokenRepository.save(newToken);
    }

    private Timestamp calculateExpireDate(int expireTimeInMinutes){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expireTimeInMinutes);
        return new Timestamp(calendar.getTime().getTime());
    }

}
