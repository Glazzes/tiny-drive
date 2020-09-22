package com.tiny.exceptions;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionDetails {
    
    private String title;
    private int statusCode;
    private String developerComment;
    private LocalDate timestamp;
    
}
