package com.vodafone.parkingticket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GarageOperationException extends AbstractCustomRuntimeException {
    public GarageOperationException(Integer status){
        super(status);
    }
}
