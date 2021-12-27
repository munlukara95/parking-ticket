package com.vodafone.parkingticket.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Objects;

@Component
@Slf4j
public class CacheRequestResponseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(httpServletResponse);

        filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);

        printRequestData(httpServletRequest, contentCachingRequestWrapper);

        printResponseData(httpServletResponse, contentCachingResponseWrapper);

    }

    private Enumeration<String> getParamsOrHeadersNames(boolean isHeader, HttpServletRequest httpServletRequest){
        if(isHeader){
            return httpServletRequest.getHeaderNames();
        }
        else {
            return httpServletRequest.getParameterNames();
        }
    }

    private String getParamOrHeaderValue(boolean isHeader, HttpServletRequest req, String paramOrHeaderName){
        if(isHeader){
            return req.getHeader(paramOrHeaderName);
        }
        else {
            return req.getParameter(paramOrHeaderName);
        }
    }

    private String getJsonValueParamsOrHeaders(boolean isHeader, HttpServletRequest req){
        StringBuilder jsonValue = new StringBuilder("{");
        Enumeration<String> paramOrHeaderNames = getParamsOrHeadersNames(isHeader, req);
        while(paramOrHeaderNames.hasMoreElements()){
            String name = paramOrHeaderNames.nextElement();
            String value = getParamOrHeaderValue(isHeader, req, name);
            jsonValue.append(" " + name + " : " + value + " ");
        }
        jsonValue.append("}");
        return jsonValue.toString();
    }

    private String getRequestParamsWithName(HttpServletRequest req){
        return getJsonValueParamsOrHeaders(false, req);
    }

    private String getHeaderValuesWithName(HttpServletRequest req){
        return getJsonValueParamsOrHeaders(true, req);
    }

    private void printRequestData(HttpServletRequest httpServletRequest, ContentCachingRequestWrapper contentCachingRequestWrapper)throws UnsupportedEncodingException {
        String methodType = httpServletRequest.getMethod();
        String headerNamesAndValues = getHeaderValuesWithName(httpServletRequest);
        String paramNamesAndValues = getRequestParamsWithName(httpServletRequest);
        String path = httpServletRequest.getServletPath();
        String requestBody = "";
        byte[] requestBodyByteArray = contentCachingRequestWrapper.getContentAsByteArray();
        if(Objects.nonNull(contentCachingRequestWrapper) && Objects.nonNull(requestBodyByteArray)){
            requestBody = new String(requestBodyByteArray, contentCachingRequestWrapper.getCharacterEncoding());
        }
        log.info("REQUEST - [{}] - [{}] - Header : [{}] - Params : [{}] - Body : [{}]", methodType, path, headerNamesAndValues, paramNamesAndValues, requestBody);
    }

    private void printResponseData(HttpServletResponse httpServletResponse, ContentCachingResponseWrapper contentCachingResponseWrapper) throws IOException{
        Integer status = httpServletResponse.getStatus();
        byte[] responseBodyByteArray = contentCachingResponseWrapper.getContentAsByteArray();
        String responseBody = "";
        if(Objects.nonNull(contentCachingResponseWrapper) && Objects.nonNull(responseBodyByteArray)){
            responseBody = new String(responseBodyByteArray, contentCachingResponseWrapper.getCharacterEncoding());
        }
        log.info("RESPONSE - [{}] - Body : [{}]", status, responseBody);
        contentCachingResponseWrapper.copyBodyToResponse();
    }


}

