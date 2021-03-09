package br.com.zup.bootcamp.ecommerce;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UncaughtExceptionHandler {

    @ExceptionHandler
    ResponseEntity<Collection<Map<String, String>>> onMethodArgumentInvalid(MethodArgumentNotValidException ex) {
        Collection<Map<String, String>> errors = new ArrayList<>();

        ex.getGlobalErrors().stream()
                .map(e -> Map.of(e.getObjectName(), e.getDefaultMessage()))
                .forEach(errors::add);

        ex.getFieldErrors().stream()
                .map(e -> Map.of(e.getField(), e.getDefaultMessage()))
                .forEach(errors::add);

        return ResponseEntity.badRequest().body(errors);
    }
}