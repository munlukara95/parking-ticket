package com.vodafone.parkingticket.exception;

public enum ResultStatus {
    ILLEGAL_ARGUMENT(5500);

    private Integer status;

    ResultStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus(){
        return this.status;
    }
}
