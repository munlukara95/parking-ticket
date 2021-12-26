package com.vodafone.parkingticket.exception;

public abstract class AbstractCustomRuntimeException extends RuntimeException {
    private Integer status;

    public AbstractCustomRuntimeException() {
    }

    public AbstractCustomRuntimeException(Integer status) {
        this.status = status;
    }

    public AbstractCustomRuntimeException(Throwable cause) {
        super(cause);
    }

    public AbstractCustomRuntimeException(Throwable cause, Integer status) {
        super(cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
