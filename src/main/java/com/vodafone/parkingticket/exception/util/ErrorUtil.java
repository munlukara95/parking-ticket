package com.vodafone.parkingticket.exception.util;

import com.vodafone.parkingticket.exception.AbstractCustomRuntimeException;
import com.vodafone.parkingticket.exception.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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
                .build();
    }

    public static ErrorResponse getErrorResponse(final AbstractCustomRuntimeException ex){
        return ErrorResponse.builder()
                .error(ex.getClass().getSimpleName())
                .status(ex.getStatus())
                .build();
    }
}
