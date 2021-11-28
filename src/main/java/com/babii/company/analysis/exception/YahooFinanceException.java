package com.babii.company.analysis.exception;

import org.springframework.http.HttpStatus;

public class YahooFinanceException extends RuntimeException{

    private final String message;
    private final Throwable exception;
    private final HttpStatus status;

    public YahooFinanceException(String message, Throwable exception, HttpStatus status) {
        super(message, exception);
        this.message = message;
        this.exception = exception;
        this.status = status;
    }
}
