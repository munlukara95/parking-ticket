package com.vodafone.parkingticket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundException extends AbstractCustomRuntimeException{
    public NotFoundException(Integer status){
        super(status);
    }
}
