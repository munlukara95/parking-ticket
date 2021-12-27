package com.vodafone.parkingticket.config;


import com.vodafone.parkingticket.filter.CacheRequestResponseFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CacheRequestResponseFilter> cacheRequestResponseFilterFilterRegistrationBean(){
        FilterRegistrationBean<CacheRequestResponseFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CacheRequestResponseFilter());
        registrationBean.addUrlPatterns("/parking/*");

        return registrationBean;
    }
}
