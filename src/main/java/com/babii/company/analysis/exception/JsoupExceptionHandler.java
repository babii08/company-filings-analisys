package com.babii.company.analysis.exception;

import com.babii.company.analysis.dto.Company;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
@ControllerAdvice(basePackageClasses = Company.class)
@Order(HIGHEST_PRECEDENCE)
public class JsoupExceptionHandler {

    @ExceptionHandler(value = {JsoupException.class})
    public ResponseEntity<Object> handle(JsoupException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
