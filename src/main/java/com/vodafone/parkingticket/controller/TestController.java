package com.vodafone.parkingticket.controller;

import com.vodafone.parkingticket.exception.ResultStatus;
import com.vodafone.parkingticket.exception.UnExpectedErrorException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.vodafone.parkingticket.constant.ApiConstants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX + "/test")
public class TestController {

    @GetMapping
    public String test(){
        return "Test controller is working perfectly";
    }

    @PostMapping
    public String testWithException(){
        throw new UnExpectedErrorException(ResultStatus.ILLEGAL_ARGUMENT.getStatus());
    }
}

