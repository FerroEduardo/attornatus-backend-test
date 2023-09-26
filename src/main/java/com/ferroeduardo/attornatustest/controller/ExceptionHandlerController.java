package com.ferroeduardo.attornatustest.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName    = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> handleException(ConstraintViolationException exception) {
        Map<String, String> body = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
            String message = constraintViolation.getMessage();
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                if (node.getKind() == ElementKind.METHOD) continue;
                body.put(node.getName(), message);
            }
        }

        return ResponseEntity.badRequest().body(body);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> body  = new HashMap<>();
        Throwable           cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {
            for (JsonMappingException.Reference reference : invalidFormatException.getPath()) {
                String fieldName = reference.getFieldName();
                body.put(fieldName, "invalid format");
            }
        }

        if (!body.isEmpty()) {
            return ResponseEntity.badRequest().body(body);
        }
        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }
}
