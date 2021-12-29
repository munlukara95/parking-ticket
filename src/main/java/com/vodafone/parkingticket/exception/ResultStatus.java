package com.vodafone.parkingticket.exception;

public enum ResultStatus {
    ILLEGAL_ARGUMENT(5500),
    GARAGE_FULL(5501),
    VEHICLE_TYPE_NOT_FOUND(4452),
    VEHICLE_ID_NOT_FOUND(4453);

    private Integer status;

    ResultStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus(){
        return this.status;
    }
}
