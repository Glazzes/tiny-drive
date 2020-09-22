package com.tiny.exceptions;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler( value = {CapacityExcededException.class} )
    public ResponseEntity<ExceptionDetails> handleCapacityExcededException(
        CapacityExcededException excededCapacity
    ){
        return new ResponseEntity<ExceptionDetails>(
            ExceptionDetails.builder()
                            .title("You can't upload this file, no storage left")
                            .developerComment( CapacityExcededException.class.getName() )
                            .timestamp( LocalDate.now() )
                            .statusCode( HttpStatus.INSUFFICIENT_STORAGE.value() )
                            .build(), HttpStatus.INSUFFICIENT_STORAGE
        );
    }

    @ExceptionHandler( value = {UsernameAlreadyExistsException.class} )
    public ResponseEntity<ExceptionDetails> handleUsernameAleadyExistException(
        UsernameAlreadyExistsException usernameExist
    ){
        return new ResponseEntity<ExceptionDetails>(
            ExceptionDetails.builder()
                            .title(usernameExist.getMessage() + " is already in use." )
                            .statusCode( HttpStatus.BAD_REQUEST.value() )
                            .developerComment( usernameExist.getClass().getName())
                            .timestamp( LocalDate.now() )
                            .build(), HttpStatus.BAD_REQUEST
        );
    }


   @ExceptionHandler( value = {InvalidJwtException.class} )
   public ResponseEntity<ExceptionDetails> handleInvalidJwtException(
       InvalidJwtException invalidJwtException
   ){
       return new ResponseEntity<ExceptionDetails>(
           ExceptionDetails.builder()
                            .title( invalidJwtException.getMessage() )
                            .statusCode( HttpStatus.INTERNAL_SERVER_ERROR.value() )
                            .timestamp( LocalDate.now() )
                            .developerComment( InvalidJwtException.class.getName() )
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
       );
   }

}
