package com.vodafone.parkingticket.exception.handler;

import com.vodafone.parkingticket.exception.AbstractCustomRuntimeException;
import com.vodafone.parkingticket.exception.ErrorResponse;
import com.vodafone.parkingticket.exception.util.ErrorUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class CustomRuntimeExceptionHandler implements CustomExceptionHandler<AbstractCustomRuntimeException> {
    @Override
    public ResponseEntity<ErrorResponse> handle(AbstractCustomRuntimeException exception) {
        final ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        ErrorResponse errorResponse = ErrorUtil.getErrorResponse(exception);
        return ErrorUtil.createResponseEntity(errorResponse, responseStatus == null ? INTERNAL_SERVER_ERROR : responseStatus.value());
    }
}
