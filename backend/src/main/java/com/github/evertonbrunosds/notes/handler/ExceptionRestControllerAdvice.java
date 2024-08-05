package com.github.evertonbrunosds.notes.handler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.github.evertonbrunosds.notes.util.ResourceException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionRestControllerAdvice {

    private static final String BODY = "body";

    private static final String PARAMETER = "parameter";

    private static String capitalizeFirstLetter(@NonNull final String target) {
        return Character.toUpperCase(target.charAt(0)) + target.substring(1);
    }

    private static void addErrorMessage(final Map<String, List<String>> bodyErrors, final FieldError fieldError) {
        final var messageError = fieldError.getDefaultMessage();
        if (messageError != null) {
            bodyErrors.computeIfAbsent(fieldError.getField(), key -> {
                return new LinkedList<>();
            }).add(capitalizeFirstLetter(messageError));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(final MethodArgumentNotValidException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        final var bodyErrors = new HashMap<String, List<String>>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> addErrorMessage(bodyErrors, fieldError));
        allErrors.put(BODY, bodyErrors);
        return ResponseEntity.badRequest().body(allErrors);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> handle(final InvalidFormatException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        final var bodyErrors = new HashMap<String, List<String>>();
        final var fieldName = exception.getPath().get(0).getFieldName();
        final var fullErrorMessage = exception.getMessage().split(":")[0];
        final var pattern = Pattern.compile("(\\w+)`");
        final var matcher = pattern.matcher(fullErrorMessage);
        if (matcher.find()) {
            final var errorMessage = fullErrorMessage.replaceAll("`[^`]+`", matcher.group(1)).replace("\"", "'");
            bodyErrors.put(fieldName, List.of(errorMessage));
        } else {
            bodyErrors.put(fieldName, List.of());
        }
        allErrors.put(BODY, bodyErrors);
        return ResponseEntity.badRequest().body(allErrors);
    }

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<?> handle(final ResourceException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        allErrors.put(exception.getType().toString(), exception.getContent());
        return ResponseEntity.status(exception.getStatus()).body(allErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handle(final HttpMessageNotReadableException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        final var bodyErrors = new HashMap<String, List<String>>();
        if (exception.getCause() instanceof InvalidFormatException) {
            return handle((InvalidFormatException) exception.getCause());
        }
        bodyErrors.put("_", List.of(exception.getCause() instanceof JsonParseException
                ? exception.getMessage()
                : "Required valid request body is missing"));
        allErrors.put(BODY, bodyErrors);
        return ResponseEntity.badRequest().body(allErrors);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handle(final HttpMediaTypeNotSupportedException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        final var bodyErrors = new HashMap<String, List<String>>();
        bodyErrors.put("_", List.of(exception.getMessage()));
        allErrors.put(BODY, bodyErrors);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(allErrors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handle(MethodArgumentTypeMismatchException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        final var bodyErrors = new HashMap<String, List<String>>();
        bodyErrors.put(exception.getPropertyName(), List.of(exception.getMessage()));
        allErrors.put(PARAMETER, bodyErrors);
        return ResponseEntity.badRequest().body(allErrors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handle(MissingServletRequestParameterException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        final var bodyErrors = new HashMap<String, List<String>>();
        bodyErrors.put(exception.getParameterName(), List.of(exception.getMessage()));
        allErrors.put(PARAMETER, bodyErrors);
        return ResponseEntity.badRequest().body(allErrors);
    }

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<?> handle(MismatchedInputException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        final var bodyErrors = new HashMap<String, List<String>>();
        bodyErrors.put("_", List.of(exception.getMessage()));
        allErrors.put(PARAMETER, bodyErrors);
        return ResponseEntity.badRequest().body(allErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handle(final ConstraintViolationException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        final var bodyErrors = new HashMap<String, List<String>>();
        exception.getConstraintViolations().forEach(violation -> {
            final var splittedPropertyPath = violation.getPropertyPath().toString().split("\\.");
            final var propertyName = splittedPropertyPath[splittedPropertyPath.length - 1];
            bodyErrors.computeIfAbsent(propertyName, key -> {
                return new LinkedList<>();
            }).add(violation.getMessage());
        });
        allErrors.put(PARAMETER, bodyErrors);
        return ResponseEntity.badRequest().body(allErrors);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<?> handle(MissingPathVariableException exception) {
        final var allErrors = new HashMap<String, Map<String, List<String>>>();
        final var bodyErrors = new HashMap<String, List<String>>();
        bodyErrors.put(exception.getVariableName(), List.of(exception.getMessage()));
        allErrors.put(PARAMETER, bodyErrors);
        return ResponseEntity.badRequest().body(allErrors);
    }

}
