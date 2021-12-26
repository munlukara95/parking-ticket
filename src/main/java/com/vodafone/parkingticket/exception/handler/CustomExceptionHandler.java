package com.vodafone.parkingticket.exception.handler;

import com.vodafone.parkingticket.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;

public interface CustomExceptionHandler<T extends Throwable> {
    ResponseEntity<ErrorResponse> handle(T e);
}
