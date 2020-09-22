package com.tiny.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.io.IOException;

@ResponseStatus( code = HttpStatus.INSUFFICIENT_STORAGE )
public class CapacityExcededException extends IOException{

    private static final long serialVersionUID = 1L;

    public CapacityExcededException(String message) {
        super(message);
    }
}
