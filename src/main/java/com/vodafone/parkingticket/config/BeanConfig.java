package com.vodafone.parkingticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.StampedLock;

@Configuration
public class BeanConfig {

    @Bean
    public StampedLock getStampedLock(){
        return new StampedLock();
    }
}
