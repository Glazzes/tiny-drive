package com.tiny.validators;

import com.tiny.entities.User;
import com.tiny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UsernameValidator implements ConstraintValidator<UsernameAlreadyExists, String>{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UsernameAlreadyExists constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isEmpty();
    }
}


