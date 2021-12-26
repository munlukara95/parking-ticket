package com.vodafone.parkingticket.exception.advisor;

import com.vodafone.parkingticket.exception.ErrorResponse;
import com.vodafone.parkingticket.exception.handler.CustomExceptionHandler;
import com.vodafone.parkingticket.exception.handler.Handlers;
import com.vodafone.parkingticket.exception.util.ErrorUtil;
import com.vodafone.parkingticket.exception.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.Objects;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ControllerAdvisor {

    private final MessageUtil messageUtil;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(final Exception ex){
        ResponseEntity<ErrorResponse> responseEntity = null;

        Throwable exception = ex;
        final CustomExceptionHandler customExceptionHandler = Handlers.getHandler(ex.getClass());
        if(Objects.nonNull(customExceptionHandler)){
            responseEntity = customExceptionHandler.handle(ex);
        } else {
            final ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
            responseEntity = ErrorUtil.createResponseEntity(exception, responseStatus == null ? INTERNAL_SERVER_ERROR : responseStatus.value());
        }

        ErrorResponse errorResponse = responseEntity.getBody();
        if (ObjectUtils.isEmpty(errorResponse.getMessage())) {
            errorResponse.setMessage(messageUtil.getMessageByStatus(errorResponse.getStatus()));
        }
        HttpServletRequest request = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        errorResponse.setPath(request.getRequestURI());

        log.error("ERROR RESPONSE: {}", errorResponse);
        return responseEntity;
    }
}
