package com.babii.company.analysis.exception;

import lombok.Getter;

@Getter
public class JsoupException extends RuntimeException {

    private final String message;

    public JsoupException(String message) {
        this.message = message;
    }
}
