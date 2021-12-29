package com.vodafone.parkingticket.exception.util;

import com.vodafone.parkingticket.exception.AbstractCustomRuntimeException;
import com.vodafone.parkingticket.exception.ErrorResponse;
import com.vodafone.parkingticket.exception.handler.UnexpectedTypeExceptionHandler;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ErrorUtil {

    public static final Integer DEFAULT_ERROR_MESSAGE_CODE = 9999;

    private ErrorUtil(){}

    public static ResponseEntity<ErrorResponse> createResponseEntity(final Throwable throwable, final HttpStatus statusCode){
        return createResponseEntity(getErrorResponse(throwable), statusCode);
    }

    public static ResponseEntity<ErrorResponse> createResponseEntity(final ErrorResponse errorResponse, final HttpStatus statusCode) {
        final HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(errorResponse, header, statusCode);
    }

    private static ErrorResponse getErrorResponse(final Throwable throwable){
        return ErrorResponse.builder()
                .error(throwable.getClass().getSimpleName())
                .status(DEFAULT_ERROR_MESSAGE_CODE)
                .message(throwable.getMessage())
                .build();
    }

    public static ErrorResponse getErrorResponse(final AbstractCustomRuntimeException ex){
        return ErrorResponse.builder()
                .error(ex.getClass().getSimpleName())
                .status(ex.getStatus())
                .build();
    }

    public static ErrorResponse getErrorResponse(final ConstraintViolationException ex){
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        ConstraintViolationImpl<?> violation = (ConstraintViolationImpl<?>) violations.iterator().next();
        return ErrorResponse.builder()
                .error(ex.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(violation.getMessageTemplate())
                .build();
    }

    public static ErrorResponse getErrorResponse(final UnexpectedTypeException ex){
        return ErrorResponse.builder()
                .error(ex.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
    }

    public static ErrorResponse getErrorResponse(final MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ErrorResponse.builder()
                .error(ex.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errors.toString())
                .build();
    }
}
