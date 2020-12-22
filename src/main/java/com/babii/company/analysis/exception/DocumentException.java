package com.babii.company.analysis.exception;

import org.springframework.http.HttpStatus;

public class DocumentException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public DocumentException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
