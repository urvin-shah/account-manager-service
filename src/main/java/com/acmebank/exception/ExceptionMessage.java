package com.acmebank.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ExceptionMessage {
    private String exception;
    private HttpStatus errorCode;
    private LocalDateTime date;
}
