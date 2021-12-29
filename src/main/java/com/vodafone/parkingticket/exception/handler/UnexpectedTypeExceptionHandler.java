package com.vodafone.parkingticket.exception.handler;

import com.vodafone.parkingticket.exception.ErrorResponse;
import com.vodafone.parkingticket.exception.util.ErrorUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.UnexpectedTypeException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class UnexpectedTypeExceptionHandler implements CustomExceptionHandler<UnexpectedTypeException>{
    @Override
    public ResponseEntity<ErrorResponse> handle(UnexpectedTypeException e) {
        final ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
        ErrorResponse errorResponse = ErrorUtil.getErrorResponse(e);
        return ErrorUtil.createResponseEntity(errorResponse, responseStatus == null ? BAD_REQUEST : responseStatus.value());
    }
}
